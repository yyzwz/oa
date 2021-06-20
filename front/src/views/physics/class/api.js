// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getClassInformationList = (params) => {
    return getRequest('/classInformation/getByPage', params)
}
// 添加
export const addClassInformation = (params) => {
    return postRequest('/classInformation/insertOrUpdate', params)
}
// 编辑
export const editClassInformation = (params) => {
    return postRequest('/classInformation/insertOrUpdate', params)
}
// 删除
export const deleteClassInformation = (params) => {
    return postRequest('/classInformation/delByIds', params)
}