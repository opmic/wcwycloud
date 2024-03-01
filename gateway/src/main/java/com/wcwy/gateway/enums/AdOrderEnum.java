package com.wcwy.gateway.enums;

/**
 * @Description: 广告订单相关枚举
 * @Author: duanchao
 * @Date: 2022/6/28 16:06
 */
public interface AdOrderEnum {

    enum OrderType {
        DY(1, "抖音"),
        PYQ(2, "朋友圈"),
        MS(3, "短信"),
        L(4, "附近推")
        ;

        private Integer code;
        private String name;

        OrderType(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }


    enum AdStatus {
        N(1, "待投放"),
        P(2, "投放中"),
        A(3, "投放完成"),
        ST(4, "暂停投放"),
        QX(5, "取消投放"),
        W(6, "待审核"),
        AI(7, "审核中"),
        F(8, "审核通过"),
        AN(9, "审核失败"),
        CI(10,"创建中"),
        CF(11,"创建失败")
        ;

        private Integer code;
        private String name;

        AdStatus(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    enum AuditStatus {
        W(1, "待审核"),
        P(2, "审核中"),
        F(3, "审核通过"),
        N(4, "审核失败")
        ;

        private Integer code;
        private String name;

        AuditStatus(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }


    enum CreateStatus {
        W(1, "待创建"),
        S(2, "创建成功"),
        F(3, "创建失败")
        ;

        private Integer code;
        private String name;

        CreateStatus(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

}
