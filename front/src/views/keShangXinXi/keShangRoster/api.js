// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getKeShangRosterList = (params) => {
    return getRequest('/keShangRoster/getByPage', params)
}
// 添加
export const addKeShangRoster = (params) => {
    return postRequest('/keShangRoster/insertOrUpdate', params)
}
// 编辑
export const editKeShangRoster = (params) => {
    return postRequest('/keShangRoster/insertOrUpdate', params)
}
// 删除
export const deleteKeShangRoster = (params) => {
    return postRequest('/keShangRoster/delByIds', params)
}
export const getAllShengShiXian = (params) => {
    return getRequest('/xian/getAllShengShiXian', params)
}
export const getAllShangType = (params) => {
    return getRequest('/keShangType/getNotParent', params)
}
export const getLookList = (params) => {
    return getRequest('/shangCall/getByPage', params)
}
export const deleteLook = (params) => {
    return postRequest('/shangCall/delByIds', params)
}
export const addLook = (params) => {
    return postRequest('/shangCall/insertOrUpdate', params)
}
export const editLook = (params) => {
    return postRequest('/shangCall/insertOrUpdate', params)
}