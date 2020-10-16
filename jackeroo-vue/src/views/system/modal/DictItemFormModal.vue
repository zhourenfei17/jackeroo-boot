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
            <a-form-model-item label="字典项名称" prop="label">
              <a-input v-model="form.label" placeholder="请输入字典项名称" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="字典项值" prop="value">
              <a-input v-model="form.value" placeholder="请输入字典项值" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="排序" prop="sort">
              <a-input-number v-model="form.sort" placeholder="请输入排序" :disabled="flag.view" style="width: 100%;"></a-input-number>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="备注" prop="remark">
              <a-input v-model="form.remark" placeholder="请输入备注" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-spin>
  </j-modal>
</template>

<script>
import { getAction, postAction, httpAction } from '@/api/manage'
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'

export default {
  mixins: [JackerooFromMixins],
  data(){
    return {
      title: '字典项信息',
      tableName: 'sys_dict',
      width: '40vw',
      form: {
        id: null,
        label: null,
        value: null,
        sort: null,
        remark: null,
        dictCode: null
      },
      url: {
        getById: '/system/dict/',
        add: '/system/dict/addDictItem',
        update: '/system/dict/updateDictItem',
        getMaxSort: '/system/dict/getMaxSort'
      }
    }
  },
  computed: {
    rules() {
      return {
        value: [
          {required: true, message: '请输入字典项值'},
          {validator: this.validUnique, condition: 'type=1 and dict_code=#{dictCode}', param: {dictCode: this.form.dictCode}, message: '字典项值不能重复', trigger: 'blur'},
          {max: 30, message: '长度需要在0到30之间'},
        ],
        label: [
          {required: true, message: '请输入字典项名称'},
          {max: 30, message: '长度需要在0到30之间'},
        ],
        sort: [
          {min: 0, max: 99999, message: '长度需要在0到5之间', type: 'number'},
        ],
        remark: [
          {max: 100, message: '长度需要在0到100之间'},
        ],
      }
    }
  },
  methods: {
    add(dictCode){
      this.form.id = null
      this.form.dictCode = dictCode
      getAction(this.url.getMaxSort, {dictCode: dictCode}).then(result => {
        if(!result.code){
          this.form.sort = result.data
        }
      })
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
