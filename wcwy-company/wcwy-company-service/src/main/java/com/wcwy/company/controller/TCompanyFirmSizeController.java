package com.wcwy.company.controller;

import com.wcwy.common.base.result.R;
import com.wcwy.company.entity.TCompanyFirmSize;
import com.wcwy.company.service.TCompanyFirmSizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: TCompanyFirmSizeController
 * Description:
 * date: 2022/9/2 10:30
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "企业规模接口")
@RestController
@RequestMapping("/tCompanyFirmSize")
public class TCompanyFirmSizeController {

    @Autowired
    private TCompanyFirmSizeService tCompanyFirmSizeService;

    /**
     * @param
     * @return null
     * @Description:获取企业规模信息
     * @Author tangzhuo
     * @CreateTime 2022/9/2 10:43
     */

    @GetMapping("/select")
    @ApiOperation(value = "获取企业规模信息", notes = "获取企业规模信息")
    public R<TCompanyFirmSize> select() {
        return R.success(tCompanyFirmSizeService.list());
    }
}
