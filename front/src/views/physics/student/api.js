// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

// 分页获取数据
export const getStudentList = (params) => {
    return getRequest('/student/getByPage', params)
}
// 添加
export const addStudent = (params) => {
    return postRequest('/student/insertOrUpdate', params)
}
// 编辑
export const editStudent = (params) => {
    return postRequest('/student/insertOrUpdate', params)
}
// 删除
export const deleteStudent = (params) => {
    return postRequest('/student/delByIds', params)
}