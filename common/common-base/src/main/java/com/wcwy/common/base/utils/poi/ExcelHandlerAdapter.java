package com.wcwy.common.base.utils.poi;

/**
 * ClassName: ExcelHandlerAdapter
 * Description:
 * date: 2023/3/24 16:44
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public interface ExcelHandlerAdapter
{
    /**
     * 格式化
     *
     * @param value 单元格数据值
     * @param args excel注解args参数组
     *
     * @return 处理后的值
     */
    Object format(Object value, String[] args);
}
