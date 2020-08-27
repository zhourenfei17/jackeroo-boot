package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.system.entity.SysMenuPermissionConfig;
import cn.hub.jackeroo.system.mapper.SysMenuPermissionConfigMapper;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 菜单权限配置 服务实现类
 * </p>
 *
 * @author jackeroo
 * @since 2020-08-27
 */
@Service
public class SysMenuPermissionConfigService extends ServiceImpl<SysMenuPermissionConfigMapper, SysMenuPermissionConfig>  {

    @Resource
    private SysMenuPermissionConfigMapper mapper;

    /**
     * 查询数据列表-带分页
     * @param sysMenuPermissionConfig
     * @param pageParam
     * @return
     */
    public IPage<SysMenuPermissionConfig> findPage(SysMenuPermissionConfig sysMenuPermissionConfig, PageParam pageParam){
        Page<SysMenuPermissionConfig> page = sysMenuPermissionConfig.initPage(pageParam);
        page.setRecords(mapper.findList(sysMenuPermissionConfig));

        return page;
    }
}
