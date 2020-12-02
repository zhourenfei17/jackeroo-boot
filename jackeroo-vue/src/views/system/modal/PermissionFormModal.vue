<template>
  <j-modal
    ref="modal"
    :title="title"
    :width="width"
    :visible="visible"
    :fullscreen.sync="fullscreen"
    :switchFullscreen="showFullscreenBtn"
    :confirmLoading="loading"
    :disabled="flag.view"
    @ok="handleSubmit"
    @cancel="cancel"
  >
    <j-spin :spinning="loading">
      <a-form-model ref="formModel" :model="form" :rules="rules" v-bind="layout">
        <a-row :gutter="formGutter">
          <a-col :span="rowSpan">
            <a-form-model-item label="权限名称" prop="name">
              <a-input v-model="form.name" placeholder="请输入权限名称" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="功能模块" prop="module">
              <j-select 
                v-model="form.module" 
                placeholder="请选择功能模块" 
                :url="url.findModuleList" 
                valueField="code" 
                disabled
                style="width:90%;">
              </j-select>

              <a-tooltip placement="topLeft" overlayClassName="tooltip-content"> 
                <template slot="title">
                  <span>用来拼接权限标识，</span><span class="tooltip-red">规则【功能模块代码:功能名称:权限代码】</span>
                  <br>
                  <span>示例：</span>
                  <br>
                  <span class="tooltip-red">功能模块: 例如系统管理对应的代码为【system】</span>
                  <br>
                  <span class="tooltip-red">功能名称: 例如菜单管理页面填写【menu】</span>
                  <br>
                  <span class="tooltip-red">权限代码: 例如添加的权限代码对应为【add】</span>
                  <br>
                  <span class="tooltip-red">拼接后权限标识: system:menu:add</span>
                </template>
                <a-icon type="exclamation-circle" style="margin-left:20px;"></a-icon>
              </a-tooltip>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="功能名称" prop="function">
              <a-input v-model="form.function" placeholder="请输入功能名称" disabled style="width:90%;"></a-input>

              <a-tooltip placement="topLeft" overlayClassName="tooltip-content"> 
                <template slot="title">
                  <span>用来拼接权限标识，</span><span class="tooltip-red">规则【功能模块代码:功能名称:权限代码】</span>
                  <br>
                  <span>示例：</span>
                  <br>
                  <span class="tooltip-red">功能模块: 例如系统管理对应的代码为【system】</span>
                  <br>
                  <span class="tooltip-red">功能名称: 例如菜单管理页面填写【menu】</span>
                  <br>
                  <span class="tooltip-red">权限代码: 例如添加的权限代码对应为【add】</span>
                  <br>
                  <span class="tooltip-red">拼接后权限标识: system:menu:add</span>
                </template>
                <a-icon type="exclamation-circle" style="margin-left:20px;"></a-icon>
              </a-tooltip>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="权限代码" prop="code">
              <a-input v-model="form.code" placeholder="请输入权限代码" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="排序号" prop="sort">
              <a-input-number v-model="form.sort" placeholder="请输入排序号" :disabled="flag.view" style="width:100%;"></a-input-number>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-spin>
  </j-modal>
</template>

<script>
import {JackerooFormMixins} from '@/mixins/JackerooFormMixins'
import { getAction, httpAction } from '@/api/manage'
import { JSelect } from '@/components';

export default {
  mixins: [JackerooFormMixins],
  components: {
    JSelect
  },
  data(){
    return {
      title: '权限信息',
      width: '40vw',
      form: {
        id: undefined,
        name: undefined,
        permission: undefined,
        sort: undefined,
        parentId: undefined,
        type: 1,
        module: undefined,
        function: undefined,
        code: undefined
      },
      url: {
        getById: '/system/menu/',
        save: '/system/menu/save',
        update: '/system/menu/update',
        getPermissionPrefix: '/system/menu/getPermissionPrefix'
      }
    }
  },
  computed: {
    rules() {
      return {
        name: [
          {required: true, message: '请输入权限名称'}, 
          {max: 20, message: '长度需要在0到20之间'}
        ],
        module: [
          {required: true, message: '请选择功能模块'}
        ],
        function: [
          {required: true, message: '请输入功能名称'}
        ],
        code: [
          {required: true, message: '请输入权限代码'}, 
          {max: 30, message: '长度需要在0到30之间'},
          {validator: this.validLetterAndUnderline}
        ]
      }
    }
  },
  methods: {
    add(parentId, sort){
      this.form.id = null
      this.form.parentId = parentId
      this.form.sort = sort
      this.getPermissionPrefix(parentId)
      this.loading = false
    },
    edit(id){
      getAction(this.url.getById + id).then(result => {
        let permission = result.data.permission.split(':')
        result.data.module = permission[0]
        result.data.function = permission[1]
        result.data.code = permission[2]
        this.copyProperties(result.data, this.form)
      }).finally(() => {
        this.loading = false
      })
    },
    getPermissionPrefix(pid){
      getAction(this.url.getPermissionPrefix, {pid: pid}).then(result => {
        if(!result.code){
          this.form.module = result.data.module
          this.form.function = result.data.function
        }
      })
    },
    handleSubmit(){
      this.$refs.formModel.validate((success) => {
        if(success){
          const formData = {...this.form}
          formData.permission = `${formData.module}:${formData.function}:${formData.code}`
          console.log('formData', formData)

          this.$loading.show()
          this.submit(formData).then(result => {
            if(!result.code){
              this.$message.success('保存成功！')
              this.cancel()
              this.$emit('ok')
            }
          }).finally(() => {
            this.$loading.hide()
          })
        }
      })
    },
  }
}
</script>
<style lang="less" scoped>
.radio-btn{
  width: 50px;
  text-align:center;
}
.tooltip-red{
  color: #ff4545;
}
</style>