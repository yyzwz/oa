import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';
export const getArchiveListData = (params) => {
    return getRequest('/archive/getByPage', params)
}
// 通过部门档案名搜索
export const searchDepartmentArchiveByName = (departmentArchivename, params) => {
    return getRequest('/archive/searchByName/'+departmentArchivename, params)
}
// 获取全部部门档案数据
export const getAllDepartmentArchiveData = (params) => {
    return getRequest('/archive/getAll', params)
}
// 通过部门获取全部部门档案数据
export const getDepartmentArchiveByDepartmentId = (id, params) => {
    return getRequest(`/archive/getByDepartmentId/${id}`, params)
}
// 编辑部门档案
export const editArchive = (params) => {
    return postRequest('/archive/insertOrUpdate', params)
}
// 启用部门档案
export const enableDepartmentArchive = (id, params) => {
    return postRequest(`/archive/enable/${id}`, params)
}
// 禁用部门档案
export const disableDepartmentArchive = (id, params) => {
    return postRequest(`/archive/disable/${id}`, params)
}
// 删除部门档案
export const deleteDepartmentArchive = (params) => {
    return postRequest('/archive/delByIds', params)
}
// 导入部门档案
export const importDepartmentArchiveData = (params) => {
    return postBodyRequest('/archive/importData', params)
}