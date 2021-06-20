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
    return getRequest('/keShangType/getByAllSubInTreeByParentId/0', params)
}
// 获取一级角色
export const initJueSeManger = (params) => {
    return getRequest('/keShangType/getByParentId/0', params)
}
// 加载角色子级数据
export const loadJueSeManger = (id, params) => {
    return getRequest(`/keShangType/getByParentId/${id}`, params)
}
// 添加角色
export const addJueSeManger = (params) => {
    return postRequest('/keShangType/add', params)
}
// 编辑角色
export const editJueSeManger = (params) => {
    return postRequest('/keShangType/edit', params)
}
// 删除角色
export const deleteJueSeManger = (params) => {
    return postRequest('/keShangType/delByIds', params)
}
// 搜索角色
export const searchJueSeManger = (params) => {
    return getRequest('/keShangType/search', params)
}