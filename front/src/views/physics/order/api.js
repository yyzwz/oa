// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getLaboratoryOrderList = (params) => {
    return getRequest('/laboratoryOrder/getByPage', params)
}
// 添加
export const addLaboratoryOrder = (params) => {
    return postRequest('/laboratoryOrder/insertOrUpdate', params)
}
// 编辑
export const editLaboratoryOrder = (params) => {
    return postRequest('/laboratoryOrder/insertOrUpdate', params)
}
// 删除
export const deleteLaboratoryOrder = (params) => {
    return postRequest('/laboratoryOrder/delByIds', params)
}