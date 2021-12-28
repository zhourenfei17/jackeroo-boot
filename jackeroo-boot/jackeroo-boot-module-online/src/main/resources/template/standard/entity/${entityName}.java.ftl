package ${scheme.packageName}.${module.code}.entity;

<#if existBigDecimal>
import java.math.BigDecimal;
</#if>
<#if existLocalDateTime>
import java.time.LocalDateTime;
</#if>
<#if existLocalDate>
import java.time.LocalDate;
</#if>

<#if existValidRules && scheme.enableServerValid == 1>
import cn.hub.jackeroo.enums.ValidRuleType;
</#if>
import ${scheme.packageName}.persistence.BaseEntity;
<#if existDict && scheme.enableServerValid == 1>
import cn.hub.jackeroo.utils.validator.annotation.Dict;
</#if>
<#if existUnique && scheme.enableServerValid == 1>
import ${scheme.packageName}.utils.validator.annotation.Unique;
</#if>
<#if existValidRules && scheme.enableServerValid == 1>
import cn.hub.jackeroo.utils.validator.annotation.ValidRules;
</#if>
import ${scheme.packageName}.utils.validator.groups.Insert;
import ${scheme.packageName}.utils.validator.groups.Update;
<#if existFieldFill>
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
</#if>
<#if !existFieldFill && existBetween>
import com.baomidou.mybatisplus.annotation.TableField;
</#if>
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
<#if existLocker>
import com.baomidou.mybatisplus.annotation.Version;
</#if>
<#if existLogic>
import com.baomidou.mybatisplus.annotation.TableLogic;
</#if>
<#if existLocalDateTime || existLocalDate>
import com.fasterxml.jackson.annotation.JsonFormat;
</#if>
<#if scheme.enableSwagger == 1>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
<#if existLength  && scheme.enableServerValid == 1>
import org.hibernate.validator.constraints.Length;
</#if>
<#if existRange  && scheme.enableServerValid == 1>
import org.hibernate.validator.constraints.Range;
</#if>
<#if existUrl  && scheme.enableServerValid == 1>
import org.hibernate.validator.constraints.URL;
</#if>
<#if existEmail  && scheme.enableServerValid == 1>
import javax.validation.constraints.Email;
</#if>
<#if existDigits  && scheme.enableServerValid == 1>
import javax.validation.constraints.Digits;
</#if>
<#if existNotEmpty  && scheme.enableServerValid == 1>
import javax.validation.constraints.NotBlank;
</#if>
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
<#if scheme.enableSwagger == 1>
@ApiModel(value = "${table.comment}")
</#if>
@TableName("${table.tableName}")
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
    <#if column.locker == 1>
    @Version
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
    @Range(min = 0, max = 128)
        </#if>
        <#if column.formDictCode?? && column.formDictCode != "">
    @Dict(dictCode = "${column.formDictCode}")
        </#if>
        <#if column.formValidator?? && column.formValidator != ''>
            <#if column.formValidator?contains('validMobile')>
    @ValidRules(type = ValidRuleType.VALID_MOBILE, message = "手机号不正确")
            </#if>
            <#if column.formValidator?contains('validPhone')>
    @ValidRules(type = ValidRuleType.VALID_TELEPHONE, message = "电话号不正确")
            </#if>
            <#if column.formValidator?contains('validUnique')>
    @Unique(name = "${column.dbFieldDesc}")
            </#if>
            <#if column.formValidator?contains('validIdNumber')>
    @ValidRules(type = ValidRuleType.VALID_ID_CARD, message = "身份证号不正确")
            </#if>
            <#if column.formValidator?contains('validWebsite')>
    @URL
            </#if>
            <#if column.formValidator?contains('validEmail')>
    @Email
            </#if>
            <#if column.formValidator?contains('validPostCode')>
    @ValidRules(type = ValidRuleType.VALID_POSTCODE, message = "邮编不正确")
            </#if>
            <#if column.formValidator?contains('validLetterAndUnderline')>
    @ValidRules(type = ValidRuleType.VALID_LETTER_AND_UNDERLINE, message = "只能输入字母和下划线")
            </#if>
        </#if>
    </#if>
    <#if column.fillStrategy?? && column.fillStrategy != ''>
    @TableField(fill = FieldFill.${column.fillStrategy})
    </#if>
    <#if scheme.enableSwagger == 1>
    @ApiModelProperty(value = "${column.dbFieldDesc}")
    </#if>
    private ${column.entityFieldType} ${column.entityFieldName};

</#list>
<#list searchList as column>
    <#if (column.formType == 'date' || column.formType == 'dateTime') && column.queryType == 'Between'>

    /**
     * ${column.dbFieldDesc} - 开始时间
     */
    @TableField(exist = false)
    private String ${column.entityFieldName}Begin;

    /**
     * ${column.dbFieldDesc} - 结束时间
     */
    @TableField(exist = false)
    private String ${column.entityFieldName}End;
    </#if>
</#list>
}
