export const UploadMixins = {
  props:{
    // 图片格式
    imgFormat: {
      type: Array,
      default: () => {
        return ['image/gif', 'image/jpeg', 'image/png']
      }
    },
    // 图片大小，单位：mb
    imgSize:{
      type: Number,
      default: 2
    },
  },
  data(){
    return {
      accept: 'image/*'
    }
  },
  methods: {
    // 上传之前 格式与大小校验
    beforeUpload(file){
      let { imgFormat, imgSize } = this
      let isFormat = imgFormat.includes(file.type)
      if(!isFormat){
        this.$message.error('图片格式不支持！')
      }
      const isLt2M = file.size / 1024 / 1024 <= imgSize
      if (!isLt2M) {
        this.$message.error('图片大小限制在' + imgSize + 'MB内!')
      }
      return isFormat && isLt2M
    }
  },
}