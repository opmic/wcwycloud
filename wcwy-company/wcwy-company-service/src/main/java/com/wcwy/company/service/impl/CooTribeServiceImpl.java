package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.dto.CooTribeDTO;
import com.wcwy.company.entity.CooTribe;
import com.wcwy.company.query.CooTribeQuery;
import com.wcwy.company.service.CooTribeService;
import com.wcwy.company.mapper.CooTribeMapper;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.CooTribeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
* @author Administrator
* @description 针对表【coo_tribe】的数据库操作Service实现
* @createDate 2024-01-19 11:18:16
*/
@Service
public class CooTribeServiceImpl extends ServiceImpl<CooTribeMapper, CooTribe>
    implements CooTribeService{

    @Autowired
    private CompanyMetadata companyMetadata;

    @Autowired
    private CooTribeMapper cooTribeMapper;
    @Override
    public boolean add(CooTribeVO cooTribeVO) {
        CooTribe cooTribe=new CooTribe();
        BeanUtils.copyProperties(cooTribeVO,cooTribe);
        cooTribe.setAudit(1);
        cooTribe.setUserId(companyMetadata.userid());
        boolean save = this.save(cooTribe);
        return save;
    }

    @Override
    public Boolean del(Long id) {
        LambdaQueryWrapper<CooTribe> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(CooTribe::getId,id);
        lambdaQueryWrapper.eq(CooTribe::getUserId,companyMetadata.userid());
        boolean remove = this.remove(lambdaQueryWrapper);
        return remove;
    }

    @Override
    public IPage<CooTribe> getPage(CooTribeQuery cooTribeQuery) {
        LambdaQueryWrapper<CooTribe> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(CooTribe::getOnLine,0);
       if(cooTribeQuery.getType() !=null){
           lambdaQueryWrapper.eq(CooTribe::getType,cooTribeQuery.getType());
       }
        if(! StringUtils.isEmpty(cooTribeQuery.getTitle())){
            lambdaQueryWrapper.like(CooTribe::getTitle,cooTribeQuery.getTitle());
        }
        lambdaQueryWrapper.ne(CooTribe::getType,3);
        lambdaQueryWrapper.orderByDesc(CooTribe::getCreateTime);

        IPage<CooTribe> page = this.page(cooTribeQuery.createPage(),lambdaQueryWrapper);
        return page;
    }

    @Override
    public IPage<CooTribeDTO> pageAnswer(CooTribeQuery cooTribeQuery) {
        return cooTribeMapper.pageAnswer(cooTribeQuery.createPage(),cooTribeQuery.getId());
    }

    @Override
    public Boolean isReply(Long id) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_id",companyMetadata.userid());
        queryWrapper.eq("father",id);
        int count = this.count(queryWrapper);
        if(count>0){
            return true;
        }
        return false ;
    }

    @Override
    public IPage<CooTribe> selectType(CooTribeQuery cooTribeQuery, String userid) {

        return cooTribeMapper.selectType(cooTribeQuery.createPage(),cooTribeQuery,userid);
    }

    @Override
    public IPage<CooTribeDTO> getPageDTO(CooTribeQuery cooTribeQuery) {
        return cooTribeMapper.getPageDTO(cooTribeQuery.createPage(),cooTribeQuery);
    }

    @Override
    public CooTribeDTO getAnswer(Long id, String userId) {
        return cooTribeMapper.getAnswer(id,userId);
    }

    @Override
    public IPage<CooTribeDTO> inquirePage(CooTribeQuery cooTribeQuery) {
        return cooTribeMapper.inquirePage(cooTribeQuery.createPage(),cooTribeQuery);
    }

    @Override
    public CooTribeDTO selectId(Long id) {
        return cooTribeMapper.selectId(id);
    }
}




