package com.wcwy.company.controller;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.Base64Util;
import com.wcwy.common.base.utils.IpUtils;
import com.wcwy.common.base.utils.QRCodeUtil;
import com.wcwy.common.base.utils.UUID;
import com.wcwy.common.redis.enums.InvitationCode;
import com.wcwy.common.redis.enums.QRCode;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.asyn.ShareDataAsync;
import com.wcwy.company.entity.EiCompanyPost;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.service.EiCompanyPostService;
import com.wcwy.company.service.PromotionPostService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.QRCodeVO;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: QRCodeController
 * Description:
 * date: 2023/5/3 13:50
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/QRCode")
@Api(tags = "二维码接口")
@Slf4j
public class QRCodeController {


    //请求地址
    @Value("${weixin.url}")
    private String url;
    //appid
    @Value("${weixin.appid}")
    private String appid;
    //appSecret
    @Value("${weixin.appSecret}")
    private String appSecret;
    @Resource
    private RedisUtils redisUtils;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private IDGenerator idGenerator;

    @Autowired
    private ShareDataAsync shareDataAsync;
    @Autowired
    private EiCompanyPostService eiCompanyPostService;

    @Autowired
    private PromotionPostService promotionPostService;

    @PostMapping("/postQRCode")
    @ApiOperation("生成岗位二维码")
/*    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", required = true, value = "链接地址"),
            @ApiImplicitParam(name = "qrWidth", required = true, value = "二维码宽度"),
            @ApiImplicitParam(name = "qrHeight", required = true, value = "二维码高度")
            *//*@ApiImplicitParam(name = "postId", required = true, value = "职位")*//*
    })*/
    @Log(title = "生成岗位二维码", businessType = BusinessType.SELECT)
    public R postQRCode(@RequestBody QRCodeVO qrCodeVO) throws IOException {
       /* if(qrWidth==null || qrHeight==null){
            return R.fail("请检查数据！");
        }*/
        String userid = companyMetadata.userid();
        String os = System.getProperties().getProperty("os.name");
        String pathLogs = null;
        // System.out.println("获取文件地址1111111111111111111");
        //Windows操作系统
        if (os != null && os.toLowerCase().startsWith("windows")) {
            pathLogs = "D:/static/logs.png";
            // pathLogs = ClassLoader.getSystemClassLoader().getResource("static/logs.png").getPath();
            // pathLogs= ClassLoader.getSystemClassLoader().getResource("static/logs.png").getPath();
        } else if (os != null && os.toLowerCase().startsWith("linux")) {//Linux操作系统
            pathLogs = Paths.get("/wx/logs.png").toString();
        } else { //其它操作系统
            System.out.println(String.format("当前系统版本是:%s", os));
        }
        //  System.out.println("获取文件地址");
        //   System.out.println(pathLogs);
        if (!StringUtils.isEmpty(userid)) {
            String substring = userid.substring(0, 2);
            if ("TR".equals(substring)) {
                String code = (String) redisUtils.get(InvitationCode.CODE.getType() + companyMetadata.userid());
                if (StringUtils.isEmpty(code)) {
                    code = idGenerator.invitationCode("invitationCode");
                    redisUtils.set(InvitationCode.CODE.getType() + companyMetadata.userid(), code);
                    redisUtils.set(InvitationCode.INVITATION_URL_CODE.getType() + code, companyMetadata.userid());
                }
                qrCodeVO.setPath( qrCodeVO.getPath() + "&QRCode=" + code);
            }
        }
        MultipartFile encode = QRCodeUtil.encode("UTF-8", qrCodeVO.getPath(), qrCodeVO.getQrWidth(), qrCodeVO.getQrHeight(), pathLogs);
        String encode1 = Base64Util.encode(encode.getBytes());
        Map map = new HashMap();
        map.put("QRCode", encode1);
        map.put("url", qrCodeVO.getPath());
        return R.success(map);
    }

    @PostMapping("/QR")
    @ApiOperation("生成二维码")
    @Log(title = "生成二维码", businessType = BusinessType.SELECT)
    public R QRCode(@Valid @RequestBody QRCodeVO qrCodeVO) throws IOException {
        String os = System.getProperties().getProperty("os.name");
        String pathLogs = null;
        //Windows操作系统
        if (os != null && os.toLowerCase().startsWith("windows")) {
            // pathLogs = TCompanyController.class.getResource("/static/logs.png").getPath();
            // pathLogs= ClassLoader.getSystemClassLoader().getResource("static/logs.png").getPath();
            pathLogs = "D:/static/logs.png";
        } else if (os != null && os.toLowerCase().startsWith("linux")) {//Linux操作系统
            pathLogs = Paths.get("/wx/logs.png").toString();
        } else { //其它操作系统
            System.out.println(String.format("当前系统版本是:%s", os));
        }
        MultipartFile encode = QRCodeUtil.encode("UTF-8", qrCodeVO.getPath(), qrCodeVO.getQrWidth(), qrCodeVO.getQrHeight(), pathLogs);
        String encode1 = Base64Util.encode(encode.getBytes());
        Map map = new HashMap();
        map.put("QRCode", encode1);
        map.put("url", qrCodeVO.getPath());
        return R.success(map);
    }

    @GetMapping("/postSharing")
    @ApiOperation("分享岗位添加记录")
    @Log(title = "分享岗位添加记录", businessType = BusinessType.INSERT)
    @ApiImplicitParam(name = "postId", required = true, value = "职位id")
    public void postSharing(@RequestParam("postId") String postId) {
        String userid = companyMetadata.userid();
        if (!StringUtils.isEmpty(userid)) {
            String substring = userid.substring(0, 2);
            if (substring.equals("TR")) {
               try {
                   EiCompanyPost byId = eiCompanyPostService.getById(postId);
                   ProvincesCitiesPO workCity = byId.getWorkCity();
                   shareDataAsync.shareDataPost(workCity.getProvince(), workCity.getCity(), userid);
                   promotionPostService.add(postId,userid);
               }catch (Exception e){
                   log.error("分享岗位添加记录异常"+e);
               }
            }
        }
    }


    @GetMapping("/accessToken")
    @ApiOperation("获取微信accessToken")
    public R<Object> accessToken() {
        RestTemplate restTemplate = new RestTemplate();
        String api = url + "/cgi-bin/token?appid={appid}&secret={appSecret}&grant_type=client_credential";
        Map<String, Object> map = new HashMap<>();
        map.put("appid", appid);
        map.put("appSecret", appSecret);
        Object obj = restTemplate.getForObject(api, Object.class, map);//此处三个参数分别是请求地址、请求体以及返回参数类型
        System.out.println(map);
        return R.success(obj);
    }


    @PostMapping("/qrCode")
    @ApiOperation("获取微信二维码")
    public R<String> cenerateTheQrCode(String scene,String accesstoken,String page){
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("page",page);
        //deviceId和I都只是参数，可以自己按需要定义，用&分隔
        param.put("scene",scene);
        //参数必须是Json格式
        String json = JSON.toJSONString( param );
        byte[] bytes= HttpRequest.post("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+accesstoken)
                .header(HTTP.CONTENT_TYPE, "application/json").body(json).execute().bodyBytes();
        String encoded = "data:image/jpg;base64," + Base64.encodeBase64String(bytes).replaceAll("[\\s*\t\n\r]", "");
        return R.success(encoded);
    }

}
