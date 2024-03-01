package com.wcwy.websocket.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wawy.company.api.TCompanyApi;
import com.wawy.company.api.TJobHunterApi;
import com.wawy.company.api.TRecommendApi;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.web.config.CacheClient;
import com.wcwy.company.entity.TCompany;
import com.wcwy.company.entity.TJobhunter;
import com.wcwy.company.entity.TRecommend;
import com.wcwy.websocket.dto.ChatUserDTO;
import com.wcwy.websocket.dto.UserInfoDTO;
import com.wcwy.websocket.entity.ChatNews;
import com.wcwy.websocket.entity.ChatUser;
import com.wcwy.websocket.query.ChatNewsQuery;
import com.wcwy.websocket.service.ChatNewsService;
import com.wcwy.websocket.service.ChatRecordService;
import com.wcwy.websocket.service.ChatUserService;
import com.wcwy.websocket.session.CompanyMetadata;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * ClassName: ChatUserController
 * Description:
 * date: 2023/10/25 10:36
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
//@RestController
//@RequestMapping("/chatUser")
//@Api(tags = "查看信息")
public class ChatUserController {

    @Autowired
    private ChatUserService chatUserService;

    @Autowired
    private TCompanyApi tCompanyApi;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ChatNewsService chatNewsService;
    @Autowired
    private ChatRecordService chatRecordService;
    @Autowired
    private TJobHunterApi tJobHunterApi;
    @Autowired
    private TRecommendApi tRecommendApi;
    @Autowired
    private CompanyMetadata companyMetadata;


    @GetMapping("/select")
    @ApiOperation("查询聊天人员")
    public R<ChatUserDTO> select() {
        String userid = companyMetadata.userid();
       // Set<Object> objects = redisUtils.gerSortedSet(Cache.CACHE_NEWS_ID.getKey() + userid);
        List<ChatUserDTO> list = null;
/*        if(objects !=null){
            Iterator<Object> iterator = objects.iterator();
            while (iterator.hasNext()){
                Object next = iterator.next();
                ChatUserDTO chatUserDTO = JSON.parseObject(next.toString(), ChatUserDTO.class);
                list.add(chatUserDTO);
            }

        }else {
            list = chatUserService.getList(userid);
        }*/
        list = chatUserService.getList(userid);
       if(list==null){
           return R.success(list);
       }
        List<ChatUserDTO> list2 = new ArrayList<>(list.size());
        //查找聊天人信息
        Set<String> lists = new HashSet<>(list.size());
        //聊天室
        Set<String> chatId = new HashSet<>(list.size());

        if (list != null && list.size() > 0) {
            //做区分
            for (ChatUserDTO chatUserDTO : list) {
                chatId.add(chatUserDTO.getId());
                ChatUserDTO chatUserDTO1 = null;
                if (chatUserDTO.getUserId().equals(userid)) {
                    lists.add(chatUserDTO.getAnotherId());
                    chatUserDTO1 = chatUserDTO;

                } else if (chatUserDTO.getAnotherId().equals(userid)) {
                    lists.add(chatUserDTO.getUserId());
                    chatUserDTO1 = new ChatUserDTO();
                    chatUserDTO1.setId(chatUserDTO.getId());
                    chatUserDTO1.setUserId(chatUserDTO.getAnotherId());
                    chatUserDTO1.setAnotherId(chatUserDTO.getUserId());
                    chatUserDTO1.setUnread(chatUserDTO.getUnread());
                }
                //缓存聊天室
              //  redisUtils.SortedSet(Cache.CACHE_NEWS_ID.getKey() + userid, JSON.toJSONString(chatUserDTO), 1);
                list2.add(chatUserDTO1);
            }
        }else {
            return R.success(list);
        }
        List<ChatUserDTO> chatUserDTOList = new ArrayList<>();
        Map<String, UserInfoDTO> stringUserInfoDTOMap = listMap(lists);
        if (stringUserInfoDTOMap != null) {
            for (ChatUserDTO chatUserDTO : list2) {
                chatUserDTO.setUserInfoDTO(stringUserInfoDTOMap.get(chatUserDTO.getAnotherId()));
                chatUserDTOList.add(chatUserDTO);
            }
        }


        List<ChatNews> list1 = chatNewsService.getList(chatId);
        for (ChatUserDTO chatUserDTO : chatUserDTOList) {
            for (ChatNews chatNews : list1) {
                if (chatUserDTO.getId().equals(chatNews.getChatId())) {
                   // System.out.println(chatNews.getMsg());
                    chatNews.setMsg(chatNews.getMsg().replace("\"", " "));
                    //System.out.println(chatNews.getMsg());
                    chatUserDTO.setChatNews(chatNews);
                    continue;
                }
            }

        }

        return R.success(chatUserDTOList);
    }


