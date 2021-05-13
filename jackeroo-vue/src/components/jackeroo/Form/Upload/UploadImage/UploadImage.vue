<template>
  <div>
    <a-upload
      name="avatar"
      listType="picture-card"
      class="avatar-uploader"
      :accept="accept"
      :disabled="disabled"
      :showUploadList="multiple"
      :beforeUpload="beforeUpload"
      :fileList="fileList"
      :multiple="multiple"
      :customRequest="function(){}"
      :remove="handleRemove"
      @change="handleChange"
    >
      <template v-if="!multiple">
        <img class="default-img" v-if="imageUrl" :src="imageUrl" alt="avatar" />
        <div v-else>
          <a-icon :type="loading ? 'loading' : 'plus'" />
          <div class="ant-upload-text">点击上传</div>
        </div>
      </template>
      <template v-else>
        <div v-if="fileList.length < multipleSize">
          <a-icon type="plus" />
          <div class="ant-upload-text">点击上传</div>
        </div>
      </template>
    </a-upload>

    <cropper-modal ref="cropperModal" @ok="handleCropperSuccess"></cropper-modal>
  </div>
</template>

<script>
import {uploadImg} from '@/api/manage'
import CropperModal from './CropperModal'
import {UploadMixins} from './UploadMixins'

export default {
  props:{
    //回显图片路径
    value:{
      type: String,
      default: ''
    },
    //是否支持多图片上传
    multiple: {
      type: Boolean,
      default: false
    },
    // 最多上传的图片数量-待实现
    multipleSize: {
      type: Number,
      default: 9
    },
    // 如果multiple=true，则需要返回Array还是String
    isArray: {
      type: Boolean,
      default: false
    },
    // 图片是否需要裁剪，仅multiple=false时有效
    isCrop: {
      type: Boolean,
      default: true
    },
    disabled:{
      type: Boolean
    }
  },
  mixins: [UploadMixins],
  components:{
    CropperModal
  },
  data () {
    return {
      loading: false,
      imageUrl: '',
      fileList: [],
      imageList: [],
      imageCount: 0
    }
  },
  watch:{
    value:{
      handler(val){
        this.imageUrl = val || '';
      },
      immediate:true
    },
  },
  methods: {
    //从本地选择文件
    handleChange (info) {
      if(info.file.status === 'removed'){
        return
      }
      this.imageCount++
      if(this.multiple){
        if(this.imageCount > this.multipleSize){
          this.$message.warning(`最多只能上传${this.multipleSize}张图片`)
          return
        }
        this.upload(info.file.originFileObj)
      }else{
        if(this.isCrop){
          var reader = new FileReader()
          reader.onload = (e) => {
            let data
            if (typeof e.target.result === 'object'){ 
              // 把Array Buffer转化为blob 如果是base64不需要 
              data = window.URL.createObjectURL(new Blob([e.target.result]))
            }else{
              data = e.target.result
            }
            this.$refs['cropperModal'].edit(data)
          }
          // 转化为base64
          // reader.readAsDataURL(file) 
          // 转化为blob 
          reader.readAsArrayBuffer(info.file.originFileObj)  
        }else{
          this.upload(info.file.originFileObj)
        }
      }
    },
    //裁剪成功后的File对象
    handleCropperSuccess(data){
      this.upload(data)
    },
    // 上传图片
    upload(file){
      uploadImg(file).then(res => {
        if(!res.code){
          if(this.multiple){
            this.fileList.push(this.transformUrl(res.data[0]))
            this.imageList.push(res.data[0])
            if(this.isArray){
              this.$emit('input', this.imageList)
            }else{
              this.$emit('input', this.imageList.join(","))
            }
          }else{
            this.imageUrl = res.data[0]
            this.$emit('input', this.imageUrl)
          }
          
        }else{
          this.$message.success('上传失败！')
        }
      })
    },
    transformUrl(url){
      return {url: url, uid: new Date().getTime(), name: url.substring(url.lastIndexOf('/') + 1), status: 'done'}
    },
    handleRemove(file){
      this.fileList = this.fileList.filter(item => item.url != file.url)
      this.imageList = this.imageList.filter(item => item.url != file.url)
      if(this.isArray){
        this.$emit('input', this.imageList)
      }else{
        this.$emit('input', this.imageList.join(","))
      }
      this.imageCount--
      return true
    }
  }
}
</script>

<style lang="less" scoped>
.default-img{
  width: 102px;
  height: 102px;
}
</style>