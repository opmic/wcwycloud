package com.wcwy.common.base.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * ClassName: CardUtil
 * Description:
 * date: 2022/9/8 11:19
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public class CardUtil {
    /**
     * 权重位
     */
    private static int[] w = {7, 9, 10, 5, 8, 4, 2, 1, 6,
            3, 7, 9, 10, 5, 8, 4, 2};


    /**
     * 校验码字符数组
     */
    private static char[] verifyCode = {'1','0','X','9','8','7','6','5','4','3','2'};

    /**
     * 判断是否为合法身份证号
     * @param idCard
     * @return
     */
    public static boolean isCard(String idCard) {


        // 1. 如果为空或位数不对则
        if(StringUtils.isBlank(idCard) || ( idCard.length() != 15 && idCard.length() != 18)){
            return false;
        }


        // 2. 如果是15位身份证号转成18位
        if( idCard.length() == 15){
            try {
                idCard = getEighteenIdCard(idCard);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }


        // 3. 校验身份证号
        char[] c = idCard.toCharArray();
        int sum = 0;
        for (int i = 0; i < w.length; i++) {
            sum += (c[i] - '0') * w[i];
        }
        char[] verifyCode = "10X98765432".toCharArray();
        char ch = verifyCode[sum % 11];
        return c[17] == ch;
    }


    /**
     * 15位身份证 转18位
     * @param fifteenIdCard
     * @return
     * @throws Exception
     */
    public static String getEighteenIdCard(String fifteenIdCard) throws Exception {


        // 1. 判断是否为15位身份证号
        if (fifteenIdCard != null && fifteenIdCard.length() == 15) {


            // 2. 拼接成18位身份证号
            StringBuilder eighteenIdCard = new StringBuilder();
            eighteenIdCard
                    .append(fifteenIdCard.substring(0,6))
                    .append("19")
                    .append(fifteenIdCard.substring(6))
                    .append(getVerifyCode(eighteenIdCard.toString()));
            return eighteenIdCard.toString();
        } else {
            throw new Exception("不是15位的身份证");
        }
    }


    /**
     *  获取身份证的校验码
     *  lsp  2017年1月19日13:47:52
     * @param idCardNumber
     * @return
     * @throws Exception
     */
    public static char getVerifyCode(String idCardNumber) throws Exception {

        // 1. 非空和小于17位判断
        if (StringUtils.isBlank(idCardNumber) || idCardNumber.length() == 15) {
            throw new Exception("不合法的身份证号码");
        }


        // 2. 生成校验码
        char[] Ai = idCardNumber.toCharArray();
        int S = 0;
        int Y;
        for (int i = 0; i < w.length; i++) {
            S += (Ai[i] - '0') * w[i];
        }
        Y = S % 11;
        return verifyCode[Y];
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(isCard("220182190001018888c"));
    }
}
