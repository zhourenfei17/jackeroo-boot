package cn.hub.jackeroo.system.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import cn.hub.jackeroo.persistence.BaseEntity;
import cn.hub.jackeroo.utils.validator.groups.Insert;
import cn.hub.jackeroo.utils.validator.groups.Update;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
* <p>
* 系统操作日志
* </p>
*
* @author alex
* @since 2021-10-11
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_operate_log")
public class SysOperateLog extends BaseEntity<SysOperateLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 操作主体内容
     */
    private String operateContent;

    /**
     * 操作类型，0：登录，1：退出，2：添加，3：修改，4：删除
     */
    private Integer operateType;

    /**
     * 操作人
     */
    private String operateUser;

    /**
     * 请求ip地址
     */
    private String requestIp;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求地址
     */
    private String requestUri;

    /**
     * 请求参数体
     */
    private String requestParams;

    /**
     * 执行时长
     */
    private Long executeTime;

    /**
     * 返回状态，0：成功，1：失败
     */
    private Integer responseStatus;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public static final int RESPONSE_STATUS_SUCCESS = 0;
    public static final int RESPONSE_STATUS_FAIL = 1;
}
