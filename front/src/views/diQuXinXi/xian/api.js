// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getXianList = (params) => {
    return getRequest('/xian/getByPage', params)
}
// 添加
export const addXian = (params) => {
    return postRequest('/xian/insertOrUpdate', params)
}
// 编辑
export const editXian = (params) => {
    return postRequest('/xian/insertOrUpdate', params)
}
// 删除
export const deleteXian = (params) => {
    return postRequest('/xian/delByIds', params)
}
export const getAllCountyType = (params) => {
    return getRequest('/countyType/getAll', params)
}