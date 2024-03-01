package com.wcwy.post.pojo;


import com.wcwy.post.entity.HeadhunterPositionRecord;
import com.wcwy.post.po.ProvincesCitiesPO;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;


/**
 * ClassName: TCompanyPostRecord
 * Description:
 * date: 2023/3/31 8:35
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
public class TCompanyPostRecord {
    /**
     * 发布岗位Id
     */
    private String postId;

    /**
     * 企业Id
     */
    private String companyId;


    /**
     * 企业logo
     */
    private String logo;


    /**
     * 所在城市
     */
    private ProvincesCitiesPO workCity;


    /**
     * 岗位开始薪资
     */
    private BigDecimal beginSalary;

    /**
     * 岗位结束薪资
     */
    private BigDecimal endSalary;


    /**
     * 岗位名称
     */
    private String postLabel;



    /**
     * 入职赏金/服务结算金
     */
    private BigDecimal hiredBounty;



    /**
     * 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)
     */
    private Integer postType;



    private List<HeadhunterPositionRecord> headhunterPositionRecord;

    private String companyName;

}
