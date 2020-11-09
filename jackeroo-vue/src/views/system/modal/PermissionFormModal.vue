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
            <a-form-model-item label="权限标识" prop="permission">
              <a-input v-model="form.permission" placeholder="请输入权限标识" :disabled="flag.view" style="width:90%;"></a-input>
              <a-tooltip title="此处填写完整的权限标识，如：system:user:view"> 
                <a-icon type="exclamation-circle" style="margin-left:20px;"></a-icon>
              </a-tooltip>
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

export default {
  mixins: [JackerooFormMixins],
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
        type: 1
      },
      rules: {
        name: [
          {required: true, message: '请输入权限名称'}, 
          {max: 20, message: '长度需要在0到20之间'}
        ],
        permission: [
          {required: true, message: '请输入权限标识'}, 
          {max: 100, message: '长度需要在0到100之间'}
        ]
      },
      url: {
        getById: '/system/menu/',
        save: '/system/menu/save',
        update: '/system/menu/update'
      }
    }
  },
  methods: {
    add(parentId, sort){
      this.form.id = null
      this.form.parentId = parentId
      this.form.sort = sort
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
</style>