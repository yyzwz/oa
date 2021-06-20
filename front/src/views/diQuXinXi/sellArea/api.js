// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getSellAreaList = (params) => {
    return getRequest('/sellArea/getByPage', params)
}
// 添加
export const addSellArea = (params) => {
    return postRequest('/sellArea/insertOrUpdate', params)
}
// 编辑
export const editSellArea = (params) => {
    return postRequest('/sellArea/insertOrUpdate', params)
}
// 删除
export const deleteSellArea = (params) => {
    return postRequest('/sellArea/delByIds', params)
}
export const getAllArea = (params) => {
    return getRequest('/areaArchives/getAll', params)
}