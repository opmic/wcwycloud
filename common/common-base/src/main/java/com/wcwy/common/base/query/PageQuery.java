package com.wcwy.common.base.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * ClassName: PageQuery
 * Description:
 * date: 2022/9/3 10:07
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
public class PageQuery extends BaseQuery {

    private Long pageSize = 10L;

    private Long pageNo = 1L;

    public IPage createPage() {
        return new Page(this.pageNo, this.pageSize);
    }


}
