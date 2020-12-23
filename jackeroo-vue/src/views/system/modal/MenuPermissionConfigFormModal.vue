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
            <a-form-model-item label="权限名称" prop="label">
              <a-input v-model="form.label" placeholder="请输入权限名称" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="权限代码" prop="value">
              <a-input v-model="form.value" placeholder="请输入权限代码" :disabled="flag.view"></a-input>
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
import {JackerooFormMixins} from '@/mixins/JackerooFormMixins'
import { getAction, httpAction } from '@/api/manage'
import { message } from 'ant-design-vue'

export default {
  mixins: [JackerooFormMixins],
  data(){
    return {
      title: '权限信息',
      width: '40vw',
      tableName: 'sys_menu_permission_config',
      form: {
        id: undefined,
        label: undefined,
        value: undefined,
        checked: 1,
        sort: undefined,
        groupId: undefined
      },
      url: {
        getById: '/system/menu/permission/config/',
        save: '/system/menu/permission/config/save',
        update: '/system/menu/permission/config/update',
        getMaxSort: '/system/menu/permission/config/getMaxSort'
      }
    }
  },
  computed: {
    rules() {
      return {
        label: [
          {required: true, message: '请输入权限名称'}, 
          {max: 20, message: '长度需要在0到20之间'}
        ],
        value: [
          {required: true, message: '请输入权限代码'}, 
          {max: 30, message: '长度需要在0到30之间'},
          {validator: this.validLetterAndUnderline},
          {validator: this.validUnique, condition: 'group_id=' + this.form.groupId, trigger: 'blur', message: '该权限代码已存在'}
        ],
        checked: [
          {required: true, message: '请选择是否选中'}
        ],
        sort: [
          {required: true, message: '请填写排序号'}
        ]
      }
    }
  },
  methods: {
    add(groupId){
      this.form.id = null
      this.form.groupId = groupId
      this.getMaxSort()
      this.loading = false
    },
    edit(id){
      getAction(this.url.getById + id).then(result => {
        this.copyProperties(result.data, this.form)
      }).finally(() => {
        this.loading = false
      })
    },
    getMaxSort(){
      getAction(this.url.getMaxSort, {groupId: this.form.groupId}).then(result => {
        if(!result.code){
          this.form.sort = result.data
        }
      })
    },
    handleSubmit(){
      this.$refs.formModel.validate((success) => {
        if(success){
          const formData = {...this.form}
          console.log('formData', formData)

          this.$loading.show()
          httpAction(this.requestUrl, formData, this.requestMethod).then(result => {
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