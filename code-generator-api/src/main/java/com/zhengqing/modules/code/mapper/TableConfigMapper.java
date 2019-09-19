package com.zhengqing.modules.code.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.zhengqing.modules.code.dto.input.TableConfigQueryPara;
import com.zhengqing.modules.code.entity.TableConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  <p> 项目数据表配置信息表 Mapper 接口 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/22 11:14
 */
public interface TableConfigMapper extends BaseMapper<TableConfig> {

    /**
     * 列表分页
     *
     * @param page
     * @param filter
     * @return
     */
    List<TableConfig> selectTableConfigs(Pagination page, @Param("filter") TableConfigQueryPara filter);

    /**
     * 列表
     *
     * @param filter
     * @return
     */
    List<TableConfig> selectTableConfigs(@Param("filter") TableConfigQueryPara filter);

}
