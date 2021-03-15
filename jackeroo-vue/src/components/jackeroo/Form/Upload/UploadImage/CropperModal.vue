<template>
  <a-modal :visible="visible" title="修改头像" :maskClosable="false" :confirmLoading="confirmLoading" :width="1000" @cancel="cancelHandel">
    <a-row>
      <a-col :span="12">
        <div class="cropper-wrapper">
          <vue-cropper
            ref="cropper"
            v-bind="options"
            @realTime="realTime"
          >
          </vue-cropper>
        </div>

        <a-row>
          <a-col :span="24" class="footer-btn">
            <a-upload
              :accept="accept"
              :showUploadList="false"
              :beforeUpload="beforeUpload"
              :customRequest="function(){}"
              @change="handleChange">

              <a-button type="primary" size="small" icon="upload">重选</a-button>
            </a-upload>
            <a-button type="danger" size="small" ghost @click="changeScale(1)" icon="zoom-in">放大</a-button>
            <a-button type="danger" size="small" ghost @click="changeScale(-1)" icon="zoom-out">缩小</a-button>
            <a-button type="danger" size="small" ghost @click="rotateLeft" icon="undo">左旋转</a-button>
            <a-button type="danger" size="small" ghost @click="rotateRight" icon="redo">右旋转</a-button>
          </a-col>
        </a-row>
      </a-col>
      <a-col :span="12">
        <div class="result-wrapper">
          <div class="tar-img" :style=" {...previews.div, borderRadius: '50%', border: '1px solid'}">
            <img :src="previews.url" :style="previews.img"/>
          </div>
        </div>
      </a-col>
    </a-row>
    <template slot="footer">
      <a-button key="back" @click="cancelHandel">取消</a-button>
      <a-button key="submit" type="primary" :loading="confirmLoading" @click="okHandel">保存</a-button>
    </template>
  </a-modal>
</template>
<script>
  import { VueCropper } from 'vue-cropper'
  import {UploadMixins} from './UploadMixins'
 
  export default {
    name:'cropperModal',
    components: {
      VueCropper
    },
    mixins: [UploadMixins],
    data() {
      return {
        visible: false,
        img: null,
        confirmLoading: false,
        options: {
          img: null,//裁剪图片的地址
          info: true,  //裁剪框的大小信息
          canScale: true, //图片是否允许滚轮缩放
          original: false, //上传图片按照原始比例渲染
          autoCrop: true, //是否默认生成截图框
          autoCropWidth: 400, //默认生成截图框宽度
          autoCropHeight: 400, //默认生成截图框高度
          fixed: true,  //是否开启截图框宽高固定比例
          fixedNumber: [1, 1], //截图框的宽高比例
          fixedBox: false, //固定截图框大小 不允许改变
          centerBox: true, //截图框是否被限制在图片里面
          canMove: false, // 上传图片是否可以移动
        },
        previews: {},
      };
    },
    methods: {
      edit(file) {
        this.confirmLoading = true
        this.visible = true
        this.options.img = file
        this.confirmLoading = false
      },
      cancelHandel() {
        this.visible = false
        // this.options.img = null
      },
      okHandel() {
        const that = this
        that.confirmLoading = true
        // 获取截图的base64 数据
        this.$refs.cropper.getCropData((data) => {
          // 转换为File对象
          // let file = Utils.dataURLtoFile(data,'测试哟');
          //将裁剪侯的图片对象返回给父组件
          // that.$emit('ok',file);
          that.cancelHandel()
        })
      },
      //下载输入框里的图片
      downloadNewImg(){
        //获取截图的blob数据
        this.$refs.cropper.getCropBlob((data) => { 
          /* Utils.blob2Base64(data).then(res=>{
            Utils.downLoadImage(res,'测试的哟');
          }) */
        })
      },
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
          this.options.img = data
        } 
        
        reader.readAsArrayBuffer(info.file.originFileObj)  
      },
      //移动框的事件
      realTime(data) {
        this.previews = data
      },
      //图片缩放
      changeScale (num) {
        this.$refs.cropper.changeScale(num || 1)
      },
      //向左旋转
      rotateLeft () {
        this.$refs.cropper.rotateLeft()
      },
      //向右旋转
      rotateRight () {
        this.$refs.cropper.rotateRight()
      },
    }
  };
</script>
 
<style lang="less" scoped>
  .cropper-wrapper{
    width: 100%;
    height: 400px;
  }
  .result-wrapper{
    width: 100%;
    height: 400px;
    border-radius: 50%;
    display: flex;
    padding: 20px;
    justify-content: center;
    align-items: center;
    .tar-img{
      overflow: hidden;
    }
  }
  
  .footer-btn{
    display: flex;
    display: -webkit-flex;
    justify-content: space-between;
    margin-top: 20px;
  }
</style>