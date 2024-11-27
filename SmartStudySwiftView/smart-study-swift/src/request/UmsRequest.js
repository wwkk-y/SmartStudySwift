import baseUrl from "@/config/UrlConfig"
import BaseRequest from "./BaseRequest";

let umsBaseUrl = baseUrl + '/ums';
import { useTokenStore } from "@/stores/token";

/**
 * 登录 用户名和邮箱任选其一
 * @param {String} username 
 * @param {String} email 
 * @param {String} password 
 */
function login(username, email, password) {
    return BaseRequest.post(umsBaseUrl + '/ums/account/login', { username, email, password }).then(token => {
        useTokenStore().setToken(token);
        return token;
    })
}

function logout() {
    return BaseRequest.post(umsBaseUrl + '/ums/account/logout').then(() => {
        useTokenStore().setToken('');
    })
}

export default {
    login, logout
}