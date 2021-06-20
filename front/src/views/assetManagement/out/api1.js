// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getWarehouseOutList = (params) => {
    return getRequest('/warehouseOut/getByPage', params)
}
// 添加
export const addWarehouseOut = (params) => {
    return postRequest('/warehouseOut/insertOrUpdate', params)
}
// 编辑
export const editWarehouseOut = (params) => {
    return postRequest('/warehouseOut/insertOrUpdate', params)
}
// 删除
export const deleteWarehouseOut = (params) => {
    return postRequest('/warehouseOut/delByIds', params)
}