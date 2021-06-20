// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

export const importDepartmentByHua = (params) => {
    return getRequest(`/department/importDepartByHua`, params)
}

export const getNotImportUserByDepartmentByPage = (params) => {
    return getRequest('/roster/getNotImportUserByDepartmentByPage', params)
}
export const getDepartmentArchiveListData = (params) => {
    return getRequest('/archive/getByPage', params)
}
export const importFromUser = (params) => {
    return postRequest('/department/importFromUser', params)
}
// 添加用户
export const addUser = (params) => {
    return postRequest('/user/admin/add', params)
}
// 编辑用户
export const editUser = (params) => {
    return postRequest('/user/admin/edit', params)
}
// 获取全部角色数据
export const getAllRoleList = (params) => {
    return getRequest('/role/getAllList', params)
}
// 分页获取角色数据
export const getRoleList = (params) => {
    return getRequest('/role/getAllByPage', params)
}
// 花名册api接口 
export const getHuaListData = (params) => {
    return getRequest('/zwzRosterUser/getByPage', params)
}
// 通过用户名搜索
export const searchDepartmentArchiveByName = (departmentArchivename, params) => {
    return getRequest('/departmentArchive/searchByName/'+departmentArchivename, params)
}
// 获取全部用户数据
export const getAllDepartmentArchiveData = (params) => {
    return getRequest('/departmentArchive/getAll', params)
}
// 通过部门获取全部用户数据
export const getDepartmentArchiveByDepartmentId = (id, params) => {
    return getRequest(`/departmentArchive/getByDepartmentId/${id}`, params)
}
// 添加用户
export const addDepartmentArchive = (params) => {
    return postRequest('/departmentArchive/add', params)
}
// 编辑用户
export const editDepartmentArchive = (params) => {
    return postRequest('/departmentArchive/edit', params)
}
// 启用用户
export const enableDepartmentArchive = (id, params) => {
    return postRequest(`/departmentArchive/enable/${id}`, params)
}
// 禁用用户
export const disableDepartmentArchive = (id, params) => {
    return postRequest(`/departmentArchive/disable/${id}`, params)
}
// 删除用户
export const deleteDepartmentArchive = (params) => {
    return postRequest('/departmentArchive/delByIds', params)
}
// 导入用户
export const importDepartmentArchiveData = (params) => {
    return postBodyRequest('/departmentArchive/importData', params)
}
// 重置用户密码
export const resetDepartmentArchivePass = (params) => {
    return postRequest('/departmentArchive/resetPass', params)
}

