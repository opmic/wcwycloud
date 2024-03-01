package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.InvitationCode;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.dto.PositionAppliedDTO;
import com.wcwy.company.entity.EiCompanyPost;
import com.wcwy.company.entity.PositionApplied;
import com.wcwy.company.entity.ReferrerRecord;
import com.wcwy.company.query.PositionAppliedQuery;
import com.wcwy.company.service.*;
import com.wcwy.company.mapper.PositionAppliedMapper;
import com.wcwy.company.session.CompanyMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author Administrator
 * @description 针对表【position_applied(求职者意向职位)】的数据库操作Service实现
 * @createDate 2023-08-30 13:56:27
 */
@Service
public class PositionAppliedServiceImpl extends ServiceImpl<PositionAppliedMapper, PositionApplied>
        implements PositionAppliedService {

    @Resource
    private PositionAppliedMapper positionAppliedMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ReferrerRecordService referrerRecordService;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private EiCompanyPostService eiCompanyPostService;

    @Override
    public IPage<PositionAppliedDTO> select(PositionAppliedQuery positionAppliedQuery) {
        return positionAppliedMapper.select(positionAppliedQuery.createPage(), positionAppliedQuery);
    }

    @Override
    public Boolean sendAResume(String postId, Long time, String QRCode, String userid) {
        boolean aBoolean = redisUtils.setIfAbsent("SRJH:"+userid + postId, postId, 7 * 24);
        if (aBoolean) {
            //判断是否携带邀请码
            if (!StringUtils.isEmpty(QRCode)) {
                //获取邀请的推荐官
                Object o = redisUtils.get(InvitationCode.INVITATION_URL_CODE.getType() + QRCode);
                if (!StringUtils.isEmpty(o)) {
                    EiCompanyPost byId = eiCompanyPostService.getById(postId);
                  /*  Integer postType = byId.getPostType();*/
                    PositionApplied positionApplied = new PositionApplied();
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("t_job_hunter_id", userid);
                    queryWrapper.eq("recommend_id", o);
                    ReferrerRecord one = referrerRecordService.getOne(queryWrapper);
                    //判断人才对推荐官的类型
                    if (one != null && one.getCorrelationType() == 0) {
                        positionApplied.setSource(0);
                    } else {
                        positionApplied.setSource(1);
                    }
                    //如果不存在则插入
                    if (one == null) {
                        ReferrerRecord referrerRecord = new ReferrerRecord();
                        referrerRecord.setTJobHunterId(userid);
                        referrerRecord.setRecommendId(o.toString());
                        referrerRecord.setCreateTime(LocalDateTime.now());
                      //  referrerRecord.setOrigin(1);
                        referrerRecord.setCorrelationType(4);
                        boolean save1 = referrerRecordService.save(referrerRecord);
                    }

                    positionApplied.setPositionAppliedId(idGenerator.generateCode("PA"));
                    positionApplied.setJobHunter(userid);
                    positionApplied.setPutTime(LocalDateTime.now());
                    // 将时间戳转换为LocalDateTime
                    Instant instant = Instant.ofEpochMilli(time);
                    positionApplied.setPromotionTime(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
                    positionApplied.setPostLabel(byId.getPostLabel());
                    positionApplied.setPostType(byId.getPostType());
                    positionApplied.setCompanyId(byId.getCompanyId());
                    positionApplied.setRecommendId(o.toString());
                    boolean save = this.save(positionApplied);
                    return save;
                }
            }
        } else {
            return false;
        }

        redisUtils.del(userid + postId);
        return false;
    }
}




