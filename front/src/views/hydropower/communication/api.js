// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getCommunicationCostList = (params) => {
    return getRequest('/communicationCost/getByPage', params)
}
// 添加
export const addCommunicationCost = (params) => {
    return postRequest('/communicationCost/insertOrUpdate', params)
}
// 编辑
export const editCommunicationCost = (params) => {
    return postRequest('/communicationCost/insertOrUpdate', params)
}
// 删除
export const deleteCommunicationCost = (params) => {
    return postRequest('/communicationCost/delByIds', params)
}