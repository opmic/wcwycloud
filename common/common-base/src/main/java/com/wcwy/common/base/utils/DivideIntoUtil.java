package com.wcwy.common.base.utils;

import com.wcwy.common.base.result.R;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: DivideIntoUtil
 * Description:
 * date: 2022/10/19 14:12
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public class DivideIntoUtil {

    /**
     * @param money：分成金额
     * @param tRecommend：邀请求职者推荐官id
     * @param sharePerson：邀请推荐官推荐官的id
     * @return Map
     * @Description:下载简历分成
     * @Author tangzhuo
     * @CreateTime 2022/10/19 13:46
     */

    public static Map<String, Object> downloadResume(BigDecimal money, String tRecommend, String sharePerson, Double recommendAPercentage , Double recommendBPercentage) {
        BigDecimal platformMoney = money;
        Map<String, Object> map = new ConcurrentHashMap();
        if (!StringUtils.isEmpty(tRecommend)) {
            if ("TR".equals(tRecommend.substring(0, 2))) {
                BigDecimal bigDecimal = money.multiply(new BigDecimal(recommendAPercentage)).setScale(2, BigDecimal.ROUND_HALF_UP);
                map.put(tRecommend, bigDecimal);
                platformMoney = platformMoney.subtract((BigDecimal) map.get(tRecommend));
                if (!StringUtils.isEmpty(sharePerson)) {
                    if ("TR".equals(sharePerson.substring(0, 2))) {
                        map.put(sharePerson, money.multiply(new BigDecimal(recommendBPercentage)).setScale(2, BigDecimal.ROUND_HALF_UP));
                        platformMoney = platformMoney.subtract((BigDecimal) map.get(sharePerson));
                        map.put(tRecommend, bigDecimal);
                    } else {
                        map.put(sharePerson, new BigDecimal(0));
                    }
                }
            } else {
                map.put(tRecommend, new BigDecimal(0));
            }

        }


        map.put("platformMoney", platformMoney);
        return map;
    }


    /**
     * 职位分成
     * @param money
     * @param tRecommendA
     * @param tRecommendB
     * @param recommendAPercentage
     * @param recommendBPercentage
     * @return
     */

    public static Map<String, BigDecimal> postDivideInto(BigDecimal money, String tRecommendA, String tRecommendB, Double recommendAPercentage , Double recommendBPercentage) {
        Map<String, BigDecimal> map = new ConcurrentHashMap();

        if (!StringUtils.isEmpty(tRecommendA)) {
            String substring = tRecommendA.substring(0, 2);//传入值是推荐官
            if ("TR".equals(substring)) {
                map.put(tRecommendA, money.multiply(new BigDecimal(recommendAPercentage)).setScale(2, BigDecimal.ROUND_HALF_UP));
            } else {
                map.put(tRecommendA, new BigDecimal(0));
            }
        }
        if (!StringUtils.isEmpty(tRecommendB)) {
            if ("TR".equals(tRecommendB.substring(0, 2))) {
                map.put(tRecommendB, money.multiply(new BigDecimal(recommendBPercentage)).setScale(2, BigDecimal.ROUND_HALF_UP));
            } else {
                map.put(tRecommendB, new BigDecimal(0));
            }

        }
        return map;
    }



    /**
     * @param money：总无忧币
     * @param tRecommend：推荐官
     * @param sharePerson：分享人
     * @return null
     * @Description: 赏金及保证经
     * @Author tangzhuo
     * @CreateTime 2022/10/20 15:51
     */

    public static Map<String, Object> earnestMoney(BigDecimal money, String tRecommend, String sharePerson) {
        BigDecimal platformMoney = money;
        Map<String, Object> map = new ConcurrentHashMap();
        if (!StringUtils.isEmpty(sharePerson)) {
            if ("TR".equals(sharePerson.substring(0, 2))) {
                map.put(sharePerson, money.multiply(new BigDecimal(0.1)).setScale(2, BigDecimal.ROUND_HALF_UP));
                platformMoney = platformMoney.subtract((BigDecimal) map.get(sharePerson));
            } else {
                map.put(sharePerson, new BigDecimal(0));
            }

        }
        if (!StringUtils.isEmpty(tRecommend)) {
            if ("TR".equals(tRecommend.substring(0, 2))) {
                map.put(tRecommend, money.multiply(new BigDecimal(0.5)).setScale(2, BigDecimal.ROUND_HALF_UP));
                platformMoney = platformMoney.subtract((BigDecimal) map.get(tRecommend));
            } else {
                map.put(tRecommend, new BigDecimal(0));
            }

        }
        map.put("platformMoney", platformMoney);
        return map;
    }


    /**
     * @param money：总无忧币
     * @param tRecommend：投简人
     * @param sharePerson：邀请企业人
     * @return null
     * @Description: 赏金岗位分成
     * @Author tangzhuo
     * @CreateTime 2022/11/9 17:20
     */

    public static Map<String, BigDecimal> moneyReward(BigDecimal money, String tRecommend, String sharePerson) {
        BigDecimal platformMoney = money;
        Map<String, BigDecimal> map = new ConcurrentHashMap();

        if (!StringUtils.isEmpty(sharePerson)) {
            String substring = sharePerson.substring(0, 2);//传入值是推荐官
            if ("TR".equals(substring)) {
                map.put(sharePerson, money.multiply(new BigDecimal(0.1)).setScale(2, BigDecimal.ROUND_HALF_UP));
                platformMoney = platformMoney.subtract((BigDecimal) map.get(sharePerson));
            } else {
                map.put(sharePerson, new BigDecimal(0));
            }
        }
        if (!StringUtils.isEmpty(tRecommend)) {
            if ("TR".equals(tRecommend.substring(0, 2))) {
                map.put(tRecommend, money.multiply(new BigDecimal(0.5)).setScale(2, BigDecimal.ROUND_HALF_UP));
                platformMoney = platformMoney.subtract((BigDecimal) map.get(tRecommend));
            } else {
                map.put(tRecommend, new BigDecimal(0));
            }

        }
        map.put("platformMoney", platformMoney);
        return map;
    }


    /**
     * @param salary：薪资
     * @param tRecommend：投简人
     * @param sharePerson：邀请企业人
     * @param percentage：佣金率
     * @param workday：工作日
     * @return null
     * @Description: 猎头岗位
     * @Author tangzhuo
     * @CreateTime 2022/11/10 8:12
     */

    public static List<Map> headhuntingFee(BigDecimal salary, String tRecommend, String sharePerson, Integer percentage, Integer workday) {

        List<Map> list = new ArrayList();

        //计算佣金率
        BigDecimal commissionRate = new BigDecimal(percentage).multiply(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_HALF_UP);
        //计算佣金率之后的钱
        BigDecimal bigDecimal = salary.multiply(commissionRate).setScale(2, BigDecimal.ROUND_HALF_UP);
        LocalDate localDate = LocalDate.now();
        BigDecimal bigDecimal3 = new BigDecimal(1);

        if (workday == 90) {
            for (int i = 1; i < 4; i++) {

                Map<String, Object> map = new ConcurrentHashMap();
                if (i == 3) {
                    //分每个月保证期的钱
                    BigDecimal bigDecimal2 = bigDecimal.multiply(bigDecimal3).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal platformMoney = bigDecimal2;
                    if (!StringUtils.isEmpty(sharePerson)) {
                        if ("TR".equals(sharePerson.substring(0, 2))) {
                            map.put(sharePerson, bigDecimal2.multiply(new BigDecimal(0.1)).setScale(2, BigDecimal.ROUND_HALF_UP));
                            platformMoney = platformMoney.subtract((BigDecimal) map.get(sharePerson));
                        } else {
                            map.put(sharePerson, new BigDecimal(0));
                        }

                    }
                    if (!StringUtils.isEmpty(tRecommend)) {
                        if ("TR".equals(tRecommend.substring(0, 2))) {
                            map.put(tRecommend, bigDecimal2.multiply(new BigDecimal(0.5)).setScale(2, BigDecimal.ROUND_HALF_UP));
                            platformMoney = platformMoney.subtract((BigDecimal) map.get(tRecommend));
                        } else {
                            map.put(tRecommend, new BigDecimal(0));
                        }

                    }
                    map.put("platformMoney", platformMoney);
                    map.put("noPaymentTime", localDate.plusDays(30 * i));
                    map.put("money", bigDecimal2);
                    list.add(map);
                    map = null;
                } else {
                    BigDecimal bigDecimal4 = new BigDecimal(0.1);
                    //分每个月保证期的钱
                    BigDecimal bigDecimal21 = bigDecimal.multiply(bigDecimal4).setScale(2, BigDecimal.ROUND_HALF_UP);
                    bigDecimal3 = bigDecimal3.subtract(bigDecimal4);//减去10%
                    BigDecimal platformMoney = bigDecimal21;
                    if (!StringUtils.isEmpty(sharePerson)) {
                        if ("TR".equals(sharePerson.substring(0, 2))) {
                            map.put(sharePerson, bigDecimal21.multiply(new BigDecimal(0.1)).setScale(2, BigDecimal.ROUND_HALF_UP));
                            platformMoney = platformMoney.subtract((BigDecimal) map.get(sharePerson));
                        } else {
                            map.put(sharePerson, new BigDecimal(0));
                        }
                    }
                    if (!StringUtils.isEmpty(tRecommend)) {
                        if ("TR".equals(tRecommend.substring(0, 2))) {
                            map.put(tRecommend, bigDecimal21.multiply(new BigDecimal(0.5)).setScale(2, BigDecimal.ROUND_HALF_UP));
                            platformMoney = platformMoney.subtract((BigDecimal) map.get(tRecommend));
                        } else {
                            map.put(tRecommend, new BigDecimal(0));
                        }

                    }
                    map.put("platformMoney", platformMoney);
                    map.put("noPaymentTime", localDate.plusDays(30 * i));
                    map.put("money", bigDecimal21);
                    list.add(map);
                    map = null;
                }

            }

        } else if (workday == 180) {
            for (int i = 1; i < 7; i++) {

                Map<String, Object> map = new ConcurrentHashMap();
                if (i == 6) {
                    //分每个月保证期的钱
                    BigDecimal bigDecimal2 = bigDecimal.multiply(bigDecimal3).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal platformMoney = bigDecimal2;
                    if (!StringUtils.isEmpty(sharePerson)) {
                        if ("TR".equals(sharePerson.substring(0, 2))) {
                            map.put(sharePerson, bigDecimal2.multiply(new BigDecimal(0.1)).setScale(2, BigDecimal.ROUND_HALF_UP));
                            platformMoney = platformMoney.subtract((BigDecimal) map.get(sharePerson));
                        } else {
                            map.put(sharePerson, new BigDecimal(0));
                        }


                    }
                    if (!StringUtils.isEmpty(tRecommend)) {
                        if(  "TR".equals(tRecommend.substring(0, 2))){
                            map.put(tRecommend, bigDecimal2.multiply(new BigDecimal(0.5)).setScale(2, BigDecimal.ROUND_HALF_UP));
                            platformMoney = platformMoney.subtract((BigDecimal) map.get(tRecommend));
                        } else {
                            map.put(tRecommend, new BigDecimal(0));
                        }

                    }
                    map.put("platformMoney", platformMoney);
                    map.put("noPaymentTime", localDate.plusDays(30 * i));
                    map.put("money", bigDecimal2);
                    list.add(map);
                    map = null;
                } else {
                    BigDecimal bigDecimal4 = new BigDecimal(0.1);
                    //分每个月保证期的钱
                    BigDecimal bigDecimal21 = bigDecimal.multiply(bigDecimal4).setScale(2, BigDecimal.ROUND_HALF_UP);
                    bigDecimal3 = bigDecimal3.subtract(bigDecimal4);//减去10%
                    BigDecimal platformMoney = bigDecimal21;
                    if (!StringUtils.isEmpty(sharePerson)) {
                        if( "TR".equals(sharePerson.substring(0, 2))){
                            map.put(sharePerson, bigDecimal21.multiply(new BigDecimal(0.1)).setScale(2, BigDecimal.ROUND_HALF_UP));
                            platformMoney = platformMoney.subtract((BigDecimal) map.get(sharePerson));
                        }else {
                            map.put(sharePerson, new BigDecimal(0));
                        }
                    }
                    if (!StringUtils.isEmpty(tRecommend) ) {
                        if( "TR".equals(tRecommend.substring(0, 2))){
                            map.put(tRecommend, bigDecimal21.multiply(new BigDecimal(0.5)).setScale(2, BigDecimal.ROUND_HALF_UP));
                            platformMoney = platformMoney.subtract((BigDecimal) map.get(tRecommend));
                        }else {
                            map.put(tRecommend, new BigDecimal(0));
                        }
                    }
                    map.put("platformMoney", platformMoney);
                    map.put("noPaymentTime", localDate.plusDays(30 * i));
                    map.put("money", bigDecimal21);
                    list.add(map);
                    map = null;
                }

            }

        }
        return list;


    }

    /**
     *
     * @param money 金额
     * @param tRecommend 邀请求职者人推荐官
     * @param sharePerson 邀请企业人推荐官
     * @param month 月份
     * @return
     */
    public static Map headhuntingFee1(BigDecimal money, String tRecommend, String sharePerson, Integer month) {
        Map<String,Object> map=new HashMap<>();
        //分每个月保证期的钱
        LocalDate localDate = LocalDate.now();

        BigDecimal platformMoney = money;
        if (!StringUtils.isEmpty(sharePerson)) {
            if ("TR".equals(sharePerson.substring(0, 2))) {
                map.put(sharePerson, money.multiply(new BigDecimal(0.1)).setScale(2, BigDecimal.ROUND_HALF_UP));
                platformMoney = platformMoney.subtract((BigDecimal) map.get(sharePerson));
            } else {
                map.put(sharePerson, new BigDecimal(0));
            }

        }
        if (!StringUtils.isEmpty(tRecommend)) {
            if ("TR".equals(tRecommend.substring(0, 2))) {
                map.put(tRecommend, money.multiply(new BigDecimal(0.5)).setScale(2, BigDecimal.ROUND_HALF_UP));
                platformMoney = platformMoney.subtract((BigDecimal) map.get(tRecommend));
            } else {
                map.put(tRecommend, new BigDecimal(0));
            }

        }
        map.put("platformMoney", platformMoney);
        map.put("noPaymentTime", localDate.plusDays(30 * month));
        map.put("money", money);
        return map;
    }


/*    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();           // 2022-06-04
        LocalDate localDate1 = localDate.plusDays(1);    // 2022-06-05
        LocalDate localDate2 = localDate.plusDays(30);    // 2022-06-06
        System.out.println(localDate1);
        System.out.println(localDate2);
        BigDecimal num1 = new BigDecimal(100);
        BigDecimal subNum1 = num1.multiply(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println(subNum1);
    }*/
    /**
     * @Description: 计算年薪佣金
     * @param startingSalary:开始年薪 finalSalary：结束年薪 commissionRate 佣金率
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/5/8 14:46
     */

    public static String brokerage(BigDecimal startingSalary,BigDecimal finalSalary, int commissionRate, int acquire ) {

        DecimalFormat df = new DecimalFormat("0.00");
        startingSalary= startingSalary.multiply(new BigDecimal(10000)).divide(new BigDecimal(12), 3, BigDecimal.ROUND_HALF_UP);
        finalSalary=finalSalary.multiply(new BigDecimal(10000)).divide(new BigDecimal(12), 3, BigDecimal.ROUND_HALF_UP);

        BigDecimal recommendAPercentage = new BigDecimal(Double.valueOf(df.format( (float)commissionRate / 100))).setScale(2, BigDecimal.ROUND_HALF_UP);//获取企业需要花费的金额
        BigDecimal multiply = startingSalary.multiply(recommendAPercentage).setScale(0, BigDecimal.ROUND_HALF_UP);
        BigDecimal multiply1 = finalSalary.multiply(recommendAPercentage).setScale(0, BigDecimal.ROUND_HALF_UP);
        Double aDouble = Double.valueOf(df.format((float)acquire / 100));//获取企业需要花费的金额
        BigDecimal multiply2 = multiply.multiply(new BigDecimal(aDouble)).setScale(0, BigDecimal.ROUND_HALF_UP);
        BigDecimal multiply3 = multiply1.multiply(new BigDecimal(aDouble)).setScale(0, BigDecimal.ROUND_HALF_UP);

        return multiply2.toPlainString()+"-"+multiply3.toPlainString();
    }

    /**
     * 简历付校园
     * @param beginMoney 开始
     * @param engMoney 结束
     * @param commissionRate  佣金比
     * @return
     */
    public static  String campusRecruitment( BigDecimal beginMoney,BigDecimal engMoney,int commissionRate){
        DecimalFormat df = new DecimalFormat("0.00");
        BigDecimal bigDecimal = new BigDecimal(Double.valueOf(df.format((float)commissionRate / 100)));//获取企业需要花费的金额
        BigDecimal multiply = beginMoney.multiply(bigDecimal).setScale(0, BigDecimal.ROUND_HALF_UP);
        if(engMoney !=null){
            BigDecimal multiply1 = engMoney.multiply(bigDecimal).setScale(0, BigDecimal.ROUND_HALF_UP);
            return multiply.toPlainString()+"-"+multiply1.toPlainString();
        }
        return multiply.toPlainString()+"";
    }
    public static  String jobMarket( BigDecimal beginMoney,BigDecimal engMoney,int commissionRate,BigDecimal gradeA,BigDecimal gradeB,BigDecimal gradeC){

        DecimalFormat df = new DecimalFormat("0.00");
        BigDecimal bigDecimal = new BigDecimal(Double.valueOf(df.format((float)commissionRate / 100)));//获取企业需要花费的金额
        BigDecimal multiply = gradeA.multiply(bigDecimal).setScale(0, BigDecimal.ROUND_HALF_UP);
        BigDecimal multiply1 = gradeB.multiply(bigDecimal).setScale(0, BigDecimal.ROUND_HALF_UP);
        BigDecimal multiply2 = gradeC.multiply(bigDecimal).setScale(0, BigDecimal.ROUND_HALF_UP);

        BigDecimal multiply3=null;
        BigDecimal multiply4=null;
        int A = beginMoney.compareTo(new BigDecimal(30));
        int B = beginMoney.compareTo(new BigDecimal(50));

        int C = engMoney.compareTo(new BigDecimal(30));
        int D = engMoney.compareTo(new BigDecimal(50));

        if(beginMoney.compareTo(engMoney)==0){
            if(A==-1){
                return multiply.toPlainString();
            }else if(A==0){
                return multiply1.toPlainString();
            }else if(A==1 || B==-1){
                return multiply1.toPlainString();
            }else if(A==1 || B==0){
                return multiply1.toPlainString();
            }else if( B==1){
                return multiply2.toPlainString();
            }
        }

        if(A==1 && D==-1){
            return multiply1.toPlainString();
        }else if(A==1 && D==0){
            return multiply1.toPlainString();
        }else if(A==0 && D==0){
            return multiply1.toPlainString();
        }

        if(A==-1){
            multiply3=multiply;
        }else if(A==0){
            multiply3=multiply1;
        }else if(A==1 && B==-1){
            multiply3=multiply1;
        }else if(B==0){
            multiply3=multiply1;
        }else if(B==1){
            return multiply2.toPlainString();
        }
        if( C==-1){
          return   multiply3.toPlainString();
        }else if(C==0){
            return   multiply3.toPlainString();
        }else if(C==1 && D==-1){
            multiply4=multiply1;
            return   multiply3.toPlainString()+"-"+multiply4.toPlainString();
        }else if(D==0){
            multiply4=multiply1;
            return   multiply3.toPlainString()+"-"+multiply4.toPlainString();
        }else if(D==1){
            return multiply3.toPlainString()+"-"+multiply2.toPlainString();
        }
        return "0";
    }
}
