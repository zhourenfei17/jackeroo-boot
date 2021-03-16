<template>
  <div>
    <a-upload
      name="avatar"
      listType="picture-card"
      class="avatar-uploader"
      :accept="accept"
      :showUploadList="false"
      :beforeUpload="beforeUpload"
      :customRequest="function(){}"
      @change="handleChange"
    >
      <img class="default-img" v-if="imageUrl" :src="baseUrl + imageUrl" alt="avatar" />
      <div v-else>
        <a-icon :type="loading ? 'loading' : 'plus'" />
        <div class="ant-upload-text">点击上传</div>
      </div>
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
    //是否支持多图片上传-待实现
    multiple: {
      type: Boolean,
      default: false
    },
    // 最多上传的图片数量-待实现
    multipleSize: {
      type: Number,
      default: 9
    },
    // 图片是否需要裁剪-待实现
    isCrop: {
      type: Boolean,
      default: true
    }
  },
  mixins: [UploadMixins],
  components:{
    CropperModal
  },
  data () {
    return {
      loading: false,
      baseUrl: '/jackeroo/api',
      imageUrl: ''
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
      var reader = new FileReader()
      reader .onload = (e ) => { 
        let data 
        if ( typeof e.target.result === 'object' ) { 
          // 把Array Buffer转化为blob 如果是base64不需要 
          data = window.URL.createObjectURL (new Blob([e.target.result])) 
        } else { 
          data = e .target .result 
        } 
        this.$refs['cropperModal'].edit(data);
      } 
      // 转化为base64
      // reader.readAsDataURL(file) 
      // 转化为blob 
      reader.readAsArrayBuffer(info.file.originFileObj)  
    },
    //裁剪成功后的File对象
    handleCropperSuccess(data){
      uploadImg(data).then(res => {
        if(!res.code){
          this.imageUrl = res.data[0]
          this.$emit('input', this.imageUrl)
        }else{
          this.$message.success('上传成功')
        }
      })
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