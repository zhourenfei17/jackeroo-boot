package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.system.entity.SysMenuPermissionConfig;
import cn.hub.jackeroo.system.entity.SysMenuPermissionGroup;
import cn.hub.jackeroo.system.mapper.SysMenuPermissionGroupMapper;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 菜单权限配置组别 服务实现类
 * </p>
 *
 * @author jackeroo
 * @since 2020-08-27
 */
@Service
public class SysMenuPermissionGroupService extends ServiceImpl<SysMenuPermissionGroupMapper, SysMenuPermissionGroup> {

    @Resource
    private SysMenuPermissionGroupMapper mapper;
    /**
     * 查询数据列表-带分页
     * @param sysMenuPermissionGroup
     * @param pageParam
     * @return
     */
    public IPage<SysMenuPermissionGroup> findPage(SysMenuPermissionGroup sysMenuPermissionGroup, PageParam pageParam){
        Page<SysMenuPermissionGroup> page = sysMenuPermissionGroup.initPage(pageParam);
        page.setRecords(mapper.findList(sysMenuPermissionGroup));

        return page;
    }
}
