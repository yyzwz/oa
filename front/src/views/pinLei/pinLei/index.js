// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 获取全部角色
export const getAllJueSeMangerInTree = (params) => {
    return getRequest('/pinLei/getByAllSubInTreeByParentId/0', params)
}
// 获取一级角色
export const initJueSeManger = (params) => {
    return getRequest('/pinLei/getByParentId/0', params)
}
// 加载角色子级数据
export const loadJueSeManger = (id, params) => {
    return getRequest(`/pinLei/getByParentId/${id}`, params)
}
// 添加角色
export const addJueSeManger = (params) => {
    return postRequest('/pinLei/add', params)
}
// 编辑角色
export const editJueSeManger = (params) => {
    return postRequest('/pinLei/edit', params)
}
// 删除角色
export const deleteJueSeManger = (params) => {
    return postRequest('/pinLei/delByIds', params)
}
// 搜索角色
export const searchJueSeManger = (params) => {
    return getRequest('/pinLei/search', params)
}