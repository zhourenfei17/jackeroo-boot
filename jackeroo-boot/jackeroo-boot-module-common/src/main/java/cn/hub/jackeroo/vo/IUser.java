package cn.hub.jackeroo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author alex
 * @date 2020/05/13
 */
public abstract class IUser implements Serializable {

    private Long id;
    private String code;		// 员工号
    private String name;		// 姓名
    private String phone;		// 手机号
    private String account;		// 登录账号
    private String password;		// 登录密码
    private String headImg;		// 头像
    private Long managerId;		// 主管id
    private Integer nature;		// 人员性质，0：管理，1：专员
    private String remark;		// 备注
    private Integer flag;		// 标识（0:正常 1:删除）
    private Date createTime;		// 创建时间
    private Date updateTime;		// 更新时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Length(min=0, max=50, message="员工号长度必须介于 0 和 50 之间")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Length(min=1, max=50, message="姓名长度必须介于 1 和 50 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min=1, max=50, message="手机号长度必须介于 1 和 50 之间")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Length(min=1, max=50, message="登录账号长度必须介于 1 和 50 之间")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Length(min=0, max=50, message="登录密码长度必须介于 0 和 50 之间")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Length(min=0, max=250, message="头像长度必须介于 0 和 250 之间")
    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    @NotNull(message="主管id不能为空")
    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Integer getNature() {
        return nature;
    }

    public void setNature(Integer nature) {
        this.nature = nature;
    }

    @Length(min=0, max=50, message="备注长度必须介于 0 和 50 之间")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
