package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.system.entity.SysMenuPermissionConfig;
import cn.hub.jackeroo.system.entity.SysMenuPermissionGroup;
import cn.hub.jackeroo.system.mapper.SysMenuPermissionConfigMapper;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private final SysMenuPermissionGroupService groupService;

    @Autowired
    public SysMenuPermissionConfigService(@Lazy SysMenuPermissionGroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * 查询数据列表-带分页
     * @param sysMenuPermissionConfig
     * @param pageParam
     * @return
     */
    public IPage<SysMenuPermissionConfig> findPage(SysMenuPermissionConfig sysMenuPermissionConfig, PageParam pageParam){
        Page<SysMenuPermissionConfig> page = sysMenuPermissionConfig.initPage(pageParam);
        page.setRecords(getBaseMapper().findList(sysMenuPermissionConfig));

        return page;
    }

    /**
     * 获取默认分组的权限列表
     * @return
     */
    public List<SysMenuPermissionConfig> findListByDefaultGroup(){
        LambdaQueryWrapper<SysMenuPermissionGroup> groupQuery = new LambdaQueryWrapper<>();
        groupQuery.eq(SysMenuPermissionGroup::getIsDefault, Constant.BOOLEAN_YES);

        SysMenuPermissionGroup group = groupService.getOne(groupQuery, false);
        if(group != null){
            return findListByGroupId(group.getId());
        }

        return null;
    }

    /**
     * 通过分组id获取权限列表
     * @param groupId
     * @return
     */
    public List<SysMenuPermissionConfig> findListByGroupId(long groupId){
        LambdaQueryWrapper<SysMenuPermissionConfig> query = new LambdaQueryWrapper<>();
        query.eq(SysMenuPermissionConfig::getGroupId, groupId);
        query.orderByAsc(SysMenuPermissionConfig::getSort);

        return super.list(query);
    }

    /**
     * 根据分组id删除
     * @param groupId
     */
    public void deleteByGroupId(String groupId){
        LambdaQueryWrapper<SysMenuPermissionConfig> query = new LambdaQueryWrapper<>();
        query.eq(SysMenuPermissionConfig::getGroupId, groupId);

        super.remove(query);
    }

    /**
     * 获取最大排序号
     * @param groupId
     * @return
     */
    public int getMaxSort(String groupId){
        LambdaQueryWrapper<SysMenuPermissionConfig> query = new LambdaQueryWrapper<>();
        query.eq(SysMenuPermissionConfig::getGroupId, groupId);
        query.orderByDesc(SysMenuPermissionConfig::getSort);
        query.last("limit 1");

        SysMenuPermissionConfig confg = super.getOne(query, false);
        if(confg != null){
            return confg.getSort() - (confg.getSort() % 10) + 10;
        }else{
            return 10;
        }
    }
}
