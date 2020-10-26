package cn.hub.jackeroo.online.service;

import cn.hub.jackeroo.online.entity.OnlineDefaultConfig;
import cn.hub.jackeroo.online.mapper.OnlineDefaultConfigMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* <p>
* 生成代码默认配置项 服务实现类
* </p>
*
* @author jackeroo
* @since 2020-10-26
*/
@Service
public class OnlineDefaultConfigService extends ServiceImpl<OnlineDefaultConfigMapper, OnlineDefaultConfig> {
    @Resource
    private OnlineDefaultConfigMapper mapper;

}