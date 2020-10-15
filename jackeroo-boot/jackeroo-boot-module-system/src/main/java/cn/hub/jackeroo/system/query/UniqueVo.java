package cn.hub.jackeroo.system.query;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 唯一性校验参数对象
 * @author alex
 * @date 2020/06/29
 */
@Data
public class UniqueVo {
    /**
     * 表名
     */
    @NotBlank
    private String tableName;
    /**
     * 列名
     */
    @NotBlank
    private String columnName;
    /**
     * 数据值
     */
    @NotBlank
    private String dataValue;
    /**
     * 数据id
     */
    private String dataId;
    /**
     * 自定义条件
     */
    private String condition;
    /**
     * 自定义条件中的参数，json字符串
     */
    private String param;
}
