package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.exception.JackerooException;
import cn.hub.jackeroo.system.entity.SysRole;
import cn.hub.jackeroo.system.mapper.SysRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 角色信息 服务实现类
 * </p>
 *
 * @author jackeroo
 * @since 2020-05-18
 */
@Service
@RequiredArgsConstructor
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {

    private final SysUserRoleService userRoleService;

    /**
     * 查询数据列表
     * @param role
     * @return
     */
    public List<SysRole> findList(SysRole role){
        return getBaseMapper().findList(role);
    }

    /**
     * 通过角色编码获取角色信息
     * @param roleCode
     * @return
     */
    public SysRole getByCode(String roleCode){
        LambdaQueryWrapper<SysRole> query = new LambdaQueryWrapper<>();
        query.eq(SysRole::getRoleCode, roleCode);

        return super.getOne(query);
    }

    /**
     * 删除角色
     * @param id
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void delete(String ...id){
        for (String roleId : id) {
            SysRole role = super.getById(roleId);
            if(role != null){
                if(userRoleService.existRole(roleId)){
                    throw new JackerooException(String.format("角色【%s】正在使用，无法删除！", role.getRoleName()));
                }
                super.removeById(roleId);
            }
        }
    }
}
