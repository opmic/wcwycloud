package com.wcwy.post.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.redis.enums.PostRecordSole;
import com.wcwy.common.redis.enums.Record;
import com.wcwy.post.dto.TotalPostShare;
import com.wcwy.post.entity.TPostShare;
import com.wcwy.post.produce.PostShareProduce;
import com.wcwy.post.service.TPostShareService;
import com.wcwy.post.mapper.TPostShareMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @description 针对表【t_post_share(发布岗位纪录表)】的数据库操作Service实现
 * @createDate 2022-09-15 17:26:24
 */
@Service
public class TPostShareServiceImpl extends ServiceImpl<TPostShareMapper, TPostShare>
        implements TPostShareService {

    @Resource
    private RedisTemplate redisTemplate;
    @Autowired
    private PostShareProduce postShareProduce;

    @Autowired
    private TPostShareMapper tPostShareMapper;
    @Override
    public void updateRedisPost(String jobHunter, String post, Integer state, String putInUser) {
        ValueOperations<String, TPostShare> valueOperations = redisTemplate.opsForValue();
        ValueOperations<String, String> shareValueOperations = redisTemplate.opsForValue();
        //0:推荐 1:浏览、 2:下载、3排除  4:约面、5:面试中6淘汰、7:offer、8:入职、9:保证期
        Long time = 604800L;
        TPostShare tPostShare = this.selectPost(post);
        if (state == 0) {
            String browse = PostRecordSole.SHARE.getValue() + post + jobHunter + putInUser;
            Boolean member = shareValueOperations.setIfAbsent(browse, jobHunter, time, TimeUnit.SECONDS);
            if (member) {
                tPostShare.setShareSize(tPostShare.getShareSize() + 1);
            }
        } else if (state == 1) {
            String browse = PostRecordSole.BROWSE.getValue() + post + jobHunter + putInUser;
            Boolean member = shareValueOperations.setIfAbsent(browse, jobHunter, time, TimeUnit.SECONDS);
            if (member) {
                tPostShare.setBrowseSize(tPostShare.getBrowseSize() + 1);
            }
        } else if (state == 2) {
            String download = PostRecordSole.DOWNLOAD.getValue() + post + jobHunter + putInUser;
            Boolean member = shareValueOperations.setIfAbsent(download, jobHunter, time, TimeUnit.SECONDS);
            if (member) {
                tPostShare.setDownloadSize(tPostShare.getDownloadSize() + 1);
            }

        } else if (state == 4 || state == 5) {

            String subscribe = PostRecordSole.SUBSCRIBE.getValue() + post + jobHunter + putInUser;
            Boolean member = shareValueOperations.setIfAbsent(subscribe, jobHunter);
            if (member) {
                tPostShare.setSubscribe(tPostShare.getSubscribe() + 1);

            }
            String interview = PostRecordSole.INTERVIEW.getValue() + post + jobHunter + putInUser;
            Boolean members = shareValueOperations.setIfAbsent(interview, jobHunter, time, TimeUnit.SECONDS);
            if (members) {
                tPostShare.setInterviewSize(tPostShare.getInterviewSize() + 1);
            }
        } else if (state == 6 || state == 3) {
            String weedOut = PostRecordSole.WEED_OUT.getValue() + post + jobHunter + putInUser;
            Boolean member = shareValueOperations.setIfAbsent(weedOut, jobHunter, time, TimeUnit.SECONDS);
            if (member) {
                tPostShare.setWeedOut(tPostShare.getWeedOut() + 1);
            }
            //当淘汰人员则减少预约面试人员
            //查询是否存在  存在则删除
            String subscribe = PostRecordSole.SUBSCRIBE.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(subscribe);
            if (state == 6 && !StringUtils.isEmpty(s)) {
                redisTemplate.delete(subscribe);
                if (tPostShare.getSubscribe() > 0) {
                    tPostShare.setSubscribe(tPostShare.getSubscribe() - 1);
                }

            }

        } else if (state == 7) {
            String offer = PostRecordSole.OFFER.getValue() + post + jobHunter + putInUser;
            Boolean member = shareValueOperations.setIfAbsent(offer, jobHunter, time, TimeUnit.SECONDS);
            if (member) {
                //当发送offer人员则减少预约面试人员
                String subscribe = PostRecordSole.SUBSCRIBE.getValue() + post + jobHunter + putInUser;
                String s = shareValueOperations.get(subscribe);
                if (!StringUtils.isEmpty(s)) {
                    redisTemplate.delete(subscribe);
                    if (tPostShare.getSubscribe() > 0) {
                        tPostShare.setSubscribe(tPostShare.getSubscribe() - 1);
                    }

                }
                tPostShare.setOfferSize(tPostShare.getOfferSize() + 1);
            }

        } else if (state == 8) {
            String entry = PostRecordSole.ENTRY.getValue() + post + jobHunter + putInUser;
            Boolean member = shareValueOperations.setIfAbsent(entry, jobHunter, time, TimeUnit.SECONDS);
            if (member) {
                tPostShare.setEntrySize(tPostShare.getEntrySize() + 1);
            }

        } else if (state == 9) {
            String overInsured = PostRecordSole.OVER_INSURED.getValue() + post + jobHunter + putInUser;
            Boolean member = shareValueOperations.setIfAbsent(overInsured, jobHunter, time, TimeUnit.SECONDS);
            if (member) {
                tPostShare.setOverInsured(tPostShare.getOverInsured() + 1);
            }
        }
        redisTemplate.delete(Record.POST_RECORD.getValue() + post);//删除重新插入
        valueOperations.set(Record.POST_RECORD.getValue() + post, tPostShare);
        this.updateById(tPostShare);
    }
    @Override
    public TotalPostShare selectTotalPostShare(String companyId) {
        return tPostShareMapper.selectTotalPostShare(companyId);
    }

    @Override
    public void updateShare(String postId) {
        ValueOperations<String, TPostShare> valueOperations = redisTemplate.opsForValue();
        TPostShare tPostShare = this.selectPost(postId);
        if(tPostShare ==null){
            return;
        }
        tPostShare.setDownloadSize(tPostShare.getDownloadSize() + 1);
        redisTemplate.delete(Record.POST_RECORD.getValue() + postId);//删除重新插入
        boolean b = this.updateById(tPostShare);
        if(b){
            valueOperations.set(Record.POST_RECORD.getValue() + postId, tPostShare);
        }

    }

    /**
     * @return null
     * @Description: 返回发布岗位的记录数
     * @Author tangzhuo
     * @CreateTime 2022/11/11 8:51
     */

    public TPostShare selectPost(String post) {
        ValueOperations<String, TPostShare> valueOperations = redisTemplate.opsForValue();
        TPostShare select = valueOperations.get(Record.POST_RECORD.getValue() + post);
        if (select == null) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("company_post_id", post);
            select = this.getOne(queryWrapper);
            if (select != null) {
                redisTemplate.delete(Record.POST_RECORD.getValue() + post);//删除重新插入
                valueOperations.set(Record.POST_RECORD.getValue() + post, select);
            }
        }
        return select;
    }


    @Override
    public void cancelRedisPost(String jobHunter, String post, Integer state, String putInUser) {
        /* SetOperations<String, String> setOperations = redisTemplate.opsForSet();*/
        ValueOperations<String, TPostShare> valueOperations = redisTemplate.opsForValue();
        ValueOperations<String, String> shareValueOperations = redisTemplate.opsForValue();
        //1:浏览、 2:下载、3排除  4:约面、5:面试中6淘汰、7:offer、8:入职、9:保证期
        TPostShare tPostShare = this.selectPost(post);
        Long time = 604800L;
        if (state == 1) {
            String browse = PostRecordSole.BROWSE.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(browse);
            if (!StringUtils.isEmpty(s)) {
                redisTemplate.delete(browse);
                tPostShare.setBrowseSize(tPostShare.getBrowseSize() - 1);
            }
        } else if (state == 2) {
            String download = PostRecordSole.DOWNLOAD.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(download);
            if (!StringUtils.isEmpty(s)) {
                redisTemplate.delete(download);
                tPostShare.setDownloadSize(tPostShare.getDownloadSize() - 1);
            }

        } else if (state == 4) {
            String subscribe = PostRecordSole.SUBSCRIBE.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(subscribe);
            if (!StringUtils.isEmpty(s)) {
                redisTemplate.delete(subscribe);//进行删除
                if (tPostShare.getInterviewSize() > 0) {
                    tPostShare.setInterviewSize(tPostShare.getInterviewSize() - 1);
                }
            }
            String interview = PostRecordSole.INTERVIEW.getValue() + post + jobHunter + putInUser;
            String s1 = shareValueOperations.get(interview);
            if (!StringUtils.isEmpty(s1)) {
                redisTemplate.delete(interview);//进行删除
                tPostShare.setInterviewSize(tPostShare.getInterviewSize() - 1);
            }

        } else if (state == 5) {
            String interview = PostRecordSole.INTERVIEW.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(interview);
            if (!StringUtils.isEmpty(s)) {
                redisTemplate.delete(interview);//进行删除
                tPostShare.setInterviewSize(tPostShare.getInterviewSize() - 1);
            }

        } else if (state == 6 || state == 3) {
            String weedOut = PostRecordSole.WEED_OUT.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(weedOut);
            if (!StringUtils.isEmpty(s)) {
                redisTemplate.delete(weedOut);//进行删除
                tPostShare.setWeedOut(tPostShare.getWeedOut() - 1);
            }

        } else if (state == 7) {
            String offer = PostRecordSole.OFFER.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(offer);
            if (!StringUtils.isEmpty(s)) {
                redisTemplate.delete(offer);//进行删除
                if (tPostShare.getOfferSize() > 0) {
                    tPostShare.setOfferSize(tPostShare.getOfferSize() - 1);
                }

            }

        } else if (state == 8) {
            String entry = PostRecordSole.ENTRY.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(entry);
            if (!StringUtils.isEmpty(s)) {
                redisTemplate.delete(entry);//进行删除
                tPostShare.setEntrySize(tPostShare.getEntrySize() - 1);
            }

        } else if (state == 9) {
            String overInsured = PostRecordSole.OVER_INSURED.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(overInsured);
            if (!StringUtils.isEmpty(s)) {
                redisTemplate.delete(overInsured);//进行删除
                tPostShare.setOverInsured(tPostShare.getOverInsured() - 1);
            }
        }
        redisTemplate.delete(Record.POST_RECORD.getValue() + post);//删除重新插入
        valueOperations.set(Record.POST_RECORD.getValue() + post, tPostShare);
        this.updateById(tPostShare);
    }
}




