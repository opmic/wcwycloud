package com.wcwy.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.post.entity.Work;
import com.wcwy.post.service.WorkService;
import com.wcwy.post.mapper.WorkMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【work(工作经验表)】的数据库操作Service实现
* @createDate 2022-09-14 15:28:36
*/
@Service
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work>
    implements WorkService{

}




