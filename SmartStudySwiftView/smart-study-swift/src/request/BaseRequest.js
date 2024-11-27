// {
//     code, message, data
// }

import { ElMessage } from "element-plus";
import { useTokenStore } from "@/stores/token";
import { useRouter } from 'vue-router';

/**
 * 
 * @param {String} url 
 * @param {Object} body 
 * @returns 
 */
function post(url, body) {
    return fetch(
        url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': useTokenStore().getToken()
        },
        body: JSON.stringify(body)
    }).then(response => response.json()).then(res => {
        // 处理通用异常，返回真实数据
        if(res.code !== 200){
            if(res.message){
                ElMessage.error(res.message);
            }
            if(res.code === 401){
                useTokenStore().setToken('')
            }
            throw new Error(res.code + res.message);
        } 
        return res.data
    }).catch(error => console.error('Error:', error)) 
}

function get() {

}

function queryPage(){
    
}

export default {
    post, get, queryPage
}