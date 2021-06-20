// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getShengList = (params) => {
    return getRequest('/sheng/getByPage', params)
}
// 添加
export const addSheng = (params) => {
    return postRequest('/sheng/insertOrUpdate', params)
}
// 编辑
export const editSheng = (params) => {
    return postRequest('/sheng/insertOrUpdate', params)
}
// 删除
export const deleteSheng = (params) => {
    return postRequest('/sheng/delByIds', params)
}
export const getAllAreaList = (params) => {
    return getRequest('/areaArchives/getAll', params)
}