// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getLaboratoryInformationList = (params) => {
    return getRequest('/laboratoryInformation/getByPage', params)
}
// 添加
export const addLaboratoryInformation = (params) => {
    return postRequest('/laboratoryInformation/insertOrUpdate', params)
}
// 编辑
export const editLaboratoryInformation = (params) => {
    return postRequest('/laboratoryInformation/insertOrUpdate', params)
}
// 删除
export const deleteLaboratoryInformation = (params) => {
    return postRequest('/laboratoryInformation/delByIds', params)
}