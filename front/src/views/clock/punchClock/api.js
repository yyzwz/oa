// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getPunchClockList = (params) => {
    return getRequest('/punchClock/getByPage', params)
}
// 添加
export const addPunchClock = (params) => {
    return postRequest('/punchClock/insertOrUpdate', params)
}
// 编辑
export const editPunchClock = (params) => {
    return postRequest('/punchClock/insertOrUpdate', params)
}
// 删除
export const deletePunchClock = (params) => {
    return postRequest('/punchClock/delByIds', params)
}