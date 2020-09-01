<template>
  <j-modal
    ref="modal"
    :title="title"
    :width="width"
    :visible="visible"
    :fullscreen.sync="fullscreen"
    :switchFullscreen="false"
    okText="确定"
    @ok="handleSubmit"
    @cancel="cancel"
  >
    <a-form-model ref="formModel" :model="form" :rules="rules" v-bind="layout">
      <a-row :gutter="24">
        <a-col :span="24">
          <a-form-model-item label="权限分组" prop="groupId">
            <j-select v-model="form.groupId" placeholder="请选择权限分组" :url="url.findAllPermissionSelect" textField="groupName"></j-select>
          </a-form-model-item>
        </a-col>
      </a-row>
    </a-form-model>
  </j-modal>
</template>

<script>
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'
import JSelect from '@/components/jackeroo/JSelect'

export default {
  mixins: [JackerooFromMixins],
  components:{
    JSelect
  },
  data(){
    return {
      title: '选择权限分组',
      width: '400px',
      form: {
        groupId: undefined,
      },
      rules: {
        groupId: [
          {required: true, message: '请选择权限分组'}
        ],
      },
      url: {
        findAllPermissionSelect: '/system/menu/permission/group/findAll'
      }
    }
  },
  methods: {
    select(groupId){
      this.form.groupId = groupId
      this.visible = true
    },
    handleSubmit(){
      this.$refs.formModel.validate((success) => {
        if(success){
          this.$emit('ok', this.form.groupId)
          this.cancel()
        }
      })
    },
  }
}
</script>
