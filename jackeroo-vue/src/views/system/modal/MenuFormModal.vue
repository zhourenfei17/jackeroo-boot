<template>
  <j-drawer
      :title="title"
      :width="width"
      :visible.sync="visible"
      @ok="handleSubmit"
    >
    
    <j-spin :spinning="loading">
      <a-form-model ref="formModel" :model="form" :rules="rules" v-bind="layout">
        <a-row :gutter="24">
          <a-col :span="rowSpan">
            <a-form-model-item label="上级菜单" prop="parentId">
              <a-input v-model="form.parentId" placeholder="请选择上级菜单" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="菜单名称" prop="name">
              <a-input v-model="form.name" placeholder="请输入菜单名称" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="url路径" prop="href">
              <a-input v-model="form.href" placeholder="请输入url路径" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="组件路径" prop="component">
              <a-input v-model="form.component" placeholder="请输入组件路径" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="图标" prop="role">
              <a-input placeholder="点击选择图标" v-model="form.icon" :readOnly="true">
                <a-icon slot="addonAfter" type="setting" @click="selectIcons" />
              </a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="性别" prop="gender">
              <a-select v-model="form.gender" placeholder="请选择性别" :disabled="flag.view">
                <a-select-option value="">请选择</a-select-option>
                <a-select-option :value="1">男</a-select-option>
                <a-select-option :value="2">女</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="手机" prop="phone">
              <a-input v-model="form.phone" placeholder="请输入手机" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="座机" prop="telephone">
              <a-input v-model="form.telephone" placeholder="请输入座机" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="生日" prop="birthday">
              <a-date-picker v-model="form.birthday" placeholder="请选择生日" valueFormat="YYYY-MM-DD" style="width:100%" :disabled="flag.view"></a-date-picker>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-spin>

    <!-- 选择图标 -->
    <a-modal :visible="iconVisible" title="选择图标" width="50vw" @cancel="handleIconCancel" @ok="handleIconOk">
      <icon-selector @change="handleIconChange" :value="tempIconValue"></icon-selector>
    </a-modal>

    <!-- 页脚 button -->
    <!-- <div :style="{
          position: 'absolute',
          right: 0,
          bottom: 0,
          width: '100%',
          borderTop: '1px solid #e9e9e9',
          padding: '10px 16px',
          background: '#fff',
          textAlign: 'right',
          zIndex: 1,
        }">
        <a-button :style="{ marginRight: '8px' }" @click="cancel">
          取消
        </a-button>
        <a-button type="primary" @click="handleSubmit">
          保存
        </a-button>
      </div> -->
  </j-drawer>
</template>

<script>
import { getAction, postAction, httpAction } from '@/api/manage'
import md5 from 'md5'
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'
import JSelect from '@/components/jackeroo/JSelect'
import {IconSelector, JDrawer} from '@/components'

export default {
  components: {
    JSelect,
    IconSelector,
    JDrawer
  },
  mixins: [JackerooFromMixins],
  data(){
    return {
      title: '用户信息',
      tableName: 'sys_user',
      width: '40vw',
      iconVisible: false,
      form: {
        parentId: null,
        name: '',
        href: '',
        component: '',
        icon: '',
        passwordAgain: '',
        gender: undefined,
        phone: '',
        telephone: '',
        birthday: undefined,
        role: undefined
      },
      rules: {
        parentId: [
          {required: true, message: '请选择上级菜单'}
        ],
        name: [
          {required: true, message: '请输入姓名'},
          {max: 20, message: '长度需要在0和20之间'}
        ],
        href: [
          {required: true, message: '请输入账号'},
          {max: 30, message: '长度需要在0和30之间'}
        ],
        component: [{max: 60, message: '长度需要在0到60之间'}],
        icon: [],
        passwordAgain: [
          {required: true, message: '请再次输入密码'}, 
          {validator:(rule, value, callback)=>{
            return value != this.form.password ? callback('两次输入的密码不一致') : callback()
          }, trigger: 'blur'}
        ],
        gender: [],
        phone: [
          {required: true, message: '请输入手机'},
          {validator: this.validMobile, trigger: 'blur'},
          {validator: this.validUnique, message: '该手机号已存在', trigger: 'blur'}
        ],
        telephone: [{validator: this.validPhone, trigger: 'blur'}],
        birthday: []
      },
      url: {
        getById: '/system/user/',
        add: '/system/user/add',
        update: '/system/user/update'
      },
      tempIconValue: ''
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
          if(!this.md5Flag && this.flag.add){
            formData.password = md5(formData.password)
            formData.passwordAgain = md5(formData.passwordAgain)

            this.md5Flag = true
          }else if(this.flag.edit){
            delete formData.password
          }
          
          console.log('formData', formData)

          this.$loading.show()
          httpAction(this.requestUrl, formData, this.requestMethod).then(result => {
            if(result.code === 0){
              this.$message.success('保存成功！')
              this.md5Flag = false
              this.cancel()
              this.$emit('ok')
            }
          }).finally(() => {
            this.$loading.hide()
          })
        }
      })
    },
    selectIcons(){
      this.iconVisible = true
    },
    handleIconCancel(){
      this.iconVisible = false
    },
    handleIconOk(){
      this.form.icon = this.tempIconValue
      this.iconVisible = false
    },
    handleIconChange(icon){
      this.tempIconValue = icon
    }
  }
}
</script>