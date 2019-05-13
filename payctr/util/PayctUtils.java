package com.sdhoo.pdloan.payctr.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.sdhoo.pdloan.payctr.base.dto.PctIdData;

public class PayctUtils {

    /**
     * 格式化
     */
    private static SimpleDateFormat orderdCodeDateSdfmt = new SimpleDateFormat("YYMMdd");

    /**
     * 创建一个交易编号,规则: 年日(YYmmdd,6位) + 一天之内的秒数(5位) + 秒内的毫秒数(3位)  + 序号位(2位)
     * @return
     */
    public static synchronized PctIdData genPctIdData() {
     // 订单编号,
        Calendar currDateCld = Calendar.getInstance();
        Date inDate = currDateCld.getTime();
        String yeardayPre = orderdCodeDateSdfmt.format(inDate); 
        // 天内秒数
        int hourOfday = currDateCld.get(Calendar.HOUR_OF_DAY);
        // 分钟数
        int minute = currDateCld.get(Calendar.MINUTE);
        // 秒数
        int secd = currDateCld.get(Calendar.SECOND);
        // 毫秒数
        int milsecd = currDateCld.get(Calendar.MILLISECOND);
        // 一天中的秒数
        int daySecs = (hourOfday*3600)+(minute*60)+(secd);
        // 序号暂为随机
        int sortNum = Double.valueOf((new Random().nextDouble()*100)).intValue(); 
        String rtVal = yeardayPre + fillZeroNum5len(daySecs) + fillZeroNum3len(milsecd) + fillZeroNum2len(sortNum);
        
        PctIdData rtData = new PctIdData(Long.valueOf(rtVal),inDate);
        
        return rtData ;
    }

    /**
     * 创建3长度的数字串
     * @param inNum
     * @return
     */
    public static String fillZeroNum2len(long inNum) {
        return inNum < 10 ? ("0" + inNum) : (""+inNum);
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
     * 创建5长度的数字串
     * @param inNum
     * @return
     */
    public static String fillZeroNum5len(long inNum) {
        return inNum < 10 ? ("0000" + inNum ) : inNum < 100 ? ("000" + inNum) : inNum< 1000 ? ("00"+inNum) : inNum < 10000 ? ("0" + inNum) : (""+inNum);
    }
    
    
    public static String getNumberFromStr(String inStr) {
        StringBuffer rtStr = new StringBuffer() ;
        
        if(inStr != null &&  !"".equals(inStr)) {
            for(int i=0; i< inStr.length() ; i++ ) {
                char charStr = inStr.charAt(i);
                if(charStr >= 48 && charStr <= 57 ) {
                    rtStr.append(charStr);
                }
            }
        }
        return rtStr.toString();
    }
    
    
//    public static void main(String[] args) {
//        String randStr = "dftyuionj56789bjkty789n78";
//        String numberFromStr = getNumberFromStr(randStr);
//        System.err.println(numberFromStr);
//    }
    
    
}
