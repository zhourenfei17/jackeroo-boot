package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.api.ISystemApi;
import cn.hub.jackeroo.constant.RedisKeyPrefix;
import cn.hub.jackeroo.system.entity.SysRole;
import cn.hub.jackeroo.system.entity.SysRoleMenu;
import cn.hub.jackeroo.system.mapper.SysRoleMenuMapper;
import cn.hub.jackeroo.system.query.RolePermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色菜单 服务实现类
 * </p>
 *
 * @author jackeroo
 * @since 2020-05-18
 */
@Service
public class SysRoleMenuService extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> {

    @Autowired
    private ISystemApi systemApi;
    /**
     * 根据角色id获取拥有的菜单、权限列表
     * @param roleId
     * @return
     */
    public List<Long> findRoleMenuByRoleId(Long roleId){
        LambdaQueryWrapper<SysRoleMenu> query = new LambdaQueryWrapper<>();
        query.eq(SysRoleMenu::getRoleId, roleId);

        return super.list(query).stream().map(item -> item.getMenuId()).collect(Collectors.toList());
    }

    /**
     * 保存角色权限配置
     * @param rolePermission
     */
    @CacheEvict(value = RedisKeyPrefix.CACHE_MENU, key = "#role.roleCode")
    @Transactional
    public void saveRolePermission(RolePermission rolePermission, SysRole role){
        // 全删全插
        LambdaQueryWrapper<SysRoleMenu> query = new LambdaQueryWrapper<>();
        query.eq(SysRoleMenu::getRoleId, role.getId());
        super.remove(query);

        List<SysRoleMenu> roleMenuList = rolePermission.getPermissionList()
                .stream()
                .map(item -> {
                    SysRoleMenu roleMenu = new SysRoleMenu();
                    roleMenu.setRoleId(role.getId());
                    roleMenu.setMenuId(Long.parseLong(item));

                    return roleMenu;
                })
                .collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(roleMenuList)){
            super.saveBatch(roleMenuList, 100);
        }
        //清除当前用户缓存
        systemApi.clearAuthorizationCache();
    }
}
