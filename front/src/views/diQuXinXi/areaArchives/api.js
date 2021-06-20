// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getAreaArchivesList = (params) => {
    return getRequest('/areaArchives/getByPage', params)
}
// 添加
export const addAreaArchives = (params) => {
    return postRequest('/areaArchives/insertOrUpdate', params)
}
// 编辑
export const editAreaArchives = (params) => {
    return postRequest('/areaArchives/insertOrUpdate', params)
}
// 删除
export const deleteAreaArchives = (params) => {
    return postRequest('/areaArchives/delByIds', params)
}
export const getAllBigArea = (params) => {
    return getRequest('/bigArea/getAll', params)
}
export const getSonCompany = (params) => {
    return getRequest('/sonCompany/getAll', params)
}
export const getByFenGongSi = (params) => {
    return getRequest('/bigArea/getByFenGongSi', params)
}