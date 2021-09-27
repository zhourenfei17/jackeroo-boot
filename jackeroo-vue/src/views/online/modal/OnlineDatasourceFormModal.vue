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
    >
    <j-spin :spinning="loading">
      <a-form-model ref="formModel" :model="form" :rules="rules" v-bind="layout">
        <a-row :gutter="formGutter"> 
          <a-col :span="rowSpan">
            <a-form-model-item label="数据源名称" prop="name">
              <a-input v-model="form.name" placeholder="请输入数据源名称" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col> 
          <a-col :span="rowSpan">
            <a-form-model-item label="数据库类型" prop="driverClassName">
              <j-dict-select
                v-model="form.driverClassName"
                placeholder="请选择数据库类型"
                dictCode="GEN_DATABASE_TYPE"
                :disabled="flag.view"
                >
              </j-dict-select>
            </a-form-model-item>
          </a-col> 
          <a-col :span="rowSpan">
            <a-form-model-item label="连接地址" prop="url">
              <a-input v-model="form.url" placeholder="请输入连接地址" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col> 
          <a-col :span="rowSpan">
            <a-form-model-item label="用户名" prop="username">
              <a-input v-model="form.username" placeholder="请输入用户名" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col> 
          <a-col :span="rowSpan" v-show="!flag.view">
            <a-form-model-item prop="password">
              <span slot="label">
                密码
                <a-tooltip title="若为空则不修改密码！" v-show="flag.edit">
                  <a-icon type="question-circle-o"/>
                </a-tooltip>
              </span>
              <a-input v-model="form.password" type="password" placeholder="请输入密码" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          
          <!-- <a-col :span="rowSpan" v-if="dbUrl">
            <a-form-model-item label="数据库链接">
              <a-text :value="dbUrl"/>
            </a-form-model-item>
          </a-col> -->
        </a-row>
      </a-form-model>
    </j-spin>

    <template slot="footer">
        <a-button @click="handleLink()" :disabled="flag.edit && !form.password">连接测试</a-button>
        <a-divider type="vertical"></a-divider>
        <a-button @click="cancel()">取消</a-button>
        <a-button type="primary" @click="handleSubmit()">保存</a-button>
      </template>
  </j-modal>
</template>

<script>
import { getAction, httpAction } from '@/api/manage'
import {JackerooFormMixins} from '@/mixins/JackerooFormMixins'
import { JDictSelect } from '@/components'
import AText from '@/components/Jackeroo/DataDisplay/Text';

export default {
  mixins: [JackerooFormMixins],
  components: {
    JDictSelect,
    AText
  },
  data(){
    return {
      title: '数据源配置',
      tableName: 'online_datasource',
      width: '40vw',
      formCol: 1,
      form: {
        id: undefined,
        name: undefined,
        driverClassName: undefined,
        url: undefined,
        username: undefined,
        password: undefined,
      },
      url: {
        getById: '/online/datasource/',
        save: '/online/datasource/save',
        update: '/online/datasource/update',
        testConnect: '/online/datasource/testConnect'
      }
    }
  },
  computed: {
    rules() {
      const rules = {
        name: [
          {required: true, message: '请输入数据源名称'},
          {max: 50, message: '长度需要在0到50之间'},
          {validator: this.validUnique, message: '数据源名称不能重复', trigger: 'blur'},
        ],
        driverClassName: [
          {required: true, message: '请输入数据库类型'},
        ],
        url: [
          {required: true, message: '请输入连接地址'},
          {max: 500, message: '长度需要在0到500之间'}
        ],
        username: [
          {required: true, message: '请输入用户名'},
          {max: 50, message: '长度需要在0到50之间'},
        ]
      }
      if(!this.flag.edit){
        rules.password = [
          {required: true, message: '请输入密码'},
          {max: 50, message: '长度需要在0到50之间'},
        ]
      }
      return rules
    }
  },
  methods: {
    add(){
      this.form.id = null
      this.loading = false
    },
    edit(id){
      getAction(this.url.getById + id).then(result => {
        this.copyProperties({...result.data, password: undefined}, this.form)
      }).finally(() => {
        this.loading = false
      })
    },
    handleLink() {
      this.$refs.formModel.validate((success) => {
        if(success) {
          this.$loading.show()
          getAction(this.url.testConnect, this.form).then(res => {
            if(!res.code){
              this.$message.success('连接成功！')
            }
          }).finally(() => {
            this.$loading.hide()
          })
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
