package com.zhengqing.modules.system.service.impl;

import com.zhengqing.modules.system.entity.SysLog;
import com.zhengqing.modules.system.dto.input.LogQueryPara;
import com.zhengqing.modules.system.mapper.LogMapper;
import com.zhengqing.modules.system.service.ILogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p> 系统管理 - 日志表 服务实现类 </p>
 *
 * @author: zhengqing
 * @date: 2019-09-18 10:51:57
 */
@Service
@Transactional
public class LogServiceImpl extends ServiceImpl<LogMapper, SysLog> implements ILogService {

    @Autowired
    LogMapper logMapper;

    @Override
    public void listPage(Page<SysLog> page, LogQueryPara para) {
        List<SysLog> result = logMapper.selectLogs(page, para);
        result.forEach( e->{
            if (e.getUserId()==0){
                e.setUsername("非法人员");
            }
        });
        page.setRecords(result);
    }

    @Override
    public List<SysLog> list(LogQueryPara para) {
        return logMapper.selectLogs(para);
    }

    @Override
    public Integer save(SysLog para) {
        if (para.getId()!=null) {
            logMapper.updateById(para);
        } else {
            logMapper.insert(para);
        }
        return para.getId();
    }

}
