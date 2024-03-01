package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.entity.Share;
import com.wcwy.company.service.ShareService;
import com.wcwy.company.mapper.ShareMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【share(分享次数记录接口)】的数据库操作Service实现
* @createDate 2023-07-20 13:51:44
*/
@Service
public class ShareServiceImpl extends ServiceImpl<ShareMapper, Share>
    implements ShareService{

}




