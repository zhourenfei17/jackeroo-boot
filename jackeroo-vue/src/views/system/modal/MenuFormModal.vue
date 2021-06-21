<template>
  <j-drawer
      :title="title"
      :width="width"
      :visible.sync="visible"
      :disabled="flag.view"
      @ok="handleSubmit"
      @close="cancel"
    >
    
    <j-spin :spinning="loading">
      <a-form-model ref="formModel" :model="form" :rules="rules" v-bind="layout">
        <a-row :gutter="formGutter">
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
          <a-col :span="rowSpan" v-if="type == 1 && target == 1">
            <a-form-model-item label="组件路径" prop="component">
              <a-input v-model="form.component" placeholder="请输入组件路径" :disabled="flag.view" addonBefore="/views" addonAfter=".vue"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan" v-if="type == 0">
            <a-form-model-item label="页面布局" prop="layout">
              <a-select v-model="form.layout" placeholder="请选择页面布局" :disabled="flag.view">
                <a-select-option value="PageHeaderView">页头布局</a-select-option>
                <a-select-option value="RouteView">空布局</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="图标" prop="icon">
              <icon-select-input placeholder="点击选择图标" v-model="form.icon" :disabled="flag.view"></icon-select-input>
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
            <a-form-model-item label="是否配置权限" prop="setPermission">
              <a-radio-group v-model="form.setPermission" :disabled="flag.view || enableCloseAuth">
                <a-radio :value="0">否</a-radio>
                <a-radio :value="1">是</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan" v-if="type == 1 && setPermission">
            <a-form-model-item label="功能模块" :prop="form.auth.length > 0 ? 'module': undefined">
              <j-select 
                v-model="form.module" 
                placeholder="请选择功能模块" 
                :url="url.findModuleList" 
                valueField="code" 
                :disabled="flag.view" 
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
          <a-col :span="rowSpan" v-if="type == 1 && setPermission">
            <a-form-model-item label="功能名称" :prop="form.auth.length > 0 ? 'function': undefined">
              <a-input v-model="form.function" placeholder="请输入功能名称" :disabled="flag.view" style="width:90%;"></a-input>

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
          <a-col :span="rowSpan" v-if="type == 1 && setPermission">
            <a-form-model-item label="权限" prop="auth">
              <a-checkbox-group v-model="form.auth" name="auth" :disabled="flag.view" :options="permissionList">
                
              </a-checkbox-group>
              <br />
              <a @click="handleEditAuth" v-if="!flag.view">编辑权限 >></a>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-spin>

    <menu-auth-list-modal ref="menuAuthListModal" @change="handleChangeAuth"></menu-auth-list-modal>
  </j-drawer>
</template>

<script>
import { getAction, postAction, httpAction } from '@/api/manage'
import {JackerooFormMixins} from '@/mixins/JackerooFormMixins'
import { JDrawer, IconSelectInput, JSelect} from '@/components'
import {TreeSelect} from 'ant-design-vue'
import MenuAuthListModal from './MenuAuthListModal'

