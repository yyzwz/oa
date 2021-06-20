// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getShiList = (params) => {
    return getRequest('/shi/getByPage', params)
}
// 添加
export const addShi = (params) => {
    return postRequest('/shi/insertOrUpdate', params)
}
// 编辑
export const editShi = (params) => {
    return postRequest('/shi/insertOrUpdate', params)
}
// 删除
export const deleteShi = (params) => {
    return postRequest('/shi/delByIds', params)
}
export const findAllSheng = (params) => {
    return getRequest('/sheng/findAllSheng', params)
}
export const findAllCityLevel = (params) => {
    return getRequest('/cityLevel/getAll', params)
}
export const findAllCityType = (params) => {
    return getRequest('/cityType/getAll', params)
}