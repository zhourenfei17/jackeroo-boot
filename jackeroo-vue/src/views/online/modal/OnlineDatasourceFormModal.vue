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
            <a-form-model-item label="数据库类型" prop="type">
              <j-dict-select
                v-model="form.type"
                placeholder="请选择数据库类型"
                dictCode="GEN_DATABASE_TYPE"
                :disabled="flag.view"
                >
              </j-dict-select>
            </a-form-model-item>
          </a-col> 
          <a-col :span="rowSpan">
            <a-form-model-item label="ip/域名地址" prop="ip">
              <a-input v-model="form.ip" placeholder="请输入ip/域名地址" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col> 
          <a-col :span="rowSpan">
            <a-form-model-item label="端口" prop="port">
              <a-input-number v-model="form.port" placeholder="请输入端口" :precision="0" style="width: 100%;" :disabled="flag.view"></a-input-number>
            </a-form-model-item>
          </a-col> 
          <a-col :span="rowSpan">
            <a-form-model-item label="用户名" prop="user">
              <a-input v-model="form.user" placeholder="请输入用户名" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col> 
          <a-col :span="rowSpan">
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
          <a-col :span="rowSpan">
            <a-form-model-item label="数据库名" prop="databaseName">
              <a-input v-model="form.databaseName" placeholder="请输入数据库名" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-spin>
  </j-modal>
</template>

<script>
import { getAction, httpAction } from '@/api/manage'
import {JackerooFormMixins} from '@/mixins/JackerooFormMixins'
import { JDictSelect } from '@/components'

export default {
  mixins: [JackerooFormMixins],
  components: {
    JDictSelect
  },
  data(){
    return {
      title: '数据源配置',
      
      width: '40vw',
      formCol: 1,
      form: {
        id: undefined,
        type: undefined,
        ip: undefined,
        port: undefined,
        user: undefined,
        password: undefined,
        databaseName: undefined,
      },
      url: {
        getById: '/online/datasource/',
        save: '/online/datasource/save',
        update: '/online/datasource/update'
      }
    }
  },
  computed: {
    rules() {
      const rules = {
        type: [
          {required: true, message: '请输入数据库类型'},
        ],
        ip: [
          {required: true, message: '请输入ip/域名地址'},
          {max: 20, message: '长度需要在0到20之间'},
          {validator: this.validIpAndHost, message: '请填写正确的ip/域名地址', trigger: 'blur'}
        ],
        port: [
          {required: true, message: '请输入端口'},
          {max: 99999, message: '范围需要在0到99999之间', type: 'number'},
        ],
        user: [
          {required: true, message: '请输入用户名'},
          {max: 50, message: '长度需要在0到50之间'},
        ],
        databaseName: [
          {required: true, message: '请输入数据库名'},
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
