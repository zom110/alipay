package com.sdhoo.pdloan.payctr.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.pdloan.bcrud.model.DtPctOutNotifyRcd;
import com.sdhoo.pdloan.bcrud.model.DtPctSysPayRcd;
import com.sdhoo.pdloan.bcrud.model.DtPctUserPayRcd;
import com.sdhoo.pdloan.bcrud.service.DtPctOutNotifyRcdService;
import com.sdhoo.pdloan.bcrud.service.DtPctSysPayRcdService;
import com.sdhoo.pdloan.bcrud.service.DtPctUserPayRcdService;
import com.sdhoo.pdloan.payctr.PaycenterThreadPools;
import com.sdhoo.pdloan.payctr.enums.PctOutNotifyStepEnum;
import com.sdhoo.pdloan.payctr.enums.SysPayStepStatusEnum;
import com.sdhoo.pdloan.payctr.enums.SyspayIfcChnlEnum;
import com.sdhoo.pdloan.payctr.enums.UserPayIfcChnlEnum;
import com.sdhoo.pdloan.payctr.enums.UserPayStepStatusEnum;
import com.sdhoo.pdloan.payctr.service.PayctrOutNotifyService;
import com.sdhoo.pdloan.payctr.service.PayctrScanBusiService;
import com.sdhoo.pdloan.payctr.service.SysPayService;
import com.sdhoo.pdloan.payctr.service.UserPayService;

/**
 * 扫描类业务服务实现类
 *
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-20 15:55:45
 */
@Service
public class PayctrScanBusiServiceImpl implements PayctrScanBusiService {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(PayctrScanBusiServiceImpl.class);


    @Resource
    private DtPctOutNotifyRcdService dtPctOutNotifyRcdService;

    @Resource
    private PayctrOutNotifyService payctrOutNotifyService;


    @Autowired
    private DtPctUserPayRcdService pctUserPayRcdService;

    @Autowired
    private UserPayService userPayService;


    /**
     * 异步发起支付结果核对
     *
     * @param uprCode
     */
    private void asyncCheckUserPayRcdByCode(final String uprCode) {
        Runnable nthread = new Runnable() {
            @Override
            public void run() {
                try {
                    userPayService.payRecordCheckAndDoBusi(uprCode);
                } catch (BaseServiceException e) {
                    logger.error("异步通知出异常了", e);
                }
            }
        };
        PaycenterThreadPools.userpayMntThreadPool.submit(nthread);
    }


    @Override
    public void doScanAndCheckAllForCheckUserPayRcd() {
        // 查找所有需要核对的支付记录
        Map<String, Object> notifyQmap = new HashMap<>(4);
        List<Integer> ifcChnlId_inList = new ArrayList<Integer>();
//		ifcChnlId_inList.add(UserPayIfcChnlEnum.BAOFU_PAY_DK.getCode());
        ifcChnlId_inList.add(UserPayIfcChnlEnum.YIBAOPAY_BINDDK.getCode());
        ifcChnlId_inList.add(UserPayIfcChnlEnum.FUYOU_BINDDK.getCode());
        ifcChnlId_inList.add(UserPayIfcChnlEnum.ALIPAY.getCode());
        notifyQmap.put("ifcChnlId_inList", ifcChnlId_inList);
        notifyQmap.put("stepStatus", UserPayStepStatusEnum.WAITE4NTF.getCode());
        List<DtPctUserPayRcd> toNotifyUprList = pctUserPayRcdService.selectByCriteria(notifyQmap, 0, Integer.MAX_VALUE);
        System.out.println("JSON.toJSONString(toNotifyUprList) = " + JSON.toJSONString(toNotifyUprList));
        for (DtPctUserPayRcd pctUserPayRcd : toNotifyUprList) {
            String uprCode = pctUserPayRcd.getUprCode();
            // 异步发起核对.
            asyncCheckUserPayRcdByCode(uprCode);
        }
    }


    @Override
    public void doScanAndCallToNotifyDataAndNotify() {

        String logPre = "扫描并发起支付状态通知-->";
        Integer onePageMax = 500;
        boolean isContinue = true;
        Long maxId = 0L;
        Map<String, Object> planQmap = new HashMap<>();
        Date nextPlanTime_le = new Date();
        List<Integer> stepStatus_inList = new ArrayList<>();

        stepStatus_inList.add(PctOutNotifyStepEnum.NOTIFY_FAIL.getCode());
        stepStatus_inList.add(PctOutNotifyStepEnum.UN_NOTIFY.getCode());

        planQmap.put("ponrId_gt", maxId);
        planQmap.put("nextPlanTime_le", nextPlanTime_le);
        planQmap.put("stepStatus_inList", stepStatus_inList);

        List<String> orderBy_conditionList = new ArrayList<String>();
        orderBy_conditionList.add("ponrId_asc");

        while (isContinue) {
            List<DtPctOutNotifyRcd> dataInfList = dtPctOutNotifyRcdService.selectByCriteria(planQmap, 0, onePageMax);
            int curSize = dataInfList.size();
            if (curSize < onePageMax) {
                isContinue = false;
            }
            for (DtPctOutNotifyRcd dtPctOutNotifyRcd : dataInfList) {
                Long ponrId = dtPctOutNotifyRcd.getPonrId();
                if (maxId < ponrId) {
                    maxId = ponrId;
                }
                try {
                    payctrOutNotifyService.asynvCheckAndStartNotify(dtPctOutNotifyRcd);
                } catch (Exception e) {
                    logger.error(logPre + "处理记录(" + ponrId + ")时出现异常了", e);
                }
            }
        }
    }


    @Autowired
    private DtPctSysPayRcdService pctSysPayRcdService;

    @Autowired
    private SysPayService sysPayService;

    private void asyncCheckNotifySysPayRcd(final String sprCode) {
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                try {
                    sysPayService.doPayRcdCheckAndDoBusi(sprCode);
                } catch (Exception e) {
                    logger.error("异步核对代付记录出异常了", e);
                }
            }
        };
        // 加入到线程池.
        PaycenterThreadPools.syspayMntThreadPool.submit(thread);
    }

    @Override
    public void doScanAndCheckAllForCheckSysPayRcd() {
        Map<String, Object> inNotifyQmap = new HashMap<>(3);
        List<Integer> ifcChnlId_inList = new ArrayList<Integer>();
        ifcChnlId_inList.add(SyspayIfcChnlEnum.BAOFUDF.getCode());
        ifcChnlId_inList.add(SyspayIfcChnlEnum.YIBAODF.getCode());
        ifcChnlId_inList.add(SyspayIfcChnlEnum.FUIOUDF.getCode());
        inNotifyQmap.put("ifcChnlId_inList", ifcChnlId_inList);
        inNotifyQmap.put("stepStatus", SysPayStepStatusEnum.WAITE4NTF.getCode());
        List<DtPctSysPayRcd> inNotifyList = pctSysPayRcdService.selectByCriteria(inNotifyQmap, 0, Integer.MAX_VALUE);
        for (DtPctSysPayRcd pctSysPayRcd : inNotifyList) {
            asyncCheckNotifySysPayRcd(pctSysPayRcd.getSprCode());
        }
    }


}
