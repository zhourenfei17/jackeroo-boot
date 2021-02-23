import request from '@/utils/request'

const api = {
  getDictByCode: '/system/dict/getDictItemList',
  getDictItemByCodes: '/system/dict/getDictItemByCodes'
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

export function loadDictItemByCodeList(dictCodeList){
  return request({
    url: api.getDictItemByCodes,
    method: 'get',
    params: {dictCode: dictCodeList.join(',')}
  }).then(result => {
    if(!result.code){
      return result.data
    }else{
      this.$message.error('获取字典信息失败')
      return null
    }
  })
}