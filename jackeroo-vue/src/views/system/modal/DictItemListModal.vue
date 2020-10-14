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
        <a-row :gutter="formGutter">
          <a-col :span="rowSpan">
            <a-form-model-item label="上级菜单" prop="parentId">
              <tree-select v-model="form.parentId" placeholder="请选择上级菜单" :disabled="true" :treeData="treeData" style="width:100%;" 
                :dropdownStyle="{maxHeight: '200px', overflow: 'auto'}">
                
              </tree-select>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-spin>
  </j-drawer>
</template>

<script>
import { getAction, postAction, httpAction } from '@/api/manage'
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'
import { JDrawer} from '@/components'

export default {
  components: {
    JDrawer
  },
  mixins: [JackerooFromMixins],
  data() {
    return {
      title: '菜单信息',
      tableName: 'sys_user',
      width: '40vw',
      form: {
        id: null,
        parentId: null,
        
      },
      rules: {
        parentId: [
          {required: true, message: '请选择上级菜单'}
        ],
        leaf:[
          {required: true, message: '请选择是否叶子菜单'}
        ],
      },
      url: {
        getById: '/system/menu/',
        add: '/system/menu/add',
        update: '/system/menu/update',
      },
    }
  }
}
</script>