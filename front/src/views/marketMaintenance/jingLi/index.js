// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';


// Vaptcha ID
export const vaptchaID = "5dce36188713b71e70a41eb7"
// 文件上传接口
export const uploadFile = "/xboot/upload/file"
// 验证码渲染图片接口
export const drawCodeImage = "/xboot/common/captcha/draw/"
// 获取菜单
export const getMenuList = "/xboot/permission/getMenuList"
// 获取数据字典
export const getDictData = "/xboot/dictData/getByType/"
// Websocket
export const ws = "/xboot/ws"

// ============================================================
// 角色 角色 角色

// 获取全部角色
export const getAllJueSeMangerInTree = (params) => {
    return getRequest('/jingLi/getByAllSubInTreeByParentId/0', params)
}
// 获取一级角色
export const initJueSeManger = (params) => {
    return getRequest('/jingLi/getByParentId/0', params)
}
// 加载角色子级数据
export const loadJueSeManger = (id, params) => {
    return getRequest(`/jingLi/getByParentId/${id}`, params)
}
// 添加角色
export const addJueSeManger = (params) => {
    return postRequest('/jingLi/add', params)
}
// 编辑角色
export const editJueSeManger = (params) => {
    return postRequest('/jingLi/edit', params)
}
// 删除角色
export const deleteJueSeManger = (params) => {
    return postRequest('/jingLi/delByIds', params)
}
// 搜索角色
export const searchJueSeManger = (params) => {
    return getRequest('/jingLi/search', params)
}
export const getUserList = (params) => {
    return getRequest('/user/getByCondition', params)
}
export const importJingLiById = (params) => {
    return postRequest('/jingLi/importById', params)
}
export const getDianList = (params) => {
    return getRequest('/dianAndJing/getByLi', params)
}
export const deleteJAD = (params) => {
    return postRequest('/dianAndJing/deleteJAD', params)
}
export const getDianByNotImport = (params) => {
    return getRequest('/dianAndJing/getByNotImport', params)
}
export const importMore = (params) => {
    return postRequest('/dianAndJing/importMore', params)
}