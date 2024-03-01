package com.wcwy.post.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {
    /**
     * 未支付
     */
    NOTPAY("未支付", 1),


    /**
     * 支付成功
     */
    SUCCESS("支付成功", 2),

    /**
     * 已关闭
     */
    CLOSED("超时已关闭", 3),

    /**
     * 已取消
     */
    CANCEL("用户已取消", 4),

    /**
     * 退款中
     */
    REFUND_PROCESSING("退款中", 5),

    /**
     * 已退款
     */
    REFUND_SUCCESS("已退款", 6),

    /**
     * 退款异常
     */
    REFUND_ABNORMAL("退款异常", 7),
    ERROR_CANCEL("异常取消", 8);
    /**
     * 类型
     */
    private final String type;
    private final Integer state;

    public static String getType(Integer state) {
        if (state.equals(OrderStatus.NOTPAY.state)) {
            return OrderStatus.NOTPAY.type;
        }
        if (state.equals(OrderStatus.SUCCESS.state)) {
            return OrderStatus.SUCCESS.type;
        }
        if (state.equals(OrderStatus.CLOSED.state)) {
            return OrderStatus.CLOSED.type;
        }
        if (state.equals(OrderStatus.CANCEL.state)) {
            return OrderStatus.CANCEL.type;
        }
        if(state.equals(OrderStatus.REFUND_PROCESSING.state)){
            return OrderStatus.REFUND_PROCESSING.type;
        }
        if(state.equals(OrderStatus.REFUND_SUCCESS.state)){
            return OrderStatus.REFUND_SUCCESS.type;
        }
        if(state.equals(OrderStatus.REFUND_ABNORMAL.state)){
            return OrderStatus.REFUND_ABNORMAL.type;
        }
        if(state.equals(OrderStatus.ERROR_CANCEL.state)){
            return OrderStatus.ERROR_CANCEL.type;
        }
        return "未知状态!";

    }

}
