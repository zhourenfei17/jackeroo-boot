package ${scheme.packageName}.${module.code}.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import ${scheme.packageName}.persistence.BaseEntity;
import ${scheme.packageName}.utils.validator.annotation.Unique;
import ${scheme.packageName}.utils.validator.groups.Insert;
import ${scheme.packageName}.utils.validator.groups.Update;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
* <p>
* ${table.comment}
* </p>
*
* @author ${scheme.author}
* @since ${createDate}
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ${table.className} extends BaseEntity<${table.className}> {

    private static final long serialVersionUID = 1L;

<#list columnList as column>
    /**
     * ${column.dbFieldDesc}
     */
    <#if column.primaryKey == 1>
    @TableId
    </#if>
    <#if column.entityFieldType == "LocalDateTime">
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    </#if>
    <#if column.entityFieldType == "LocalDate">
    @JsonFormat(pattern = "yyyy-MM-dd")
    </#if>
    <#if scheme.enableServerValid == 1>
        <#if column.formRequired == 1>
            <#if column.primaryKey == 1>
    @NotNull(groups = Update.class)
    @Null(groups = Insert.class)
            <#elseif column.entityFieldType == "String">
    @NotBlank
            <#else>
    @NotNull
            </#if>
        </#if>
        <#if column.entityFieldType == "String" && column.dbFieldLength??>
    @Length(max = ${column.dbFieldLength})
        </#if>
        <#if column.entityFieldType == "Integer" && column.dbFieldLength = 3>
    @Min(0)
    @Max(128)
        </#if>
    </#if>
    private ${column.entityFieldType} ${column.entityFieldName};

</#list>
}
