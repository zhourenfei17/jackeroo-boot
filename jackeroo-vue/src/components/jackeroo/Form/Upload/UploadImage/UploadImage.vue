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
      type:String,
      default:''
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
      /* let { options } = this;
      Utils.file2Base64(info.file.originFileObj, (imageUrl) => {
        let target = Object.assign({},options,{
          img:imageUrl
        })
        this.$refs['cropperModal'].edit(target);
      }) */
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
      
      /* uploadImg(info.file.originFileObj).then(res => {
        if(!res.code){
          this.imageUrl = res.data[0]
        }
      }) */
    },
    //裁剪成功后的File对象
    handleCropperSuccess(data){
      console.log('File:',data);
      //进行图片上传动作
      // 模拟后端请求 2000 毫秒延迟
      let that=this;
      that.loading = true
      new Promise((resolve) => {
          setTimeout(() => resolve(), 2000)
      }).then((res) => {
        that.$message.success('上传成功')
        //将返回的数据回显
        that.imageUrl = res.img;
        that.$emit('ok')
      }).catch(() => {
        // Do something
      }).finally(() => {
        that.loading = false
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