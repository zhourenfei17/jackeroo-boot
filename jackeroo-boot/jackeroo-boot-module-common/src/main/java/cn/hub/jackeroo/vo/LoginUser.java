package cn.hub.jackeroo.vo;


/**
 * @author alex
 * @date 2020/05/13
 */
public class LoginUser extends IUser {

    private Long roleId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
