package com.wcwy.company.controller;

import com.wcwy.common.base.result.R;
import com.wcwy.company.entity.TCompanyType;
import com.wcwy.company.service.TCompanyTypeService;
import io.swagger.annotations.Api;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: TCompanyTypeController
 * Description:
 * date: 2022/9/2 10:20
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/tCompanyType")
@Api(tags = "企业类型")
public class TCompanyTypeController {
    @Autowired
    private TCompanyTypeService tCompanyTypeService;

    /**
     * @param
     * @return null
     * @Description: 查询
     * @Author tangzhuo
     * @CreateTime 2022/9/2 10:24
     */
    @GetMapping("/select")
    public R<TCompanyType> select() {
        List<TCompanyType> list = tCompanyTypeService.list();
        return R.success(list);
    }

}
