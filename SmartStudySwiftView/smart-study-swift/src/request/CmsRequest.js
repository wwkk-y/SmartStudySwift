import { cmsBaseUrl } from "@/config/UrlConfig";
import BaseRequest from "./BaseRequest";

/**
 * 查询当前用户的会话
 * @param {Number} pageNum 必须 当前页
 * @param {Number} pageSize 必须 每页条数
 * @param {String} targetName 非必须 对方名字
 */
async function queryConversationList(pageNum, pageSize, targetName) {
    if (pageNum <= 0 || pageSize <= 0) {
        console.error("queryConversationList error", pageNum, pageSize);
    }

    return await BaseRequest.get(`${cmsBaseUrl}/cms/conversation/list`, { pageNum, pageSize, targetName })
}

/**
 * 查询某个会话的消息
 * @param {Number} pageNum 必须 当前页
 * @param {Number} pageSize 必须 每页条数
 * @param {Number} conversationId 必须 会话id
 */
async function queryMessageOfConversation(pageNum, pageSize, conversationId) {
    if (pageNum <= 0 || pageSize <= 0) {
        console.error("queryConversationList error", pageNum, pageSize);
        return;
    }

    return await BaseRequest.get(`${cmsBaseUrl}/cms/conversation/msg/list`, { pageNum, pageSize, conversationId });
}

/**
 * 发消息
 * @param {Number} conversationId 必须 会话id
 * @param {String} type 必须 消息类型 'TEXT'|'IMAGE'|'FILE'
 * @param {String} content 必须 消息内容，格式见文档
 */
async function sendMsg(conversationId, type, content) {
    if (!conversationId || !type || !content) {
        console.error("sendMsg error", conversationId, type, content);
        return;
    }

    return await BaseRequest.post(`${cmsBaseUrl}/message/send`, {conversationId, type, content});
}


export default {
    queryConversationList, queryMessageOfConversation, sendMsg
}