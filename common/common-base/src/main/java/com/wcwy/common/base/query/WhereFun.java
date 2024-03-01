package com.wcwy.common.base.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public interface WhereFun {
    void whereFunc(QueryWrapper wrapper, String field, Object vaule);
}