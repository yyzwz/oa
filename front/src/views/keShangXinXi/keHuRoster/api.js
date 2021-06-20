// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getKeHuRosterList = (params) => {
    return getRequest('/keHuRoster/getByPage', params)
}
// 添加
export const addKeHuRoster = (params) => {
    return postRequest('/keHuRoster/insertOrUpdate', params)
}
// 编辑
export const editKeHuRoster = (params) => {
    return postRequest('/keHuRoster/insertOrUpdate', params)
}
// 删除
export const deleteKeHuRoster = (params) => {
    return postRequest('/keHuRoster/delByIds', params)
}
export const getKeShangTypeNotParent = (params) => {
    return getRequest('/keShangType/getNotParent', params)
}
export const getAllShengShiXian = (params) => {
    return getRequest('/xian/getAllShengShiXian', params)
}
export const getXianByName = (params) => {
    return getRequest('/xian/findByName', params)
}
export const getShinByName = (params) => {
    return getRequest('/shi/findByName', params)
}
export const findAllCityLevel = (params) => {
    return getRequest('/cityLevel/getAll', params)
}
export const findAllCityType = (params) => {
    return getRequest('/cityType/getAll', params)
}
export const getAllCountyType = (params) => {
    return getRequest('/countyType/getAll', params)
}