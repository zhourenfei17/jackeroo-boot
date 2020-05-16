package cn.hub.jackeroo.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 用户信息
 */
@Data
@TableName("sys_user")
public class User implements Serializable {
	@TableId
    private Long id;
    private String name;
    private String account;
    private String password;
    private String salt;
    private Integer gender;
    private String phone;
    private String code;
    private String avatar;
    private Integer status;
    private String remark;
    @TableLogic
    private Integer delFlag;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
}