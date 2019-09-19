package com.zhengqing.modules.code.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhengqing.modules.code.dto.input.BsTemplateQueryPara;
import com.zhengqing.modules.code.entity.BsTemplate;
import com.zhengqing.modules.code.entity.Project;
import com.zhengqing.modules.code.mapper.BsTemplateMapper;
import com.zhengqing.modules.code.service.IBsTemplateService;
import com.zhengqing.modules.common.exception.MyException;
import com.zhengqing.modules.shiro.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 *  <p> 项目代码模板表 服务实现类 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/20 16:08
 */
@Service
@Transactional
public class BsTemplateServiceImpl extends ServiceImpl<BsTemplateMapper, BsTemplate> implements IBsTemplateService {

    @Autowired
    BsTemplateMapper bsTemplateMapper;

    @Override
    public void listPage(Page<BsTemplate> page, BsTemplateQueryPara filter) {
        filter.setUserId( ShiroUtils.getUserInfo().getId() );
        page.setRecords(bsTemplateMapper.selectBsTemplates(page, filter));
    }

    @Override
    public List<BsTemplate> list(BsTemplateQueryPara filter) {
        filter.setUserId( ShiroUtils.getUserInfo().getId() );
        return bsTemplateMapper.selectBsTemplates(filter);
    }

    @Override
    public Integer save(BsTemplate para) {
        para.setUserId( ShiroUtils.getUserInfo().getId() );

        BsTemplateQueryPara bsTemplateQueryPara = new BsTemplateQueryPara();
        bsTemplateQueryPara.setUserId( para.getUserId() );
        bsTemplateQueryPara.setType( para.getType() );
        List<BsTemplate> bsTemplateList = bsTemplateMapper.selectBsTemplates( bsTemplateQueryPara );

        if (para.getId() != null) {
            if ( !CollectionUtils.isEmpty( bsTemplateList ) ){
                BsTemplate bsTemplate = bsTemplateMapper.selectById(para.getId());
                if ( !para.getType().equals( bsTemplate.getType() ) ){
                    throw new MyException( "模板类型重复，请重新输入！" );
                }
            }
            bsTemplateMapper.updateById(para);
        } else {
            if ( !CollectionUtils.isEmpty( bsTemplateList ) ){
                throw new MyException( "模板类型重复，请重新输入！" );
            }
            bsTemplateMapper.insert(para);
        }
        return para.getId();
    }

}
