package com.wcwy.common.base.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcwy.common.base.query.handler.WhereEntityHandler;

import java.io.Serializable;

/**
 * @author lmabbe
 */
public class BaseQuery implements Serializable {

    /**
     * 创建查询Wrapper
     *
     * @return
     */
    public QueryWrapper createQuery() {
        return WhereEntityHandler.invoke(this);
    }
}
