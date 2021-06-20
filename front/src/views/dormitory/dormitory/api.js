// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getDormitoryList = (params) => {
    return getRequest('/dormitory/getByPage', params)
}
// 添加
export const addDormitory = (params) => {
    return postRequest('/dormitory/insertOrUpdate', params)
}
// 编辑
export const editDormitory = (params) => {
    return postRequest('/dormitory/insertOrUpdate', params)
}
// 删除
export const deleteDormitory = (params) => {
    return postRequest('/dormitory/delByIds', params)
}