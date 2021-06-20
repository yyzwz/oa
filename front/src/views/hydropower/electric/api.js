// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getElectricCostList = (params) => {
    return getRequest('/electricCost/getByPage', params)
}
// 添加
export const addElectricCost = (params) => {
    return postRequest('/electricCost/insertOrUpdate', params)
}
// 编辑
export const editElectricCost = (params) => {
    return postRequest('/electricCost/insertOrUpdate', params)
}
// 删除
export const deleteElectricCost = (params) => {
    return postRequest('/electricCost/delByIds', params)
}