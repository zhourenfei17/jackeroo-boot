<template>
  <div><#if existQuery><#assign existJDictSelect = false /><#assign existJSelect = false />
    <search-card :enter="refreshData">
        <#list searchList as column>
            <#if column_index < 3>
      <a-col :md="${((column.formType == 'date' || column.formType == 'dateTime') && column.queryType == 'Between')?string('12','6')}" :sm="12">
        <a-form-item label="${column.dbFieldDesc}">
                <#if column.formType == "input" || column.formType == 'textarea'>
                    <#if column.entityFieldType == 'Integer' || column.entityFieldType == 'Double' || column.entityFieldType == 'Float' || column.entityFieldType == 'BigDecimal'>
          <a-input-number v-model="queryParam.${column.entityFieldName}" placeholder="请输入${column.dbFieldDesc}" precision="${column.dbFieldDecimal}"></a-input-number>
                    <#else>
          <a-input v-model="queryParam.${column.entityFieldName}" placeholder="请输入${column.dbFieldDesc}"></a-input>
                    </#if>
                <#elseif column.formType == 'select' || column.formType == 'multiple_select' || column.formType == 'radio' || column.formType == 'checkbox'>
                    <#if column.formDictCode?? && column.formDictCode != ''>
                        <#assign existJDictSelect = true />
          <j-dict-select
            v-model="queryParam.${column.entityFieldName}"
            placeholder="请选择${column.dbFieldDesc}"
            dictCode="${column.formDictCode}"<#if column.formType == 'multiple_select'>
            multi</#if>
            >
          </j-dict-select>
                    <#elseif column.formType =='select' || column.formType =='multiple_select'>
                        <#assign existJSelect = true />
          <j-select
            v-model="queryParam.${column.entityFieldName}"
            placeholder="请选择${column.dbFieldDesc}"
            url=""
            textField=""
            valueField=""<#if column.formType == 'multiple_select'>
            multi</#if>
            >
          </j-select>
                    </#if>
                <#elseif column.formType == 'date' || column.formType == 'datetime'>
                    <#if column.queryType == 'Between'>
          <a-date-picker
            v-model="queryParam.${column.entityFieldName}Begin"
            placeholder="请选择${column.dbFieldDesc}"<#if column.formType == 'dateTime'>
            mode="time"</#if>
            valueFormat="${(column.formType == 'date')?string('YYYY-MM-DD', 'YYYY-MM-DD HH:mm:ss')}"
            style="width:45%">
          </a-date-picker>
          <a-date-picker
            v-model="queryParam.${column.entityFieldName}End"
            placeholder="请选择${column.dbFieldDesc}"<#if column.formType == 'dateTime'>
            mode="time"</#if>
            valueFormat="${(column.formType == 'date')?string('YYYY-MM-DD', 'YYYY-MM-DD HH:mm:ss')}"
            style="width:45%;margin-left: 10%;">
          </a-date-picker>
                    <#else>
          <a-date-picker
            v-model="queryParam.${column.entityFieldName}"
            placeholder="请选择${column.dbFieldDesc}"<#if column.formType == 'dateTime'>
            mode="time"</#if>
            valueFormat="${(column.formType == 'date')?string('YYYY-MM-DD', 'YYYY-MM-DD HH:mm:ss')}"
            style="width:100%">
          </a-date-picker>
                    </#if>
                </#if>
        </a-form-item>
      </a-col>
            </#if>
        </#list>

        <#if searchList?size &gt; 3>
      <template slot="more">
            <#list searchList as column>
                <#if column_index &gt;= 3>
        <a-col :md="${((column.formType == 'date' || column.formType == 'dateTime') && column.queryType == 'Between')?string('12','6')}" :sm="12">
          <a-form-item label="${column.dbFieldDesc}">
            <#if column.formType == "input" || column.formType == 'textarea'>
                <#if column.entityFieldType == 'Integer' || column.entityFieldType == 'Double' || column.entityFieldType == 'Float' || column.entityFieldType == 'BigDecimal'>
            <a-input-number v-model="queryParam.${column.entityFieldName}" placeholder="请输入${column.dbFieldDesc}" precision="${column.dbFieldDecimal}"></a-input-number>
                <#else>
            <a-input v-model="queryParam.${column.entityFieldName}" placeholder="请输入${column.dbFieldDesc}"></a-input>
                </#if>
            <#elseif column.formType == 'select' || column.formType == 'multiple_select' || column.formType == 'radio' || column.formType == 'checkbox'>
                <#if column.formDictCode?? && column.formDictCode != ''>
                    <#assign existJDictSelect = true />
            <j-dict-select
              v-model="queryParam.${column.entityFieldName}"
              placeholder="请选择${column.dbFieldDesc}"
              dictCode="${column.formDictCode}"<#if column.formType == 'multiple_select'>
              multi</#if>
              >
            </j-dict-select>
                <#elseif column.formType =='select' || column.formType =='multiple_select'>
                    <#assign existJSelect = true />
            <j-select
              v-model="queryParam.${column.entityFieldName}"
              placeholder="请选择${column.dbFieldDesc}"
              url=""
              textField=""
              valueField=""<#if column.formType == 'multiple_select'>
              multi</#if>
              >
            </j-select>
                </#if>
            <#elseif column.formType == 'date' || column.formType == 'datetime'>
                <#if column.queryType == 'Between'>
            <a-date-picker
              v-model="queryParam.${column.entityFieldName}Begin"
              placeholder="请选择${column.dbFieldDesc}"<#if column.formType == 'dateTime'>
              mode="time"</#if>
              valueFormat="${(column.formType == 'date')?string('YYYY-MM-DD', 'YYYY-MM-DD HH:mm:ss')}"
              style="width:45%">
            </a-date-picker>
            <a-date-picker
              v-model="queryParam.${column.entityFieldName}End"
              placeholder="请选择${column.dbFieldDesc}"<#if column.formType == 'dateTime'>
              mode="time"</#if>
              valueFormat="${(column.formType == 'date')?string('YYYY-MM-DD', 'YYYY-MM-DD HH:mm:ss')}"
              style="width:45%;margin-left: 10%;">
            </a-date-picker>
                <#else>
            <a-date-picker
              v-model="queryParam.${column.entityFieldName}"
              placeholder="请选择${column.dbFieldDesc}"<#if column.formType == 'dateTime'>
              mode="time"</#if>
              valueFormat="${(column.formType == 'date')?string('YYYY-MM-DD', 'YYYY-MM-DD HH:mm:ss')}"
              style="width:100%"
              :disabled="flag.view">
            </a-date-picker>
                </#if>
            </#if>
          </a-form-item>
        </a-col>
                </#if>
            </#list>
      </template>
        </#if>

      <template slot="operate">
        <a-button type="primary" @click="refreshData(true)">查询</a-button>
        <a-button style="margin-left: 8px" @click="reset">重置</a-button>
      </template>
    </search-card></#if>

    <data-card
      :reload="refreshData"
      :tableSize.sync="tableSize"
      :columns.sync="columns"
      :tableAlign="tableAlign">

      <template slot="toolbar">
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="handleDeleteBatch"><a-icon type="delete" />删除</a-menu-item>
          </a-menu>
          <a-button style="margin-right: 8px">
            批量操作 <a-icon type="down" />
          </a-button>
        </a-dropdown>
        <a-button type="primary" icon="plus"<#if scheme.enableSecurity == 1> v-action="'${module.code}:${scheme.securitySign}:add'"</#if> @click="handleAdd">新建</a-button>
        <a-dropdown-button style="margin-left: 8px">
          <a-icon type="file-excel"></a-icon>
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="handleExport"><a-icon type="export" />导出</a-menu-item>
            <a-menu-item key="2"><a-icon type="import" />导入</a-menu-item>
          </a-menu>
          <a-icon type="down" slot="icon"></a-icon>
        </a-dropdown-button>
      </template>

      <s-table
        ref="table"
        :size="tableSize"
        rowKey="id"
        :columns="columns"
        :data="loadData"
        :alert="tableAlert"
        :rowSelection="rowSelection"
        :showPagination="showPagination"
        >
        <template slot="action" slot-scope="text, record">
          <action-list>
            <a :type="actionType.view" :icon="actionIcon.view" @click="handleView(record)">详情</a>
            <a :type="actionType.edit" :icon="actionIcon.edit"<#if scheme.enableSecurity == 1> v-action="'${module.code}:${scheme.securitySign}:update'"</#if> @click="handleEdit(record)">编辑</a>
            <a :type="actionType.delete" :icon="actionIcon.delete"<#if scheme.enableSecurity == 1> v-action="'${module.code}:${scheme.securitySign}:delete'"</#if> @click="handleDelete(record)">删除</a>
          </action-list>
        </template>
      </s-table>
    </data-card>

    <${componentName}-form-modal ref="formModal" @ok="handleOk"></${componentName}-form-modal>
  </div>
