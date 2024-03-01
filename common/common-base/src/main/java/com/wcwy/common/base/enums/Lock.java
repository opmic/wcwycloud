package com.wcwy.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: Lock
 * Description:
 * date: 2022/9/28 9:14
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
public enum Lock {
    INSERT_TC("insert_tc"),//注册企业锁
    DEDUCT_EXPENSES("deductExpenses"),//扣除费用锁
    INSERT_PT("insert_pt");//添加岗位

    private String lockSion;
}
