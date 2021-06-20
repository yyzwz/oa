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


export const getUserByPermission = (params) => {
    return getRequest('/permission/getUserByPermission', params)
}