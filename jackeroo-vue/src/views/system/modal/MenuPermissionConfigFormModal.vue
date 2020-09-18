<template>
  <j-modal
    ref="modal"
    :title="title"
    :width="width"
    :visible="visible"
    :fullscreen.sync="fullscreen"
    :switchFullscreen="showFullscreenBtn"
    :confirmLoading="loading"
    @ok="handleSubmit"
    @cancel="cancel"
  >
    <j-spin :spinning="loading">
      <a-form-model ref="formModel" :model="form" :rules="rules" v-bind="layout">
        <a-row :gutter="formGutter">
          <a-col :span="rowSpan">
            <a-form-model-item label="权限名称" prop="label">
              <a-input v-model="form.label" placeholder="请输入权限名称" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="权限标识后缀" prop="value">
              <a-input v-model="form.value" placeholder="请输入权限标识后缀" :disabled="flag.view" style="width:90%;"></a-input>
              <a-tooltip title="此处填写的权限标识不包含权限前缀部分，如：view、add、edit、delete"> 
                <a-icon type="exclamation-circle" style="margin-left:20px;"></a-icon>
              </a-tooltip>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="是否选中" prop="checked">
              <a-radio-group v-model="form.checked" size="small" buttonStyle="solid" :disabled="flag.view">
                <a-radio-button :value="0" class="radio-btn">
                  否
                </a-radio-button>
                <a-radio-button :value="1" class="radio-btn">
                  是
                </a-radio-button>
              </a-radio-group>
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
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'
import { getAction, httpAction } from '@/api/manage'

export default {
  mixins: [JackerooFromMixins],
  data(){
    return {
      title: '权限信息',
      width: '40vw',
      form: {
        id: null,
        label: '',
        value: '',
        checked: 1,
        sort: null,
        groupId: null
      },
      rules: {
        label: [
          {required: true, message: '请输入权限名称'}, 
          {max: 20, message: '长度需要在0到20之间'}
        ],
        value: [
          {required: true, message: '请输入权限标识后缀'}, 
          {max: 30, message: '长度需要在0到30之间'}
        ],
        checked: [
          {required: true, message: '请选择是否选中'}
        ]
      },
      url: {
        getById: '/system/menu/permission/config/',
        add: '/system/menu/permission/config/add',
        update: '/system/menu/permission/config/update'
      }
    }
  },
  methods: {
    add(groupId){
      this.form.id = null
      this.form.groupId = groupId
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
          const formData = this.form
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
<style lang="less" scoped>
.radio-btn{
  width: 50px;
  text-align:center;
}
</style>