export default {
  components: {
    JDrawer,
    TreeSelect,
    MenuAuthListModal,
    IconSelectInput,
    JSelect
  },
  mixins: [JackerooFormMixins],
  data(){
    return {
      title: '菜单信息',
      tableName: 'sys_user',
      width: '40vw',
      form: {
        id: undefined,
        parentId: undefined,
        leaf: 1,
        name: undefined,
        href: undefined,
        component: undefined,
        layout: 'PageView',
        icon: undefined,
        sort: undefined,
        target: 1,
        hide: 0,
        type: 0,
        setPermission: 1,
        module: undefined,
        function: undefined,
        auth: []
      },
      permissionList:[],
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
        layout:[
          {required: true, message: '请选择页面布局'}
        ],
        icon: [],
        sort: [{min: 0, max: 999999, message: '长度需要在0到6之间', type: 'number'}],
        target: [],
        hide: [],
        function: [
          {required: true, message: '请填写功能名称'},
          {max: 30, message: '长度需要在0到30之间'},
          {validator: this.validLetterAndUnderline}
        ],
        module: [
          {required: true, message: '请选择功能模块'}
        ],
        auth: []
      },
      url: {
        getById: '/system/menu/',
        save: '/system/menu/save',
        update: '/system/menu/update',
        getTreeSelect: '/system/menu/getTreeSelect',
        findDefaultGroupPermission: '/system/menu/permission/config/findDefaultPermissionConfig',
        findModuleList: '/system/module/allList'
      },
      treeData: [],
      groupId: undefined,
      permissionEdit: false,
      enableCloseAuth: false
    }
  },
  created(){
    this.loadTreeData()
  },
  computed: {
    // 0: 非叶子节点，1：叶子节点
    type(){
      return this.form.leaf
    },
    target(){
      return this.form.target
    },
    setPermission(){
      return this.form.setPermission
    }
  },
  watch: {
    setPermission(val){
      if(val === 1 && this.flag.edit && this.permissionList.length === 0){
        this.loadDefaultAuthAndChecked()
      }
    }
  },
  methods: {
    add(pid, sort){
      this.form.id = null
      this.form.parentId = pid
      this.form.sort = sort
      this.loading = false
      this.loadDefaultAuthAndChecked()
    },
    edit(id){
      this.loading = true
      getAction(this.url.getById + id).then(result => {
        const data = result.data
        if(data.auth){
          // data.function = data.auth[0].value.substring(0, data.auth[0].value.lastIndexOf(':'))
          data.module = data.auth[0].value.split(':')[0]
          data.function = data.auth[0].value.split(':')[1]

          const auth = []
          const permission = []
          let index = 0
          for(const a of data.auth){
            let value = a.value.substring(a.value.lastIndexOf(':') + 1)
            auth.push(value)
            index++
            permission.push({id: index, label: a.label, value: value})
          }
          this.permissionList = permission
          data.auth = auth
          this.enableCloseAuth = true
        }else{
          data.auth = []
          this.permissionList = []
          this.enableCloseAuth = false
        }
        this.copyProperties(data, this.form)
        if(this.form.auth == null || this.form.auth.length == 0){
          this.form.setPermission = 0
        }else{
          this.form.setPermission = 1
        }
      }).finally(() => {
        this.loading = false
      })
    },
    // 加载默认的权限，并选择默认的选项
    loadDefaultAuthAndChecked(){
      getAction(this.url.findDefaultGroupPermission).then(res => {
        if(!res.code){
          const permissionList = []
          const checkedList = []
          for(const item of res.data){
            if(!this.groupId){
              this.groupId = item.groupId
            }
            permissionList.push({label: item.label, value: item.value})
            if(item.checked){
              checkedList.push(item.value)
            }
          }

          this.permissionList = permissionList
          this.form.auth = checkedList
        }
      })
    },
    handleSubmit(){
      this.$refs.formModel.validate((success) => {
        if(success){
          const formData = {...this.form}
          if(formData.leaf == 1){
            const auth = this.permissionList.filter(item => formData.auth.indexOf(item.value) >= 0)
            formData.auth = auth
            formData.layout = null
          }else{
            formData.auth = null
          }
          if(!formData.setPermission){
            formData.auth = null
          }
          if(formData.target == 2){
            formData.component = null
          }
          
          console.log('formData', formData)

          this.$loading.show()
          httpAction(this.requestUrl, formData, this.requestMethod).then(result => {
            if(!result.code){
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
    handleChangeAuth(permissionList, groupId, checked){
      this.permissionEdit = true
      this.permissionList = [...permissionList]
      this.form.auth = checked
      this.groupId = groupId
    },
    handleEditAuth(){
      this.$refs.menuAuthListModal.edit(this.permissionList, this.form.auth, this.groupId, this.permissionEdit)
    },
    loadTreeData(){
      getAction(this.url.getTreeSelect).then(res => {
        if(!res.code){
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

      this.groupId = null
      this.enableCloseAuth = false
      this.permissionEdit = false
      this.form.auth = []
      this.form.module = undefined
      this.form.function = ''
      this.form.layout = 'PageView'
      this.form.setPermission = 1
      this.form.hide = 0
      this.form.target = 1
      this.$refs.formModel.resetFields()
      this.$refs.formModel.clearValidate()
      
      this.loading = true
    }
  }
}
</script>

<style lang="less" scoped>
.tooltip-red{
  color: #ff4545;
}
</style>

<style lang="less">
.tooltip-content{
  .ant-tooltip-content{
    .ant-tooltip-inner{
      width: 350px;
    }
  }
}
</style>