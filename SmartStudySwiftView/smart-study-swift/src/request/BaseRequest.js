// {
//     code, message, data
// }

import { ElMessage } from "element-plus";
import { useTokenStore } from "@/stores/token";
import { useRouter } from "vue-router";


/**
 * 处理响应
 */
function dealResponse(res) {
    // 处理通用异常，返回真实数据
    if (res.code !== 200) {
        if (res.code === 401) {
            useTokenStore().setToken('')
            let router = useRouter();
            if (router) {
                router.push('/login')
            }
            ElMessage.error("请先登录");
        } else {
            if (res.message) {
                ElMessage.error(res.message);
            }
        }
        throw new Error(res.code + res.message);
    }
    return res.data
}

/**
 * 
 * @param {String} url 
 * @param {Object} body 
 * @returns 
 */
async function post(url, body) {
    return await fetch(
        url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': useTokenStore().token
        },
        body: JSON.stringify(body)
    }).then(response => response.json())
        .then(res => dealResponse(res))
        .catch(error => console.error('Error:', error))
}

/**
 * 参数加到param上
 * @param {String} url 
 * @param {Object} params 
 * @returns 
 */
async function postParam(url, params) {
    const queryParams = new URLSearchParams(params);
    // 构建完整的 URL，包括查询参数
    const fullUrl = `${url}?${queryParams.toString()}`;
    return await fetch(
        fullUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': useTokenStore().token
        },
    }).then(response => response.json())
        .then(res => dealResponse(res))
        .catch(error => console.error('Error:', error))
}

/**
 * 
 * @param {String} url 
 * @param {Object} params 
 * @returns 
 */
async function get(url, params) {
    const queryParams = new URLSearchParams(params);
    // 构建完整的 URL，包括查询参数
    const fullUrl = `${url}?${queryParams.toString()}`;
    return await fetch(
        fullUrl, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': useTokenStore().token
        }
    }).then(response => response.json())
        .then(res => dealResponse(res))
        .catch(error => console.error('Error:', error))
}

export default {
    post, get, postParam
}