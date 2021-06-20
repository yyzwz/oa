// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getBigAreaList = (params) => {
    return getRequest('/bigArea/getByPage', params)
}
// 添加
export const addBigArea = (params) => {
    return postRequest('/bigArea/insertOrUpdate', params)
}
// 编辑
export const editBigArea = (params) => {
    return postRequest('/bigArea/insertOrUpdate', params)
}
// 删除
export const deleteBigArea = (params) => {
    return postRequest('/bigArea/delByIds', params)
}
export const getSonCompany = (params) => {
    return getRequest('/sonCompany/getAll', params)
}