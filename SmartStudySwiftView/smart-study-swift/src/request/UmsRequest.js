import {umsBaseUrl} from "@/config/UrlConfig"
import BaseRequest from "./BaseRequest";

import { useTokenStore } from "@/stores/token";
import { ElMessage } from "element-plus";

/**
 * 校验邮箱
 * @param {String} email 
 * @returns 
 */
function isValidEmail(email) {
    // 定义一个正则表达式，用来匹配大多数标准的电子邮件格式
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    
    // 使用test方法来检查email是否与正则表达式匹配
    return emailRegex.test(email);
}

/**
 * 登录 用户名和邮箱任选其一
 * @param {String} usernameOrEmail 
 * @param {String} password 
 */
async function login(usernameOrEmail, password) {
    let body = {password}
    if(isValidEmail(usernameOrEmail)){
        body.email = usernameOrEmail;
    } else {
        body.username = usernameOrEmail;
    }

    return await BaseRequest.post(umsBaseUrl + '/ums/account/login', body).then(token => {
        let tokenStore = useTokenStore();
        tokenStore.setToken(token);
        return token;
    })
}

async function logout() {
    return await BaseRequest.post(umsBaseUrl + '/ums/account/logout').then((msg) => {
        useTokenStore().setToken('');
        return msg;
    })
}

/**
 * 注册用户
 * @param {String} username 
 * @param {String} nickName 
 * @param {String} email 
 * @param {String} password 
 */
async function register(username, nickName, email, password){
    let valid = true;
    if(!username){
        ElMessage.warning('用户名不能为空');
        valid = false;
    }
    if(!nickName){
        ElMessage.warning('昵称不能为空');
        valid = false;
    }
    if(!email){
        ElMessage.warning('邮箱不能为空');
        valid = false;
    }
    if(!password){
        ElMessage.warning('密码不能为空');
        valid = false;
    }
    if(!isValidEmail(email)){
        ElMessage.warning('邮箱不合法');
        valid = false;
    }

    if(valid){
        return await BaseRequest.post(umsBaseUrl + '/ums/account/register', {username, nickName, email, password});
    }
}

/**
 * 获取用户信息
 */
function accountInfo(){
    return BaseRequest.get(umsBaseUrl + '/ums/account/info');
}

/**
 * 查询用户信息
 * @param {Number} pageNum 必须 当前页
 * @param {Number} pageSize 必须 每页条数
 * @param {String} name 非必须 用户名或昵称
 */
async function queryUserList(pageNum, pageSize, name){
    if (pageNum <= 0 || pageSize <= 0) {
        console.error("queryUserList error", pageNum, pageSize);
        return;
    }

    return await BaseRequest.get(`${umsBaseUrl}/user/list`, {pageNum, pageSize, name});
}


export default {
    login, logout, register, accountInfo, queryUserList
}