// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getAssetsTypeList = (params) => {
    return getRequest('/assetsType/getByPage', params)
}
// 添加
export const addAssetsType = (params) => {
    return postRequest('/assetsType/insertOrUpdate', params)
}
// 编辑
export const editAssetsType = (params) => {
    return postRequest('/assetsType/insertOrUpdate', params)
}
// 删除
export const deleteAssetsType = (params) => {
    return postRequest('/assetsType/delByIds', params)
}