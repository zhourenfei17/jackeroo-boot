package cn.hub.jackeroo.online.service;

import cn.hub.jackeroo.exception.JackerooException;
import cn.hub.jackeroo.online.entity.OnlineDefaultConfig;
import cn.hub.jackeroo.online.mapper.OnlineDefaultConfigMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    /**
     * 获取唯一的默认配置数据
     * @return
     */
    public OnlineDefaultConfig getConfig(){
        List<OnlineDefaultConfig> list = super.list();
        if(CollectionUtils.isEmpty(list)){
            return new OnlineDefaultConfig();
        }else{
            if(list.size() > 1){
                throw new JackerooException("默认配置数据异常，请检查！");
            }
            return list.get(0);
        }
    }

    @Override
    public boolean saveOrUpdate(OnlineDefaultConfig onlineDefaultConfig) {
        OnlineDefaultConfig config = getConfig();
        if(config.getId() == null){
            return super.save(onlineDefaultConfig);
        }else{
            onlineDefaultConfig.setId(config.getId());

            return super.updateById(onlineDefaultConfig);
        }
    }
}