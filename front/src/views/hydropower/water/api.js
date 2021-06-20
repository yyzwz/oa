// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getWaterCostList = (params) => {
    return getRequest('/waterCost/getByPage', params)
}
// 添加
export const addWaterCost = (params) => {
    return postRequest('/waterCost/insertOrUpdate', params)
}
// 编辑
export const editWaterCost = (params) => {
    return postRequest('/waterCost/insertOrUpdate', params)
}
// 删除
export const deleteWaterCost = (params) => {
    return postRequest('/waterCost/delByIds', params)
}