// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getCountyTypeList = (params) => {
    return getRequest('/countyType/getByPage', params)
}
// 添加
export const addCountyType = (params) => {
    return postRequest('/countyType/insertOrUpdate', params)
}
// 编辑
export const editCountyType = (params) => {
    return postRequest('/countyType/insertOrUpdate', params)
}
// 删除
export const deleteCountyType = (params) => {
    return postRequest('/countyType/delByIds', params)
}