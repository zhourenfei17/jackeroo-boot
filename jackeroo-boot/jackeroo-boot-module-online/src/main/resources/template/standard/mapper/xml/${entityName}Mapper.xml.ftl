<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${scheme.packageName}.${module.code}.mapper.${table.className}Mapper">

    <!-- 通用查询结果列 -->
    <sql id="baseColumns">
        <#list columnList as column>
            <#if column_index != (columnList?size - 1)>
        a.${column.dbFieldName} as "${column.entityFieldName}",
            <#else>
        a.${column.dbFieldName} as "${column.entityFieldName}"
            </#if>
        </#list>
    </sql>

    <select id="findList" resultType="${scheme.packageName}.${module.code}.entity.${table.className}">
        select
            <include refid="baseColumns"/>
        from ${table.tableName} a
        <where>
            <#list searchList as column>
                <#if column.entityFieldType == "String">
            <if test="${column.entityFieldName} != null and ${column.entityFieldName} != ''">
                    <#if column.queryType == "Like">
                and a.${column.dbFieldName} like CONCAT('%', ${'#'}{${column.entityFieldName}}, '%')
                    <#else>
                and a.${column.dbFieldName} ${column.queryType} ${'#'}{${column.entityFieldName}}
                     </#if>
            </if>
                <#elseif column.entityFieldType == 'LocalDateTime' || column.entityFieldType == 'LocalDate'>
                    <#if column.queryType == "Between">
            <if test="${column.entityFieldName}Begin != null"
                and a.${column.dbFieldName} &gt;= ${'#'}{${column.entityFieldName}Begin}
            </if>
            <if test="${column.entityFieldName}End != null"
                and a.${column.dbFieldName} &lt;= ${'#'}{${column.entityFieldName}End}
            </if>
                    <#else>
            <if test="${column.entityFieldName} != null"
                and a.${column.dbFieldName} ${column.queryType} ${'#'}{${column.entityFieldName}}
            </if>
                    </#if>
                <#else>
            <if test="${column.entityFieldName} != null">
                and a.${column.dbFieldName} ${column.queryType} ${'#'}{${column.entityFieldName}}
            </if>
                </#if>
            </#list>
        </where>
        <choose>
            <when test="orderBy != null and orderBy != ''">
                order by ${'$'}{orderBy}
            </when>
            <otherwise>
                <#if table.sortColumn?? && table.sortColumn != "">
                order by a.${table.sortColumn} ${table.sortType}
                </#if>
            </otherwise>
        </choose>
    </select>

</mapper>
