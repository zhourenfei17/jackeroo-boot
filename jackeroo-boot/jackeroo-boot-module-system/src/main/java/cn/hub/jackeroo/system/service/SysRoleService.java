package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.exception.JackerooException;
import cn.hub.jackeroo.system.entity.SysRole;
import cn.hub.jackeroo.system.mapper.SysRoleMapper;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 角色信息 服务实现类
 * </p>
 *
 * @author jackeroo
 * @since 2020-05-18
 */
@Service
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {
    @Resource
    private SysRoleMapper mapper;
    @Resource
    private SysUserRoleService userRoleService;
    /**
     * 查询数据列表-带分页
     * @param sysRole
     * @param pageParam
     * @return
     */
    public IPage<SysRole> findPage(SysRole sysRole, PageParam pageParam){
        Page<SysRole> page = sysRole.initPage(pageParam);
        page.setRecords(mapper.findList(sysRole));

        return page;
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
    @Transactional
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
