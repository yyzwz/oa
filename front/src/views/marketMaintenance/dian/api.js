// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getDianMianList = (params) => {
    return getRequest('/dianMian/getByPage', params)
}
// 添加
export const addDianMian = (params) => {
    return postRequest('/dianMian/insertOrUpdate', params)
}
// 编辑
export const editDianMian = (params) => {
    return postRequest('/dianMian/insertOrUpdate', params)
}
// 删除
export const deleteDianMian = (params) => {
    return postRequest('/dianMian/delByIds', params)
}
export const getAllShengShiXian = (params) => {
    return getRequest('/xian/getAllShengShiXian', params)
}