</template>-

<script>
import { STable, DataCard<#if existQuery>, SearchCard</#if><#if existJSelect>, JSelect</#if><#if existJDictSelect>, JDictSelect</#if>} from '@/components'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import ${table.className}FormModal from './modal/${table.className}FormModal'

export default {
  components: {
    STable,
    DataCard,
  <#if existQuery>
    SearchCard,
  </#if>
  <#if existJSelect>
    JSelect,
  </#if>
  <#if existJDictSelect>
    JDictSelect,
  </#if>
    ${table.className}FormModal
  },
  mixins:[JackerooListMixins],
  data () {
    return {
      columns: [
        {
          title: '#',
          customRender: (text, record, index) => {
            return index + 1
          }
        },
        <#list columnList as column>
            <#if column.enableList == 1>
        {
          title: '${column.dbFieldDesc}',
          dataIndex: '${column.entityFieldName}',
                <#if column.enableSort == 1>
          sort: true,
                    <#if column.dbFieldName == table.sortColumn>
          defaultSortOrder: '${(table.sortType == "asc")?string('ascend', 'descend')}'
                    </#if>
                </#if>
                <#if column.formDictCode?? && column.formDictCode != ''>
          customRender: (text) => {
            return this.loadDictText(text, this.dictOptions.${column.entityFieldName})
          }
                </#if>
        },
            </#if>
        </#list>
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/${module.code}/${pathName}/list',
        delete: '/${module.code}/${pathName}/delete',
        deleteBatch: '/${module.code}/${pathName}/deleteBatch',
        exportExcel: '/${module.code}/${pathName}/exportExcel'
      }<#if tableDictList?? && tableDictList?size &gt; 0>,
      dictOptions: {
          <#list tableDictList as dict>
        // ${dict.dbFieldDesc}
        ${dict.entityFieldName}: {
          code: '${dict.formDictCode}',
          options: []
        }
          </#list>
      }
      </#if>
    }
  },
  methods: {

  }
}
</script>