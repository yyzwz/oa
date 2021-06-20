// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getCityLevelList = (params) => {
    return getRequest('/cityLevel/getByPage', params)
}
// 添加
export const addCityLevel = (params) => {
    return postRequest('/cityLevel/insertOrUpdate', params)
}
// 编辑
export const editCityLevel = (params) => {
    return postRequest('/cityLevel/insertOrUpdate', params)
}
// 删除
export const deleteCityLevel = (params) => {
    return postRequest('/cityLevel/delByIds', params)
}
export const getAllCityType = (params) => {
    return getRequest('/cityType/getAll', params)
}