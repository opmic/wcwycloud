package com.wcwy.post.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wcwy.common.base.utils.HttpClientUtil;
import com.wcwy.post.entity.TPosition;
import com.wcwy.post.service.TPositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: PositionController
 * Description:
 * date: 2023/4/20 9:56
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@Api(tags = "岗位类型")
@RequestMapping("/position")
public class PositionController {

    @Autowired
    private TPositionService tPositionService;
    @GetMapping("/selectPosition")
    @ApiOperation("查询岗位类型")
    public String selectPosition(){
        HttpClientUtil instance = HttpClientUtil.getInstance();
        String s = instance.sendHttpGet("https://www.zhipin.com/wapi/zpCommon/data/position.json");
        JSONObject jsonObject = JSON.parseObject(s);
        JSONArray zpData = jsonObject.getJSONArray("zpData");
        for (Object zpDatum : zpData) {
            System.out.println("A"+zpDatum);

            JSONObject jsonObject1 = JSON.parseObject(zpDatum.toString());
            TPosition tPosition=new TPosition();
            tPosition.setCode((Integer) jsonObject1.get("code"));
            tPosition.setName((String) jsonObject1.get("name"));
            tPosition.setFatherCode(0);
            tPosition.setRank(0);
            tPositionService.saveOrUpdate(tPosition);
            JSONArray zpData1 = jsonObject1.getJSONArray("subLevelModelList");
            for (Object o : zpData1) {
                System.out.println("B"+o);
                JSONObject jsonObject2 = JSON.parseObject(o.toString());
                TPosition tPosition1=new TPosition();
                tPosition1.setCode((Integer) jsonObject2.get("code"));
                tPosition1.setName((String) jsonObject2.get("name"));
                tPosition1.setFatherCode((Integer) jsonObject1.get("code"));
                tPosition1.setRank(1);
                tPositionService.saveOrUpdate(tPosition1);
                JSONArray zpData2 = jsonObject2.getJSONArray("subLevelModelList");
                for (Object o2 : zpData2) {
                    System.out.println("C"+o2);
                    JSONObject jsonObject3 = JSON.parseObject(o2.toString());
                    TPosition tPosition2=new TPosition();
                    tPosition2.setCode((Integer) jsonObject3.get("code"));
                    tPosition2.setName((String) jsonObject3.get("name"));
                    tPosition2.setFatherCode((Integer) jsonObject2.get("code"));
                    tPosition2.setRank(2);
                    tPositionService.saveOrUpdate(tPosition2);
                }
            }

        }
        return zpData.toJSONString();
    }
}
