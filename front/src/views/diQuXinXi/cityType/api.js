// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getCityTypeList = (params) => {
    return getRequest('/cityType/getByPage', params)
}
// 添加
export const addCityType = (params) => {
    return postRequest('/cityType/insertOrUpdate', params)
}
// 编辑
export const editCityType = (params) => {
    return postRequest('/cityType/insertOrUpdate', params)
}
// 删除
export const deleteCityType = (params) => {
    return postRequest('/cityType/delByIds', params)
}