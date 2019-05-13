package com.sdhoo.pdloan.payctr;

/**
 * 支付中心常量
 * 
 * @author SDPC_LIU
 *
 */
public class PaycenterConstants {

    /**
     * 服务实现类的Bean名称前缀.
     */
    public static final String USR_PAYCHNL_SERVICENAME_PRE = "usrPayChnlServiceImpl_";
    

    /**
     * 系统配置的默认绑定代扣,当接口遇到该请求时,将请求通道转换为具体绑定的代扣通道
     */
    public static final int USERPAY_CHNL_DFT_BINDDK = 1 ;


    /**
     * 支付渠道ID,新浪绑定代扣(7)
     */
    public static final int USERPAY_CHNL_SINABINDDK = 7;

    /**
     * 支付渠道ID,线下支付(8)
     */
    public static final int USERPAY_CHNL_OFLINE = 8;

//    /**
//     * 支付渠道ID,宝付银行卡代扣(9)
//     */
//    public static final int USERPAY_CHNL_BAOFU = 9;

    /**
     * 支付渠道ID,微信支付渠道(12)
     */
    public static final int USERPAY_CHNL_WEIXIN = 11;

    /**
     * 支付渠道ID,支付宝支付渠道(13)
     */
    public static final int USERPAY_CHNL_ALIPAY = 13;

    /**
     * 支付渠道ID,易宝代扣渠道(15)
     */
    public static final int USERPAY_CHNL_YIBAOBINDDK = 15;

    
    /**
     * 支付渠道ID,富友绑卡代扣渠道(17)
     */
    public static final int USERPAY_CHNL_FUIOUBINDDK = 17;


    /**
     * 用户支付渠道交易类型,1银行卡代扣
     */
    public static final int USERPAY_IFC_TRADETYPE_DUDC= 1 ;

    /**
     * 用户支付渠道交易类型,2公众号支付
     */
    public static final int USERPAY_IFC_TRADETYPE_JSAPI = 2 ;
    
    /**
     * 用户支付渠道交易类型,3本地扫码支付
     */
    public static final int USERPAY_IFC_TRADETYPE_NATIVE = 3 ;
    
    /**
     * 用户支付渠道交易类型,4商户APP支付
     */
    public static final int USERPAY_IFC_TRADETYPE_APP = 4 ;

    /**
     * 用户支付渠道交易类型,5手机网页支付
     */
    public static final int USERPAY_IFC_TRADETYPE_MWEB = 5 ;
    
    /**
     * 用户支付渠道交易类型,6(PC)网页支付
     */
    public static final int USERPAY_IFC_TRADETYPE_PCWEB = 6 ;
    
    /**
     * 用户支付渠道交易类型,8线下支付
     */
    public static final int USERPAY_IFC_TRADETYPE_OFLINE = 8 ;


    /**
     * 服务实现类的Bean名称前缀.
     */
    public static final String SYSPAY_CHNLSERVICENAME_PRE = "sysPayChnlServiceImpl_";


    /**
     * 代付渠道(宝付代付)
     */
    public static final int SYSPAY_CHNL_BAOFUDF = 101;  
    
    /**
     * 代付渠道(易宝代付)
     */
    public static final int SYSPAY_CHNL_YIBAODF = 103;  

    /**
     * 代付渠道(富友代付)
     */
    public static final int SYSPAY_CHNL_FUIOU = 107;  
    
    /**
     * 代付渠道(线下放款)
     */
    public static final int SYSPAY_CHNL_OFLINE = 108;
    /**
     * 代付渠道(展期放款)
     */
    public static final int SYSPAY_CHNL_EXTENSION = 109;


    /**
     * 正则表达式, 验证Long类型的字符串
     */
    public static final String REGEXSTR_LONG = "^-?\\d+$";
    
    
    /**
     * 配置KEY前缀
     */
    public static final String CONFIG_KEY_PRE = "";

    
    /**
     * 默认渠道ID配置KEY
     */
    public static final String CFGKEY_USERDUDC_DFT_PAYCHNL_ID = CONFIG_KEY_PRE+ "USERDUDC_DFT_PAYCHNL_ID";

    /**
     * 易宝默认渠道配置ID配置KEY
     */
    public static final String CFGKEY_USERDUDC_YIBAO_DFT_PAYCHNL_CFGID = CONFIG_KEY_PRE + "USERDUDC_YIBAO_DFT_PAYCHNL_CFGID";
    
    /**
     * 富友默认渠道配置ID配置KEY
     */
    public static final String CFGKEY_USERDUDC_FUIOU_DFT_PAYCHNL_CFGID = CONFIG_KEY_PRE + "USERDUDC_FUIOU_DFT_PAYCHNL_CFGID";

    /**
     * 支付宝渠道配置ID配置KEY
     */
    public static final String CFGKEY_USERPAY_ALIPAY_DFT_PAYCHNL_CFGID = CONFIG_KEY_PRE + "USERPAY_ALIPAY_DFT_PAYCHNL_CFGID";
    
    /**
     * 线下支付渠道配置ID配置KEY
     */
    public static final String CFGKEY_USERPAY_OFLINE_DFT_PAYCHNL_CFGID = CONFIG_KEY_PRE + "USERPAY_OFLINE_DFT_PAYCHNL_CFGID";

    
    
    
    /**
     * 默认渠道ID配置KEY
     */
    public static final String CFGKEY_SYSPAY_DFT_PAYCHNL_ID =  CONFIG_KEY_PRE+ "SYSPAY_DFT_PAYCHNL_ID";

    /**
     * 易宝默认渠道配置ID配置KEY
     */
    public static final String CFGKEY_SYSPAY_YIBAO_DFT_PAYCHNL_CFGID = CONFIG_KEY_PRE+  "SYSPAY_YIBAO_DFT_PAYCHNL_CFGID";
    
    /**
     * 富友默认渠道配置ID配置KEY
     */
    public static final String CFGKEY_SYSPAY_FUIOU_DFT_PAYCHNL_CFGID = CONFIG_KEY_PRE+  "SYSPAY_FUIOU_DFT_PAYCHNL_CFGID";
    
    /**
     * 线下放款默认配置ID配置KEY
     */
    public static final String CFGKEY_SYSPAY_OFLINE_DFT_PAYCHNL_CFGID = CONFIG_KEY_PRE+  "SYSPAY_OFLINE_DFT_PAYCHNL_CFGID";
    

}
