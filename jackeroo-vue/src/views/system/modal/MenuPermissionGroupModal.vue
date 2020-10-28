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
            <a-form-model-item label="权限组名" prop="groupName">
              <a-input v-model="form.groupName" placeholder="请输入权限组名" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="默认组别" prop="isDefault">
              <a-radio-group v-model="form.isDefault" size="small" buttonStyle="solid" :disabled="flag.view">
                <a-radio-button :value="0" class="radio-btn">
                  否
                </a-radio-button>
                <a-radio-button :value="1" class="radio-btn">
                  是
                </a-radio-button>

                <a-tooltip title="只能有一个默认权限分组，设置默认后，其他的将自动取消默认"> 
                  <a-icon type="exclamation-circle" style="margin-left:20px;"></a-icon>
                </a-tooltip>
                
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="禁用状态" prop="disabled">
              <a-radio-group v-model="form.disabled" size="small" buttonStyle="solid" :disabled="flag.view">
                <a-radio-button :value="0" class="radio-btn">
                  启用
                </a-radio-button>
                <a-radio-button :value="1" class="radio-btn">
                  禁用
                </a-radio-button>
              </a-radio-group>
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
      tableName: 'sys_menu_permission_group',
      width: '40vw',
      form: {
        id: null,
        groupName: '',
        isDefault: 0,
        disabled: 0
      },
      rules: {
        groupName: [
          {required: true, message: '请输入权限组名'}, 
          {validator: this.validUnique, message: '权限组名不能重复', trigger: 'blur'},
          {max: 20, message: '长度需要在0到20之间'}
        ],
        isDefault: [
          {required: true, message: '请设置是否默认'}, 
        ],
        disabled: [{required: true, message: '请设置禁用状态'}],
      },
      url: {
        getById: '/system/menu/permission/group/',
        save: '/system/menu/permission/group/save',
        update: '/system/menu/permission/group/update'
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