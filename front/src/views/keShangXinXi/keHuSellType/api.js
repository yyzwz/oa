// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getKeHuSellTypeList = (params) => {
    return getRequest('/keHuSellType/getByPage', params)
}
// 添加
export const addKeHuSellType = (params) => {
    return postRequest('/keHuSellType/insertOrUpdate', params)
}
// 编辑
export const editKeHuSellType = (params) => {
    return postRequest('/keHuSellType/insertOrUpdate', params)
}
// 删除
export const deleteKeHuSellType = (params) => {
    return postRequest('/keHuSellType/delByIds', params)
}