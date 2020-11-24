import request from '@/utils/request'

const api = {
  getDictByCode: '/system/dict/getDictItemList'
}

export function loadDictItemByCode(dictCode){
  return request({
    url: api.getDictByCode,
    method: 'get',
    params: {dictCode: dictCode}
  }).then(result => {
    if(!result.code){
      return result.data
    }else{
      this.$message.error('获取字典信息失败')
      return null
    }
  })
}