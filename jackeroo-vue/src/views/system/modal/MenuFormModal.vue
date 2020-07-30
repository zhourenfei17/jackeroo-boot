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
              <a-tree-select v-model="form.parentId" placeholder="请选择上级菜单" :disabled="flag.view" :treeData="treeData" style="width:100%;" 
                :dropdownStyle="{maxHeight: '200px', overflow: 'auto'}">
                
              </a-tree-select>
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
            <a-form-model-item label="图标" prop="icon">
              <a-input placeholder="点击选择图标" v-model="form.icon" :readOnly="true">
                <a-icon slot="addonAfter" type="setting" @click="selectIcons" />
              </a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="排序号" prop="sort">
              <a-input-number v-model="form.sort" placeholder="请输入排序号" :disabled="flag.view" style="width:100%;" :min="0"></a-input-number>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="目标" prop="target">
              <a-radio-group v-model="form.target" :disabled="flag.view">
                <a-radio :value="1">内部</a-radio>
                <a-radio :value="2">外部</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-spin>

    <!-- 选择图标 -->
    <a-modal :visible="iconVisible" title="选择图标" width="50vw" @cancel="handleIconCancel" @ok="handleIconOk">
      <icon-selector @change="handleIconChange" :value="tempIconValue"></icon-selector>
    </a-modal>
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
        sort: null,
        target: 1
      },
      rules: {
        parentId: [
          {required: true, message: '请选择上级菜单'}
        ],
        name: [
          {required: true, message: '请输入菜单名称'},
          {max: 20, message: '长度需要在0和20之间'}
        ],
        href: [
          {required: true, message: '请输入url路劲'},
          {max: 100, message: '长度需要在0和30之间'}
        ],
        component: [
          {required: true, message: '请输入组件路劲'},
          {max: 100, message: '长度需要在0到100之间'}
        ],
        icon: [],
        sort: [{min: 0, max: 999999, message: '长度需要在0到6之间', type: 'number'}],
        target: [
          {required: true, message: '请输入手机'}
        ]
      },
      url: {
        getById: '/system/user/',
        add: '/system/user/add',
        update: '/system/user/update'
      },
      tempIconValue: '',
      treeData: []
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