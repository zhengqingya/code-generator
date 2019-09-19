package com.zhengqing.modules.common.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *  <p> 基础分页检索参数 </p>
 *
 * @description:
 * @author: zhengqing
 * @date: 2019/9/13 0013 1:57
 */
@ApiModel
public class BasePageQuery extends BaseInput {
    @ApiModelProperty(value = "当前页", required = true, position = 0)
    private Integer page;
    @ApiModelProperty(value = "每页显示数量", required = true, position = 1)
    private Integer limit;

    public Integer getPage() {
        return page == null ? 1 : page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit == null ? Integer.MAX_VALUE : limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
