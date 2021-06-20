// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getPaySlipList = (params) => {
    return getRequest('/paySlip/getByPage', params)
}
// 添加
export const addPaySlip = (params) => {
    return postRequest('/paySlip/insertOrUpdate', params)
}
// 编辑
export const editPaySlip = (params) => {
    return postRequest('/paySlip/insertOrUpdate', params)
}
// 删除
export const deletePaySlip = (params) => {
    return postRequest('/paySlip/delByIds', params)
}
export const batchGeneration = (params) => {
    return getRequest('/paySlip/batchGeneration', params)
}