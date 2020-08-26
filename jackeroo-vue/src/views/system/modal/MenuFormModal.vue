<template>
  <j-drawer
      :title="title"
      :width="width"
      :visible.sync="visible"
      @ok="handleSubmit"
      @close="cancel"
    >
    
    <j-spin :spinning="loading">
      <a-form-model ref="formModel" :model="form" :rules="rules" v-bind="layout">
        <a-row :gutter="24">
          <a-col :span="rowSpan">
            <a-form-model-item label="上级菜单" prop="parentId">
              <tree-select v-model="form.parentId" placeholder="请选择上级菜单" :disabled="true" :treeData="treeData" style="width:100%;" 
                :dropdownStyle="{maxHeight: '200px', overflow: 'auto'}">
                
              </tree-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="是否叶子菜单" prop="leaf">
              <a-radio-group v-model="form.leaf" :disabled="flag.view">
                <a-radio :value="0">否</a-radio>
                <a-radio :value="1">是</a-radio>
              </a-radio-group>
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
          <a-col :span="rowSpan" v-if="type == 1">
            <a-form-model-item label="组件路径" prop="component">
              <a-input v-model="form.component" placeholder="请输入组件路径" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan" v-if="type == 0">
            <a-form-model-item label="图标" prop="icon">
              <a-input placeholder="点击选择图标" v-model="form.icon" :readOnly="true" :disabled="flag.view">
                <a-icon v-if="flag.view" slot="addonAfter" type="setting"/>
                <a-icon v-else slot="addonAfter" type="setting" @click="selectIcons"/>
              </a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="排序号" prop="sort">
              <a-input-number v-model="form.sort" placeholder="请输入排序号" :disabled="flag.view" style="width:100%;" :min="0"></a-input-number>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan" v-if="type == 1">
            <a-form-model-item label="目标" prop="target">
              <a-radio-group v-model="form.target" :disabled="flag.view">
                <a-radio :value="1">内部</a-radio>
                <a-radio :value="2">外部</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="是否隐藏" prop="hide">
              <a-radio-group v-model="form.hide" :disabled="flag.view">
                <a-radio :value="0">否</a-radio>
                <a-radio :value="1">是</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan" v-if="type == 1">
            <a-form-model-item label="权限分组" prop="group" v-if="form.auth.length > 0">
              <a-input v-model="form.group" placeholder="请输入权限分组" :disabled="flag.view"></a-input>
            </a-form-model-item>
            <a-form-model-item label="权限分组" v-else>
              <a-input v-model="form.group" placeholder="请输入权限分组" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan" v-if="type == 1">
            <a-form-model-item label="权限" prop="auth">
              <a-checkbox-group v-model="form.auth" name="auth" :disabled="flag.view" :options="auth">
                
              </a-checkbox-group>
              <br />
              <a @click="handleEditAuth">编辑权限 >></a>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-spin>

    <!-- 选择图标 -->
    <a-modal :visible="iconVisible" title="选择图标" width="50vw" @cancel="handleIconCancel" @ok="handleIconOk">
      <icon-selector @change="handleIconChange" :value="tempIconValue"></icon-selector>
    </a-modal>

    <menu-auth-list-modal ref="menuAuthListModal"></menu-auth-list-modal>
  </j-drawer>
</template>

<script>
import { getAction, postAction, httpAction } from '@/api/manage'
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'
import JSelect from '@/components/jackeroo/JSelect'
import {IconSelector, JDrawer} from '@/components'
import {TreeSelect} from 'ant-design-vue'
import MenuAuthListModal from './MenuAuthListModal'

export default {
  components: {
    JSelect,
    IconSelector,
    JDrawer,
    TreeSelect,
    MenuAuthListModal
  },
  mixins: [JackerooFromMixins],
  data(){
    return {
      title: '用户信息',
      tableName: 'sys_user',
      width: '40vw',
      iconVisible: false,
      form: {
        id: null,
        parentId: null,
        leaf: 1,
        name: '',
        href: '',
        component: '',
        icon: '',
        sort: null,
        target: 1,
        hide: 0,
        type: 0,
        group: '',
        auth: ['view', 'add', 'update', 'delete']
      },
      auth:[
        {
          label: '查看',
          value: 'view'
        },{
          label: '添加',
          value: 'add'
        },{
          label: '修改',
          value: 'update'
        },{
          label: '删除',
          value: 'delete'
        },{
          label: '导入',
          value: 'import'
        },{
          label: '导出',
          value: 'export'
        }
      ],
      rules: {
        parentId: [
          {required: true, message: '请选择上级菜单'}
        ],
        leaf:[
          {required: true, message: '请选择是否叶子菜单'}
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
        target: [],
        hide: [],
        group: [{required: true, message: '请填写权限分组'}],
        auth: []
      },
      url: {
        getById: '/system/menu/',
        add: '/system/menu/add',
        update: '/system/menu/update',
        getTreeSelect: '/system/menu/getTreeSelect'
      },
      tempIconValue: '',
      treeData: [],
      checkedAuth: []
    }
  },
  created(){
    this.loadTreeData()
  },
  computed: {
    // 0: 非叶子节点，1：叶子节点
    type(){
      return this.form.leaf
    }
  },
  methods: {
    add(pid, sort){
      this.form.id = null
      this.form.parentId = pid
      this.form.sort = sort
      this.loading = false
    },
    edit(id){
      this.loading = true
      getAction(this.url.getById + id).then(result => {
        const data = result.data
        if(data.auth){
          data.group = data.auth[0].value.substring(0, data.auth[0].value.lastIndexOf(':'))

          const auth = []
          for(const a of data.auth){
            auth.push(a.value.substring(a.value.lastIndexOf(':') + 1))
          }
          data.auth = auth
          console.log('data', data)
        }
        this.copyProperties(data, this.form)
      }).finally(() => {
        this.loading = false
      })
    },
    handleSubmit(){
      this.$refs.formModel.validate((success) => {
        if(success){
          const formData = JSON.parse(JSON.stringify(this.form))
          if(formData.leaf == 1){
            const auth = this.auth.filter(item => formData.auth.indexOf(item.value) >= 0)
            formData.auth = auth
          }else{
            formData.auth = null
          }
          
          console.log('formData', formData)

          this.$loading.show()
          httpAction(this.requestUrl, formData, this.requestMethod).then(result => {
            if(result.code === 0){
              this.$message.success('保存成功！')
              this.cancel()
              this.loadTreeData()
              this.$emit('ok')
            }
          }).finally(() => {
            this.$loading.hide()
          })
        }
      })
    },
    handleEditAuth(){
      this.$refs.menuAuthListModal.edit(this.auth)
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
    },
    loadTreeData(){
      getAction(this.url.getTreeSelect).then(res => {
        if(res.code == 0){
          const treeData = []
          treeData.push(res.data)
          this.treeData = treeData
        }
      })
    },
    cancel(){
      this.visible = false
      this.flag.add = false
      this.flag.edit = false
      this.flag.view = false

      this.form.auth = ['view', 'add', 'update', 'delete']
      this.$refs.formModel.resetFields()
      this.$refs.formModel.clearValidate()
      
      this.loading = true
    }
  }
}
</script>