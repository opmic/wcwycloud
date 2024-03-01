package com.wcwy.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.post.entity.TPayConfig;
import com.wcwy.post.service.TPayConfigService;
import com.wcwy.post.mapper.TPayConfigMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_pay_config(支付兑换配置表)】的数据库操作Service实现
* @createDate 2023-07-14 15:42:45
*/
@Service
public class TPayConfigServiceImpl extends ServiceImpl<TPayConfigMapper, TPayConfig>
    implements TPayConfigService{

    @Override
    public TPayConfig selectOne() {
        List<TPayConfig> list = this.list();
        return list.get(0);
    }
}




