package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.entity.WechatUser;
import com.wcwy.company.service.WechatUserService;
import com.wcwy.company.mapper.WechatUserMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【wechat_user(用户微信信息表)】的数据库操作Service实现
* @createDate 2022-09-26 09:11:27
*/
@Service
public class WechatUserServiceImpl extends ServiceImpl<WechatUserMapper, WechatUser>
    implements WechatUserService{

}




