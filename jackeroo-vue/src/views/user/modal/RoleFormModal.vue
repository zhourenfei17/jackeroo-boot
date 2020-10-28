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
            <a-form-model-item label="角色名" prop="roleName">
              <a-input v-model="form.roleName" placeholder="请输入角色名" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="角色代码" prop="roleCode">
              <a-input v-model="form.roleCode" placeholder="请输入角色代码" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="备注" prop="remark">
              <a-input v-model="form.remark" placeholder="请输入备注" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-spin>
  </j-modal>
</template>

<script>
import { getAction, postAction, httpAction } from '@/api/manage'
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'

export default {
  mixins: [JackerooFromMixins],
  data(){
    return {
      title: '用户信息',
      tableName: 'sys_role',
      width: '40vw',
      form: {
        id: null,
        roleName: '',
        roleCode: '',
        remark: ''
      },
      rules: {
        roleName: [
          {required: true, message: '请输入角色名'}, 
          {validator: this.validUnique, message: '角色名不能重复', trigger: 'blur'},
          {max: 20, message: '长度需要在0到20之间'}
        ],
        roleCode: [
          {required: true, message: '请输入角色代码'}, 
          {validator: this.validUnique, message: '角色代码不能重复', trigger: 'blur'},
          {max: 30, message: '长度需要在0到30之间'}
        ],
        remark: [{max: 100, message: '长度需要在0到100之间'}],
      },
      url: {
        getById: '/system/role/',
        save: '/system/role/save',
        update: '/system/role/update'
      }
    }
  },
  methods: {
    add(){
      this.form.id = null
      this.loading = false
    },
    edit(id){
      getAction(this.url.getById + id).then(result => {
        this.copyProperties(result.data, this.form)
      }).finally(() => {
        this.loading = false
      })
    },
    handleSubmit(){
      this.$refs.formModel.validate((success) => {
        if(success){
          const formData = {...this.form}
          console.log('formData', formData)

          this.$loading.show()
          httpAction(this.requestUrl, formData, this.requestMethod).then(result => {
            if(result.code === 0){
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
