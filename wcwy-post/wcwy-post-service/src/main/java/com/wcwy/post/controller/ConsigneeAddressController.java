package com.wcwy.post.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nimbusds.jose.util.IntegerUtils;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.post.entity.ConsigneeAddress;
import com.wcwy.post.service.ConsigneeAddressService;
import com.wcwy.post.session.CompanyMetadata;
import com.wcwy.post.vo.ConsigneeAddressVO;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * ClassName: ConsigneeAddressController
 * Description:
 * date: 2022/12/1 9:04
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "发票邮寄地址")
@RestController
@RequestMapping("/consigneeAddress")
public class ConsigneeAddressController {
    @Autowired
    private ConsigneeAddressService consigneeAddressService;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private CompanyMetadata companyMetadata;

    @PostMapping("/addConsigneeAddress")
    @ApiOperation("添加邮寄地址")
    @Transactional
    @Log(title = "添加邮寄地址", businessType = BusinessType.INSERT)
    @AutoIdempotent
    public R addConsigneeAddress(@Valid @RequestBody ConsigneeAddressVO consigneeAddressVO) {
        if (consigneeAddressVO.getTacitlyApprove() == 0) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("user_id", companyMetadata.userid());
            updateWrapper.set("tacitly_approve", 1);
            consigneeAddressService.update(updateWrapper);
        }

        ConsigneeAddress consigneeAddress = new ConsigneeAddress();
        BeanUtils.copyProperties(consigneeAddressVO, consigneeAddress);
        consigneeAddress.setConsigneeAddressId(idGenerator.generateCode("CA"));
        consigneeAddress.setUserId(companyMetadata.userid());
        boolean save = consigneeAddressService.save(consigneeAddress);
        if (save) {
            return R.success("添加成功!");
        }
        return R.fail("添加失败!");
    }


    @PostMapping("/updateConsigneeAddress")
    @ApiOperation("修改邮寄地址")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "修改邮寄地址", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R updateConsigneeAddress(@Valid @RequestBody ConsigneeAddressVO consigneeAddressVO) {
        if (StringUtils.isEmpty(consigneeAddressVO.getConsigneeAddressId())) {
            return R.fail("收件地址id不能为空!");
        }
        if (consigneeAddressVO.getTacitlyApprove() == 0) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("user_id", companyMetadata.userid());
            updateWrapper.set("tacitly_approve", 1);
            consigneeAddressService.update(updateWrapper);
        }
        ConsigneeAddress consigneeAddress = new ConsigneeAddress();
        BeanUtils.copyProperties(consigneeAddressVO, consigneeAddress);
        consigneeAddress.setUserId(companyMetadata.userid());
        boolean b = consigneeAddressService.updateById(consigneeAddress);
        if (b) {
            return R.success("修改成功!");
        }
        return R.fail("修改失败!");
    }


    @PostMapping("/deleteConsigneeAddress")
    @ApiImplicitParam(name = "consigneeAddressId", value = "邮箱地址id", required = true)
    @Transactional
    @ApiOperation("删除邮寄地址!")
    @Log(title = "删除邮寄地址", businessType = BusinessType.DELETE)
    @AutoIdempotent
    public R deleteConsigneeAddress(@RequestBody List<String> consigneeAddressId) throws Exception {
        String userid = companyMetadata.userid();
        if (consigneeAddressId.size() > 0) {
            for (String s : consigneeAddressId) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("user_id", userid);
                queryWrapper.eq("consignee_address_id", s);
                boolean remove = consigneeAddressService.remove(queryWrapper);
                if (!remove) {
                    throw new Exception("删除失败!");
                }
            }
            return R.success("删除成功!");
        }
        return R.fail("删除失败!");
    }


    @GetMapping("/selectConsigneeAddress")
    @ApiOperation("查询邮箱寄地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "consigneeAddressId", required = false, value = "收件地址id")
    })
    @Log(title = "查询邮箱寄地址", businessType = BusinessType.SELECT)
    public R<ConsigneeAddress> selectConsigneeAddress(@RequestParam(value = "consigneeAddressId", required = false) String consigneeAddressId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",companyMetadata.userid());
        if (!StringUtils.isEmpty(consigneeAddressId)) {
            queryWrapper.eq("consignee_address_id", consigneeAddressId);
        }
        queryWrapper.orderByAsc("tacitly_approve");
        List<ConsigneeAddress> list = consigneeAddressService.list(queryWrapper);
        return R.success(list);
    }


}
