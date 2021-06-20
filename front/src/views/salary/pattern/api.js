// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getPatternList = (params) => {
    return getRequest('/pattern/getByPage', params)
}
// 添加
export const addPattern = (params) => {
    return postRequest('/pattern/insertOrUpdate', params)
}
// 编辑
export const editPattern = (params) => {
    return postRequest('/pattern/insertOrUpdate', params)
}
// 删除
export const deletePattern = (params) => {
    return postRequest('/pattern/delByIds', params)
}