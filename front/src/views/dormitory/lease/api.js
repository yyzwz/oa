// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getDormitoryLeaseList = (params) => {
    return getRequest('/dormitoryLease/getByPage', params)
}
// 添加
export const addDormitoryLease = (params) => {
    return postRequest('/dormitoryLease/insertOrUpdate', params)
}
// 编辑
export const editDormitoryLease = (params) => {
    return postRequest('/dormitoryLease/insertOrUpdate', params)
}
// 删除
export const deleteDormitoryLease = (params) => {
    return postRequest('/dormitoryLease/delByIds', params)
}