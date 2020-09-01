package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.system.entity.SysMenuPermissionConfig;
import cn.hub.jackeroo.system.entity.SysMenuPermissionGroup;
import cn.hub.jackeroo.system.mapper.SysMenuPermissionGroupMapper;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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

    /**
     * 获取所有启用状态的权限分组
     * @return
     */
    public List<SysMenuPermissionGroup> findAllEnable(){
        LambdaQueryWrapper<SysMenuPermissionGroup> query = new LambdaQueryWrapper<>();
        query.eq(SysMenuPermissionGroup::getDisabled, SysMenuPermissionGroup.DISABLED_FLAG_ENABLE);

        return super.list(query);
    }

    /**
     * 设为默认
     * @param id
     */
    @Transactional
    public void setDefault(String id){
        SysMenuPermissionGroup group = super.getById(id);
        if(group == null){
            return;
        }

        LambdaUpdateWrapper<SysMenuPermissionGroup> update = new LambdaUpdateWrapper<>();
        update.set(SysMenuPermissionGroup::getIsDefault, Constant.BOOLEAN_NO);
        super.update(update);

        group.setIsDefault(Constant.BOOLEAN_YES);
        super.updateById(group);
    }

    /**
     * 更新禁用状态
     * @param id
     * @param disabledFlag
     */
    @Transactional
    public void updateDisabled(String id, Integer disabledFlag){
        LambdaUpdateWrapper<SysMenuPermissionGroup> update = new LambdaUpdateWrapper<>();
        update.eq(SysMenuPermissionGroup::getId, id);
        update.set(SysMenuPermissionGroup::getDisabled, disabledFlag);

        super.update(update);
    }
}
