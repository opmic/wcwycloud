package com.wcwy.common.base.utils;

/**
 * ClassName: OrderState
 * Description:
 * date: 2023/4/7 8:21
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public class OrderState {

    public static String state(int i){
        if(i==1){
            return "交易中";
        }else if(i==2){
            return "交易成功";
        }else if(i==3){
            return "交易超时已关闭";
        }else if(i==4){
            return "用户已取消";
        }else if(i==5){
            return "退款中";
        }else if(i==6){
            return "已退款";
        }else if(i==7){
            return "退款异常";
        }
        return "交易中";
    }
}
