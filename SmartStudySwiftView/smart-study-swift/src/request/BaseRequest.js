// {
//     code, message, data
// }

import { ElMessage } from "element-plus";

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
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    }).then(response => response.json()).then(res => {
        // 处理通用异常，返回真实数据
        if(res.code !== 200){
            if(res.message){
                ElMessage.error(res.message);
            }
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