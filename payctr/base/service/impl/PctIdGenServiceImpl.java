package com.sdhoo.pdloan.payctr.base.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdhoo.common.base.service.BaseBytesCacheService;
import com.sdhoo.pdloan.payctr.base.dto.PctIdData;
import com.sdhoo.pdloan.payctr.base.service.PctIdGenService;
import com.sdhoo.pdloan.payctr.util.PayctUtils;


/**
 * ID生成服务.
 * @author SDPC_LIU
 *
 */
@Service
public class PctIdGenServiceImpl implements PctIdGenService {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(PctIdGenServiceImpl.class);

    /**
     * 字节缓存服务
     */
    @Autowired
    private BaseBytesCacheService baseBytesCacheService ;

    /**
     * 缓存交易号前缀,用于确认交易使用,保证唯一性.
     */
    private static final String CACHEKEY_PRE_PAYCODE = "payctr_paycode:" ; 


    /**
     * 缓存交易号时间,用于创建交易时判断,交易发起必须在交易号创建时间内完成.
     */
    private static final int CACHEKEY_TIMESEC_PAYCODE = 3600 ; 


    @Override
    public PctIdData genAndCachePayctIdData(){
        // 创建订单编号
        PctIdData genPctIdData = PayctUtils.genPctIdData();
        String idvalStr = genPctIdData.getIdVal().toString();
        String cacheKey = CACHEKEY_PRE_PAYCODE +  idvalStr ;
        boolean putNx = baseBytesCacheService.putAndExpireByteArrayNx(cacheKey, idvalStr.getBytes(), CACHEKEY_TIMESEC_PAYCODE);
        while(!putNx) {
            genPctIdData = PayctUtils.genPctIdData();
            idvalStr = genPctIdData.getIdVal().toString();
            cacheKey = CACHEKEY_PRE_PAYCODE +  idvalStr ;
            putNx = baseBytesCacheService.putAndExpireByteArrayNx(cacheKey, idvalStr.getBytes(), CACHEKEY_TIMESEC_PAYCODE);
        }
        return genPctIdData; 
    }

    
    
    

//    /**
//     * 创建订单编号: 规则为: 月数(3)+月中小时数(3)+序列(6) , 共12位,
//     */
//    @Override
//    public String genOrderCode() {
//        String rtVal = null ; 
//        Long currSecsSeq = -1L ;
//        // 计算起始年份
//        int startYear = 2018 ; 
//        // 序列最大值(长度6位)
//        long seqMaxVal = 999999;
//        Date inDate= new Date();
//        while( rtVal == null ) {
//            Calendar currDateCld = Calendar.getInstance();
//            if(inDate != null) {
//                currDateCld.setTime(inDate); 
//            }
//            // 年份
//            int yearNum = currDateCld.get(Calendar.YEAR);
//            // 月数
//            int month = currDateCld.get(Calendar.MONTH);
//            // 月内天数
//            int dayOfMonth = currDateCld.get(Calendar.DAY_OF_MONTH);
//            // 天内小时数
//            int hourOfday = currDateCld.get(Calendar.HOUR_OF_DAY);
//            // 月码
//            int monthNum = 100+(yearNum-startYear)*12 + month ;
//            // 月中的小时数
//            int monthHourNum = ((dayOfMonth-1) * 24 ) + (hourOfday) ;
//
//            // 月中的小时数字串(补0)
//            String monthHourValStr = fillZeroNum3len(monthNum) + fillZeroNum3len(monthHourNum)  ;
//            String cacheKey = CACHEKEY_MONTHHOUR_ODRCODE_PRE + monthHourValStr; 
//            // 获取时间毫秒数
//            long currentTimeMillis = currDateCld.getTimeInMillis();
//            // 取末尾倒数7位到倒数2位共6位,作为最小值
//            currSecsSeq = (currentTimeMillis%10000000)/10 ; 
//            currSecsSeq = checkOrIncNextSeq(cacheKey,1,currSecsSeq); 
//            if(currSecsSeq < 0 || currSecsSeq > seqMaxVal ) {
//                try {
//                    // 初始化序值
//                    currSecsSeq = -1L ;
//                    // 计算到下一周期需要休眠的时间
//                    Calendar nextLoopCld = Calendar.getInstance();
//                    nextLoopCld.setTime(currDateCld.getTime());
//                    // 加一周期
//                    nextLoopCld.add(Calendar.HOUR, 1);
//                    // 小于周期的单位赋为0
//                    nextLoopCld.set(Calendar.MINUTE, 0);
//                    nextLoopCld.set(Calendar.SECOND, 0);
//                    nextLoopCld.set(Calendar.MILLISECOND, 0);
//                    long sleepMillSec = nextLoopCld.getTime().getTime() - currDateCld.getTime().getTime();
//                    // 记录日志 
//                    logger.warn("当前周期序列达到上限("+seqMaxVal+"),休眠"+sleepMillSec+"毫秒后继续..");
//                    Thread.sleep(sleepMillSec);
//                    inDate = new Date();
//                    continue ;
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }else {
//                String rtValStr = monthHourValStr + fillZeroNum6len(currSecsSeq); 
//                rtVal = rtValStr ;
//                break;
//            }
//        }
//        return rtVal ;
//    }


//    /**
//     * 核对或加1一个序列值缓存.
//     * @param cacheKey
//     * @param incVal
//     * @return
//     */
//    private long checkOrIncNextSeq(String cacheKey, long incVal , long minVal) {
//
//        // 初始化最小值
//        if(incVal < 1 ) {
//            incVal = 1 ;
//        }
//        if(minVal < incVal) {
//            minVal = incVal;
//        }
//        long rtVal = baseBytesCacheService.incrBy(cacheKey, incVal);
//
//        // 增量的值小于最小值,则调整为最小值
//        if(rtVal < minVal){ 
//            long subVal = (minVal-rtVal); 
//            rtVal = baseBytesCacheService.incrBy(cacheKey, subVal); 
//        }
//
//        // 第一次赋值,则重置过期时间
//        if(rtVal == minVal) {
//            // (保证时间大于一个周期).
//            int expireSeconds = BaseBytesCacheService.SECONDS_ONEHOUR*2;
//            baseBytesCacheService.reExpireByteArray(cacheKey, expireSeconds); 
//        }
//        return rtVal ;
//    }


//    /**
//     * 
//     * 创建10长度的数字串
//     * @param inNum
//     * @return
//     */
//    public static String fillZeroNum9len(long inNum) {
//        return inNum < 10 ? ("00000000" + inNum ) : inNum < 100 ? ("0000000" + inNum) : inNum< 1000 ? ("000000"+inNum) : inNum < 10000 ? ("00000" + inNum) : inNum < 100000 ? ("0000" + inNum) : inNum < 1000000 ? ("000" + inNum) : inNum < 10000000 ? ("00" + inNum) : inNum < 100000000 ? ("0" + inNum) : (""+inNum);
//    }
    
    /**
     * 
     * 创建5长度的数字串
     * @param inNum
     * @return
     */
    public static String fill5eroNum5len(long inNum) {
        return inNum < 10 ? ("0000" + inNum ) : inNum < 100 ? ("000" + inNum) : inNum< 1000 ? ("00"+inNum) : inNum < 10000 ? ("0" + inNum) : (""+inNum);
    }
    /**
     * 
     * 创建7长度的数字串
     * @param inNum
     * @return
     */
    public static String fillZeroNum7len(long inNum) {
        return inNum < 10 ? ("000000" + inNum ) : inNum < 100 ? ("00000" + inNum) : inNum< 1000 ? ("0000"+inNum) : inNum < 10000 ? ("000" + inNum) : inNum < 100000 ? ("00" + inNum) : inNum < 1000000 ? ("0" + inNum) : (""+inNum);
    }

    /**
     * 
     * 创建7长度的数字串
     * @param inNum
     * @return
     */
    public static String fillZeroNum6len(long inNum) {
        return inNum < 10 ? ("00000" + inNum ) : inNum < 100 ? ("0000" + inNum) : inNum< 1000 ? ("000"+inNum) : inNum < 10000 ? ("00" + inNum) : inNum < 100000 ? ("0" + inNum) : (""+inNum);
    }

    /**
     * 创建3长度的数字串
     * @param inNum
     * @return
     */
    public static String fillZeroNum3len(long inNum) {
        return inNum < 10 ? ("00" + inNum) : inNum<100?("0" + inNum) : (""+inNum);
    }

    /**
     * 创建2长度的数字串
     * @param inNum
     * @return
     */
    public static String fillZeroNum2len(long inNum) {
        return inNum < 10 ? ("0" + inNum) :  (""+inNum);
    }
    
//    public static void main(String[] args) {
//        long currentTimeMillis = System.currentTimeMillis();
//        long mod1 = (currentTimeMillis%10000000)/10 ;
////        System.out.println(currentTimeMillis);
////        System.out.println(mod1);
//        
////        System.out.println(currentTimeMillis%10000);
//
//    }

}

