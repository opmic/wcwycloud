package com.wcwy.common.base.utils;

/**
 * ClassName: CalculateProportionUtil
 * Description:
 * date: 2023/9/21 16:47
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: liyue
 * @Date: 2018/8/31 15:31
 * @Description:
 */
public class CalculateProportionUtil {

    /**
     * 计算百分比 整数
     *
     * @param divisor
     * @param dividend
     * @return
     */
    public static Map<String, Double> proportionInt(Integer divisor, Integer dividend) {
        Map<String,Double> map=new HashMap<>(2);
        if (!(divisor > dividend || divisor.equals(dividend) )) {
            map.put("plusOrMinus",1d);
        } else {
            map.put("plusOrMinus",0d);
        }
        if(dividend==0){
            map.put("centage", (double) (divisor*100));
            return map;
        }
        if(divisor==0){
            map.put("centage", (double) (dividend*100));
            return map;
        }
        if (dividend == null || divisor == null)
            return null;
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) divisor / (float) dividend *100 );
        Double cc=0.0;
        if (result.indexOf(".") != -1) {
            double v =(float) divisor / (float) dividend *100 - 100;
            //String format = numberFormat.format(v);
           // result = format ;
            cc=v;
        }
      /*  String format = numberFormat.format(Double.parseDouble(result) - 100);
        result = format + "%";*/
        //result = Math.round( Double.parseDouble(result)) + "%";
        map.put("centage",(double)Math.round(cc*100)/100);
        return map;
    }

    /**
     * 计算百分比 整数
     *
     * @param divisor
     * @param dividend
     * @return
     */
    public static String proportionInt(Long divisor, Long dividend) {
        if (dividend == null || divisor == null)
            return null;
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format(divisor / dividend * 100);
        if (result.indexOf(".") != -1) {
            result = Math.round(Double.parseDouble(result)) + "%";
        }
        return result;
    }

    /**
     * 计算百分比 保留留n位小数
     *
     * @param divisor
     * @param dividend
     * @param bit
     * @return
     */
    public static String proportionDouble(Integer divisor, Integer dividend, Integer bit) {
        if (dividend == null || divisor == null || bit == null)
            return null;
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(bit);
        String result = numberFormat.format((float) divisor / (float) dividend * 100);

        return result + "%";
    }

    /**
     * 计算百分比 保留留n位小数
     *
     * @param divisor
     * @param dividend
     * @param bit
     * @return
     */
    public static String proportionDouble(Float divisor, Float dividend, Integer bit) {
        if (dividend == null || divisor == null || bit == null)
            return null;
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(bit);
        String result = numberFormat.format(divisor / dividend * 100);

        return result + "%";
    }


    /**
     * 保留n为小数
     *
     * @param d
     * @param bit
     * @return
     */
    public static Double doubleBit(Double d, Integer bit) {
        if (d == null || bit == null)
            return null;
        BigDecimal bg = new BigDecimal(d).setScale(bit, RoundingMode.DOWN);
        return bg.doubleValue();
    }


    /**
     * 保留n位小数,小数不足补0
     *
     * @param d
     * @param bit
     * @return
     */
    public static Double doubleBitWhole(Double d, Integer bit) {
        if (d == null || bit == null)
            return null;
        BigDecimal bg = new BigDecimal(d).setScale(bit, RoundingMode.DOWN);
        String dobu = bg.doubleValue() + "";
        if (dobu.indexOf(".") != -1) {
            String small = dobu.split("\\.")[1];
            for (int i = 0; i < bit - small.length(); i++) {
                dobu += "0";
            }

        }
        return Double.parseDouble(dobu);
    }

    public static void main(String[] args) {
        System.err.println(proportionInt(20L, 20L));
    }

}
