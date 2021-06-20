// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getDaoDianMessageList = (params) => {
    return getRequest('/daoDianMessage/getByPage', params)
}
// 添加
export const addDaoDianMessage = (params) => {
    return postRequest('/daoDianMessage/insertOrUpdate', params)
}
// 编辑
export const editDaoDianMessage = (params) => {
    return postRequest('/daoDianMessage/insertOrUpdate', params)
}
// 删除
export const deleteDaoDianMessage = (params) => {
    return postRequest('/daoDianMessage/delByIds', params)
}
export const getByDaoId = (params) => {
    return getRequest('/dianPingImage/getByDaoId', params)
}
export const addDianPing = (params) => {
    return postRequest('/daoDianDianPing/insertOrUpdate', params)
}
export const getDianPingByDianId = (params) => {
    return getRequest('/daoDianDianPing/getByDianId', params)
}
export const deleteDianPing = (params) => {
    return postRequest('/daoDianDianPing/delById', params)
}
