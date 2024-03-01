package com.wcwy.common.base.utils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * Random Util
 *
 * @author kingow
 *
 */
public final class RandomUtils {

    private RandomUtils() {

    }

    private static String[] ALLCHAR_STR = new String[]{"0","1","2","3","4","5","6","7","8","9","a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static String[] ALLCHAR = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static String[] CHAR = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    //private static final String LETTERCHAR = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    //private static final String[] NUMBERCHAR = new String[] {"0","1","2","3","4","5","6","7","8","9"};

    private static final int DEF_LENGTH = 6;

    /** 红包启动包开始比列 */
    private static final BigDecimal OPEN_HB_START = BigDecimal.valueOf(0.6);
    /** 红包启动包结束比列 */
    private static final BigDecimal OPEN_HB_END = BigDecimal.valueOf(0.7);

    /** 随机红包开始比列 */
    private static final BigDecimal HB_START = BigDecimal.valueOf(0.5);
    /** 随机红包结束比列 */
    private static final BigDecimal HB_END = BigDecimal.valueOf(1.5);



    /**
     * 生成默认长度的随机数字
     *
     * @return
     */
    public static String generateRandomNum() {
        Random rm = new Random();
        double pross = (1 + rm.nextDouble()) * Math.pow(10, DEF_LENGTH);
        String fixLenthString = String.valueOf(pross);
        return fixLenthString.substring(1, DEF_LENGTH + 1);
    }

 /*   public static String randomStr(int length) {
        if (0 >= length) {
            length = DEF_LENGTH;
        }
        Random rm = new Random();
        double pross = (1 + rm.nextDouble()) * Math.pow(10, length);
        String fixLenthString = String.valueOf(pross);
        return MD5Utils.encodeByMD5(fixLenthString).substring(0,length);
    }*/

    /**
     * 生成指定长度的随机字符串
     *
     * @param length
     * @return
     */
    public static String generateRandomNum(int length) {
        if (length == 0) {
            length = DEF_LENGTH;
        }
        Random rm = new Random();
        double pross = (1 + rm.nextDouble()) * Math.pow(10, length);
        String fixLenthString = String.valueOf(pross);
        return fixLenthString.substring(2, length + 2);
    }

    public static String generateRandomStr() {
        return generateRandom(DEF_LENGTH, ALLCHAR);
    }

    /**
     * 生成随机数
     * @param length
     * @return
     */
    public static String generateRandomStr(int length) {
        if (length == 0) {
            length = DEF_LENGTH;
        }
        return generateRandom(length, ALLCHAR);
    }

    /**
     * 生成随机数数字+大写字母
     * @param length
     * @return
     */
    public static String generateRandomStr2(int length) {
        if (length == 0) {
            length = DEF_LENGTH;
        }
        return generateRandom(length, CHAR);
    }






    /**
     * 生成默认UUID
     * @return
     */
    public static String generateDefUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成随机数字：指定数组范围内的
     * @param length
     * @param
     * @return
     */
    private static String generateRandom(int length,String[] scope) {
        StringBuilder strBuilder = new StringBuilder();
        for(int i = 0 ;i < length;i ++) {
            Random random = new Random();
            int index = random.nextInt(scope.length);
            String str = scope[index];
            strBuilder.append(str);
        }
        return strBuilder.toString();
    }

    /**
     * 生成基于startNum+随机ranNum 红包金额
     *
     * @return
     */
    public static BigDecimal getShareCashPack(Integer ranNum, Double startNum) {
        //startNum + (0~0.8)
        int value = (int) (Math.random() * ranNum);
        BigDecimal ran = BigDecimal.valueOf(value).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN);
        BigDecimal packPay = BigDecimal.valueOf(startNum).add(ran);
        return packPay;
    }

    /**
     * @Description: 随机获取两个数字之间的数字
     * @Title: random2Num
     * @param min：数字1
     * @param max：数字2
     * @return
     * @author linjia
     * @date 2020年5月26日
     */
    public static int random2Num(int min,int max){
        return (int) (Math.random() * (max - min)) + min;
    }

    /**
     * @Description: 获取两个金额之间的随机数
     * @Title: random2Num
     * @param num1:金额1
     * @param num2：金额2
     * @param ：保留几位小数
     * @return BigDecimal：金额
     * @author linjia
     * @date 2020年5月26日
     */
    public static BigDecimal random2Num(BigDecimal num1,BigDecimal num2){
        if(num1.compareTo(BigDecimal.ZERO) == 0 && num2.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        } else if(num1.compareTo(num2) == 0) {
            return num1;
        }
        int n1 = num1.multiply(BigDecimal.valueOf(100)).intValue();//乘以100，得到分
        int n2 = num2.multiply(BigDecimal.valueOf(100)).intValue();//乘以100，得到分
        int random2Num = random2Num(n1, n2);
        return BigDecimal.valueOf(random2Num).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN);
    }

    /**
     * @Description: 传入金额，取一个随机值返回
     * @Title: randomAmonut
     * @param amonut：金额
     * @return 传入金额的60%-70%
     * @author linjia
     * @date 2020年5月27日
     */
    public static BigDecimal randomAmonut(BigDecimal amonut){
        if(null == amonut || 0 >= amonut.doubleValue()){
            return BigDecimal.ZERO;
        }
        BigDecimal random2Num = random2Num(OPEN_HB_START,OPEN_HB_END);
        return amonut.multiply(random2Num).setScale(2, BigDecimal.ROUND_DOWN);
    }

    /**
     * @Description:
     * 	1、如果是最后一个红包直接返回
     * 	2、随机红包，红包金额除以红包个数，得到的金额随机0.5-1.5之间
     * @Title: randomHb
     * @param allAmonut：剩余红包金额
     * @param size：红包个数
     * @return BigDecimal：随机红包金额
     * @author linjia
     * @date 2020年5月27日
     */
    public static BigDecimal randomHb(BigDecimal allAmonut,int size){
        if(null == allAmonut || 0 >= allAmonut.doubleValue()){
            return BigDecimal.ZERO;
        }
        if(1 >= size){//最后一个红包直接返回
            return allAmonut;
        }
        BigDecimal divide = allAmonut.divide(new BigDecimal(size), 2, BigDecimal.ROUND_DOWN);//单个红包金额
        BigDecimal random2Num = random2Num(HB_START,HB_END);//随机比列
        return divide.multiply(random2Num).setScale(2, BigDecimal.ROUND_DOWN);
    }

    public static void main(String[] args) {
        System.out.println();
        Set set = new HashSet();
        for (int i=0;i<100000;i++){
            String s = generateRandomStr2(6);
            set.add(s);

        }
        System.out.println(set.size());

    }
}
