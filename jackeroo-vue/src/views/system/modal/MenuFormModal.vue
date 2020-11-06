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
                <a-select-option value="PageView">基础布局（包含面包屑）</a-select-option>
                <a-select-option value="RouteView">空布局</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="图标" prop="icon">
              <a-input placeholder="点击选择图标" v-model="form.icon" :readOnly="true" :disabled="flag.view">
                <span v-if="!form.icon" slot="addonBefore" style="width:15px;display: inline-block;"></span>
                <a-icon v-else :type="form.icon" slot="addonBefore"></a-icon>
                <a-icon type="close-circle" slot="suffix" @click="clearIcon" class="close"></a-icon>
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
            <a-form-model-item label="是否配置权限" prop="setPermission">
              <a-radio-group v-model="form.setPermission" :disabled="flag.view">
                <a-radio :value="0">否</a-radio>
                <a-radio :value="1">是</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan" v-if="type == 1 && setPermission">
            <a-form-model-item label="权限标识前缀" prop="group" v-if="form.auth.length > 0">
              <a-input v-model="form.group" placeholder="请输入权限标识前缀" :disabled="flag.view" style="width:90%;"></a-input>

              <a-tooltip placement="left"> 
                <template slot="title">
                  <span>用来拼接权限标识</span>
                  <br>
                  <span class="tooltip-red">示例：</span>
                  <br>
                  <span class="tooltip-red">权限标识前缀: system:user</span>
                  <br>
                  <span class="tooltip-red">权限标识后缀: add</span>
                  <br>
                  <span class="tooltip-red">拼接后权限标识: system:user:add</span>
                </template>
                <a-icon type="exclamation-circle" style="margin-left:20px;"></a-icon>
              </a-tooltip>
            </a-form-model-item>
            <a-form-model-item label="权限标识前缀" v-else>
              <a-input v-model="form.group" placeholder="请输入权限标识前缀" :disabled="flag.view" style="width:90%;"></a-input>

              <a-tooltip placement="left"> 
                <template slot="title">
                  <span>用来拼接权限标识</span>
                  <br>
                  <span class="tooltip-red">示例：</span>
                  <br>
                  <span class="tooltip-red">权限标识前缀: system:user</span>
                  <br>
                  <span class="tooltip-red">权限标识后缀: add</span>
                  <br>
                  <span class="tooltip-red">拼接后权限标识: system:user:add</span>
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

    <!-- 选择图标 -->
    <a-modal :visible="iconVisible" title="选择图标" width="50vw" @cancel="handleIconCancel" @ok="handleIconOk">
      <icon-selector @change="handleIconChange" :value="tempIconValue"></icon-selector>
    </a-modal>

    <menu-auth-list-modal ref="menuAuthListModal" @change="handleChangeAuth"></menu-auth-list-modal>
  </j-drawer>
</template>

<script>
import { getAction, postAction, httpAction } from '@/api/manage'
import {JackerooFormMixins} from '@/mixins/JackerooFormMixins'
import {IconSelector, JDrawer} from '@/components'
import {TreeSelect} from 'ant-design-vue'
import MenuAuthListModal from './MenuAuthListModal'

export default {
  components: {
    IconSelector,
    JDrawer,
    TreeSelect,
    MenuAuthListModal
  },
  mixins: [JackerooFormMixins],
  data(){
    return {
      title: '菜单信息',
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
        layout: 'PageView',
        icon: '',
        sort: null,
        target: 1,
        hide: 0,
        type: 0,
        setPermission: 1,
        group: '',
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
        group: [{required: true, message: '请填写权限标识前缀'}],
        auth: []
      },
      url: {
        getById: '/system/menu/',
        save: '/system/menu/save',
        update: '/system/menu/update',
        getTreeSelect: '/system/menu/getTreeSelect',
        findDefaultGroupPermission: '/system/menu/permission/config/findDefaultPermissionConfig'
      },
      tempIconValue: '',
      treeData: [],
      groupId: null,
      permissionEdit: false
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
  methods: {
    add(pid, sort){
      this.form.id = null
      this.form.parentId = pid
      this.form.sort = sort
      this.loading = false
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
    edit(id){
      this.loading = true
      getAction(this.url.getById + id).then(result => {
        const data = result.data
        if(data.auth){
          data.group = data.auth[0].value.substring(0, data.auth[0].value.lastIndexOf(':'))

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
        }else{
          data.auth = []
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
    handleSubmit(){
      this.$refs.formModel.validate((success) => {
        if(success){
          const formData = JSON.parse(JSON.stringify(this.form))
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
      this.permissionList = JSON.parse(JSON.stringify(permissionList))
      if(checked && groupId){
        this.form.auth = checked
        this.groupId = groupId
      }
    },
    handleEditAuth(){
      this.$refs.menuAuthListModal.edit(this.permissionList, this.groupId, this.permissionEdit)
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
        if(!res.code){
          const treeData = []
          treeData.push(res.data)
          this.treeData = treeData
        }
      })
    },
    clearIcon(){
      this.form.icon = ''
    },
    cancel(){
      this.visible = false
      this.flag.add = false
      this.flag.edit = false
      this.flag.view = false

      this.groupId = null
      this.permissionEdit = false
      this.form.auth = []
      this.form.group = ''
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
.close{
  color: #D3D3D3;
}
.close:hover{
  color: #696969;
}
</style>