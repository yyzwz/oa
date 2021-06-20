// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getWarehousingList = (params) => {
    return getRequest('/warehousing/getByPage', params)
}
// 添加
export const addWarehousing = (params) => {
    return postRequest('/warehousing/insertOrUpdate', params)
}
// 编辑
export const editWarehousing = (params) => {
    return postRequest('/warehousing/insertOrUpdate', params)
}
// 删除
export const deleteWarehousing = (params) => {
    return postRequest('/warehousing/delByIds', params)
}