// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getInsuranceList = (params) => {
    return getRequest('/insurance/getByPage', params)
}
// 添加
export const addInsurance = (params) => {
    return postRequest('/insurance/save', params)
}
// 编辑
export const editInsurance = (params) => {
    return postRequest('/insurance/edit', params)
}
// 删除
export const deleteInsurance = (params) => {
    return postRequest('/insurance/delByIds', params)
}