// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getFineList = (params) => {
    return getRequest('/fine/getByPage', params)
}
// 添加
export const addFine = (params) => {
    return postRequest('/fine/insertOrUpdate', params)
}
// 编辑
export const editFine = (params) => {
    return postRequest('/fine/insertOrUpdate', params)
}
// 删除
export const deleteFine = (params) => {
    return postRequest('/fine/delByIds', params)
}