package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.entity.TJobhunterExpectPosition;
import com.wcwy.company.service.TJobhunterExpectPositionService;
import com.wcwy.company.mapper.TJobhunterExpectPositionMapper;
import com.wcwy.company.vo.AddTJobhunterExpectPositionVO;
import com.wcwy.company.vo.TJobhunterExpectPositionVO;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【t_jobhunter_expect_position(期望职位表)】的数据库操作Service实现
 * @createDate 2022-10-08 11:58:41
 */
@Service
public class TJobhunterExpectPositionServiceImpl extends ServiceImpl<TJobhunterExpectPositionMapper, TJobhunterExpectPosition>
        implements TJobhunterExpectPositionService {
    @Autowired
    private TJobhunterExpectPositionMapper tJobhunterExpectPositionMapper;

    @Autowired
    private IDGenerator idGenerator;
    @Override
    public String postionId(String userid) {
        return tJobhunterExpectPositionMapper.postionId(userid);
    }

    @Override
    public Boolean adds(List<AddTJobhunterExpectPositionVO> tJobhunterExpectPositionVO, String s) {
        List<TJobhunterExpectPosition> list=new ArrayList<>(tJobhunterExpectPositionVO.size());
        for (AddTJobhunterExpectPositionVO jobhunterExpectPositionVO : tJobhunterExpectPositionVO) {
            TJobhunterExpectPosition tJobhunterExpectPosition=new TJobhunterExpectPosition();
            BeanUtils.copyProperties(jobhunterExpectPositionVO,tJobhunterExpectPosition);
            tJobhunterExpectPosition.setPostionId(idGenerator.generateCode("PT"));
            tJobhunterExpectPosition.setCreateTime(LocalDateTime.now());
            tJobhunterExpectPosition.setResumeId(s);
            list.add(tJobhunterExpectPosition);
        }
        return this.saveBatch(list);
    }

    @Override
    public int selectCount(String userId) {

        return  tJobhunterExpectPositionMapper.selectCounts(userId);
    }

    @Override
    public List<String> selectPositionName(String userid) {
        return tJobhunterExpectPositionMapper.selectPositionName(userid);
    }
}