    public Map<String, UserInfoDTO> listMap(Set<String> strings) {
        Iterator<String> iterator = strings.iterator();
        Map<String, UserInfoDTO> map = new HashMap<>(strings.size());
        //区分求职者 企业 推荐官
        List<String> jobHunter = new ArrayList<>(strings.size());
        List<String> company = new ArrayList<>(strings.size());
        List<String> recommend = new ArrayList<>(strings.size());
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (next.contains("TC")) {
                company.add(next);
            } else if (next.contains("TR")) {
                recommend.add(next);
            } else if (next.contains("TJ")) {
                jobHunter.add(next);
            } else if (next.contains("system")) {
                UserInfoDTO userInfoDTO = new UserInfoDTO();
                userInfoDTO.setAvatar("https://dev-1313175594.cos.ap-guangzhou.myqcloud.com/customLogo/1b1d6f7b79e645eca0bf8b15629e216d.png");
                userInfoDTO.setId("system");
                userInfoDTO.setUserName("网才无忧小助手!");
                map.put("system", userInfoDTO);
            }else if(next.contains("customerService")){
                UserInfoDTO userInfoDTO = new UserInfoDTO();
                userInfoDTO.setAvatar("https://dev-1313175594.cos.ap-guangzhou.myqcloud.com/customLogo/1b1d6f7b79e645eca0bf8b15629e216d.png");
                userInfoDTO.setId("system");
                userInfoDTO.setUserName("网才无忧小助手!");
                map.put("customerService", userInfoDTO);
            }
        }
        if (company != null && company.size() > 0) {
            List<TCompany> list = tCompanyApi.companyLists(company);
            if (list != null && list.size() > 0) {
                for (TCompany tCompany : list) {
                    UserInfoDTO userInfoDTO = new UserInfoDTO();
                    userInfoDTO.setAvatar(tCompany.getLogoPath());
                    userInfoDTO.setId(tCompany.getCompanyId());
                    userInfoDTO.setUserName(tCompany.getContactName());
                    map.put(tCompany.getCompanyId(), userInfoDTO);
                }
            }
        }
        if (jobHunter != null && jobHunter.size() > 0) {
            List<TJobhunter> lists = tJobHunterApi.lists(jobHunter);
            if (lists != null && lists.size() > 0) {
                for (TJobhunter list : lists) {
                    UserInfoDTO userInfoDTO = new UserInfoDTO();
                    userInfoDTO.setAvatar(list.getAvatar());
                    userInfoDTO.setId(list.getUserId());
                    userInfoDTO.setUserName(list.getUserName());
                    map.put(list.getUserId(), userInfoDTO);
                }
            }
        }

        if (jobHunter != null && jobHunter.size() > 0) {
            List<TRecommend> listsTRecommend = tRecommendApi.lists(jobHunter);
            if (listsTRecommend != null && listsTRecommend.size() > 0) {
                for (TRecommend tRecommend : listsTRecommend) {
                    UserInfoDTO userInfoDTO = new UserInfoDTO();
                    userInfoDTO.setAvatar(tRecommend.getHeadPath());
                    userInfoDTO.setId(tRecommend.getId());
                    userInfoDTO.setUserName(tRecommend.getUsername());
                    map.put(tRecommend.getId(), userInfoDTO);
                }
            }
        }
        return map;
    }

    @PostMapping("/selectNews")
    @ApiOperation("获取消息")
    @Transactional(rollbackFor = Exception.class)
    public R<ChatNews>  selectNews(@Valid @RequestBody ChatNewsQuery chatId){

       // queryWrapper. gt("" );

 /*       ListOperations listOperations = redisTemplate.opsForList();
        //未读信息
        List range = listOperations.range(Cache.UNREAD_CACHE_NEWS.getKey() + chatId, 0, -1);
        //已读信息
        List range1 = listOperations.range(Cache.CACHE_NEWS.getKey() + chatId, 0, -1);

        for (Object o : range) {
            ChatNews chatNews= JSON.parseObject(o.toString(),ChatNews.class);
            if(chatNews.getToId().equals(companyMetadata.userid())) {
                chatNews.setReadStatus(1);
                listOperations.leftPush(Cache.CACHE_NEWS.getKey() + chatId,JSON.toJSONString(chatNews));
            }else {
                range.addAll(range1);
            }
        }
        if(){

        }*/
        Boolean aBoolean = redisUtils.setIfAbsentMinutes(Cache.CACHE_SOCKET.getKey() + chatId.getChatId()+companyMetadata.userid(), chatId.getChatId(), 2);
        IPage<ChatNews> page=null;

        if(aBoolean){
            UpdateWrapper updateWrapper=new UpdateWrapper();
            updateWrapper.eq("chat_id",chatId.getChatId());
            updateWrapper.set("read_status",1);
            chatNewsService.update(updateWrapper);

            UpdateWrapper updateWrapper1=new UpdateWrapper();
            updateWrapper1.eq("chat_id",chatId.getChatId());
            updateWrapper1.eq("user_id",companyMetadata.userid());
            updateWrapper1.set("unread",0);
            chatRecordService.update(updateWrapper1);

            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("chat_id",chatId.getChatId());
            queryWrapper.eq("to_id",companyMetadata.userid());
            queryWrapper.orderByDesc("send_time");
            //  List<ChatNews> list = chatNewsService.list(queryWrapper);
            page= chatNewsService.page(chatId.createPage(),queryWrapper);
            redisUtils.set("cache_news"+chatId.getChatId(),JSON.toJSONString(page));
        }{
            Object o = redisUtils.get("cache_news" + chatId.getChatId());
            if(o !=null){
                JSONObject jsonObject = JSON.parseObject(o.toString());
                return R.success(jsonObject);
            }
        }

        return R.success(page);
    }


  /*  @GetMapping("/selectChatUser")
    public R selectChatUser(){
        String userid = companyMetadata.userid();
       List<ChatUser> a =chatUserService.selectChatUser(userid);
    }*/
}
