<template>
    <div id="chat">
        <div id="chat-left" style="overflow: hidden">
            <div style="display: flex; padding-left: 5px;">
                <el-input v-model="searchName" placeholder="搜索" :prefix-icon="Search" @input="searchChangedeDounce" />
                <el-button :icon="Plus" plain style="width: 20px; margin-left: 5px;" />
            </div>

            <!--  会话列表 -->
            <el-scrollbar max-height="80vh">
                <div v-infinite-scroll="loadConversation" :infinite-scroll-disabled="conversationLoading">
                    <el-menu :ellipsis="false">
                        <el-menu-item class="conversation-preview"
                            :class="{ active: conversationActive[conversation.id] }"
                            v-for="conversation of conversationList" :key="conversation.id" style="padding: 0;"
                            @click="clickConversation(conversation)">
                            <div style="display: flex; width: 100%; height: 100%; ">
                                <div
                                    style="width: 20%; height: 100%; display: flex; justify-content: center; align-items: center">
                                    <!-- 头像 -->
                                    <el-avatar shape="circle" :src="`${fileBaseUrl}/${conversation.icon}`" />
                                </div>
                                <div
                                    style="width: 100%; display: flex; flex-direction: column; margin-left: 5px; margin-top: 10px; margin-bottom: 10px">
                                    <div style="height: 50%; display: flex; align-items: center">
                                        <div style="margin-right: auto;">{{ conversation.nickName }}</div>
                                        <!-- 时间 conversation.updateTime -->
                                        <div style="margin-right: 15px;" v-if="conversation.updateTime">
                                            {{ formatDateString(conversation.updateTime) }}</div>
                                    </div>
                                    <div style="height: 50%; display: flex; align-items: center; opacity: 0.7;">
                                        <div style="margin-right: auto; font-size: small;">
                                            <template v-if="conversation.lastMsg">
                                                <div v-if="conversation.lastMsg.type === 'TEXT'">{{
                                                    conversation.lastMsg.content }}</div>
                                                <div v-if="conversation.lastMsg.type === 'IMAGE'">[图片]</div>
                                                <div v-if="conversation.lastMsg.type === 'FILE'">[文件]</div>
                                            </template>
                                            <template v-else>
                                                <div style="opacity: 0.3;"> 暂无消息 </div>
                                            </template>
                                        </div>
                                        <div style="margin-right: 15px;" v-show="conversation.unreadMsgNum > 0">
                                            <div style="background-color: rgb(255, 13, 13); line-height: 15px; border-radius: 10px; min-width: 20px; text-align: center;
                                                font-size: 10px; color: white;
                                                ">
                                                <template v-if="conversation.unreadMsgNum < 100">
                                                    {{ conversation.unreadMsgNum }}
                                                </template>
                                                <template v-else>
                                                    99+
                                                </template>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </el-menu-item>
                        <!-- <el-menu-item v-for="i of 100">{{ i }}</el-menu-item> -->
                        <el-menu-item v-show="conversationLoading" disabled
                            style="display: flex; justify-content: center;">加载中......</el-menu-item>
                        <el-tooltip content="点击刷新" placement="bottom">
                            <el-menu-item v-show="noMoreConversation"
                                style="display: flex; justify-content: center; opacity: 0.8;"
                                @click="reLoadConversation">
                                没有啦......

                            </el-menu-item>
                        </el-tooltip>
                    </el-menu>
                </div>
            </el-scrollbar>

        </div>
        <div class="h-gap" style="margin-top: 40px;"></div>
        <div id="chat-right">
            <!-- 聊天记录 -->
            <div style="height: 75%">
                <el-scrollbar @scroll="scrollMsg" ref="msgScrollBarRef">
                    <div style="margin: 10px 20px; 
                        height: 100%; 
                        /* background-color: bisque; */
                        display: flex; 
                        flex-direction: column-reverse; justify-content: flex-end;">
                        <!-- 发新消息时自动到最底部 -->
                        <div style="height: 2px;" ref="msgEndRef"></div>

                        <div v-for="msg of msgList" :key="msg.id" style="margin-bottom: 20px;">
                            <div v-if="msg.senderId !== curConversation.userId"
                                style="display: flex; flex-direction:row-reverse;">
                                <el-avatar shape="circle" :src="`${fileBaseUrl}/${tokenStore.getUser().icon}`" />
                                <div style="
                                    margin-right: 10px;
                                    margin-top: 5px;
                                    padding-left: 10px;
                                    padding-right: 10px;
                                    border-bottom-right-radius: 10px; 
                                    border-bottom-left-radius: 10px; 
                                    border-top-left-radius: 10px;
                                    background-color: #409EFF;
                                    display: flex;
                                    align-items: center;
                                    color: white;
                                    opacity: 0.8;
                                    max-width: 85%;
                                    word-break: break-all;
                                    ">
                                    <cms-msg-render :msg="msg"></cms-msg-render>
                                </div>
                            </div>
                            <div v-else style="text-align: left; display: flex; ">
                                <el-avatar shape="circle" :src="`${fileBaseUrl}/${curConversation.icon}`" />
                                <div style="
                                    margin-left: 10px;
                                    margin-top: 5px;
                                    padding-left: 10px;
                                    padding-right: 10px;
                                    border-bottom-right-radius: 10px; 
                                    border-bottom-left-radius: 10px; 
                                    border-top-right-radius: 10px;
                                    background-color: rgba(223, 223, 223, 0.3);
                                    display: flex;
                                    align-items: center;
                                    color: black;
                                    opacity: 0.8;
                                    max-width: 85%;
                                    word-break: break-all;
                                    ">
                                    <cms-msg-render :msg="msg"></cms-msg-render>
                                </div>
                            </div>
                        </div>
                        <div v-show="msgScrolling" style="text-align: center; opacity: 0.5;">加载中......</div>
                        <div v-show="noMoreMsg">
                            <div v-show="noMoreMsg" style="text-align: center; opacity: 0.5;">
                                <el-tooltip effect="dark" content="点击刷新" placement="bottom">
                                    <el-button style="border: none;" @click="reloadMsg">
                                        没有更早的消息了(/ω＼*)………
                                    </el-button>
                                </el-tooltip>
                            </div>
                        </div>

                    </div>

                </el-scrollbar>
            </div>
            <el-divider style="margin: 0" />
            <!-- 消息框 -->
            <div style="height: 25%; display: flex; flex-direction: column;">
                <div style="display: flex; margin-top: 5px; margin-bottom: 5px">
                    <el-icon style="margin-left: 10px" size="23">
                        <Picture />
                    </el-icon>
                    <el-icon style="margin-left: 10px" size="23">
                        <Folder />
                    </el-icon>
                </div>
                <div style="height: 65%; overflow-y: hidden;  margin-left: 8px;">
                    <textarea v-model="msgInput" @keyup.enter="sendTextMsg" @keyup.shift.space="newLine" style="width: 100%; height: 100%; 
                    border: none; outline: none;
                    font-family: 'PingFang SC', 'Microsoft YaHei', Arial, sans-serif;"></textarea>
                </div>
                <div style="text-align: right; margin-right: 10px; margin-top: 10px; ">
                    <el-tooltip effect="dark" content="按Enter键发送消息，按Alt+Enter键换行">
                        <el-button @click="sendTextMsg" :disabled="msgSending">发送</el-button>
                    </el-tooltip>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { nextTick, ref, watch } from 'vue';
import { Search, Plus } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router';
import { useTokenStore } from '@/stores/token';
import CmsRequest from '@/request/CmsRequest'
import { debounce } from 'throttle-debounce';
import { fileBaseUrl } from "@/config/UrlConfig";
import { formatDateString } from '@/util/TimeUtil'
import CmsMsgRender from '@/components/CmsMsgRender.vue';
import { ElMessage } from 'element-plus';
import WebsokcetHandler from '@/request/WebsokcetHandler';

let router = useRouter();
let tokenStore = useTokenStore();

if (!tokenStore.token) {
    router.push('/login')
}

let searchName = ref("")

// 会话列表
let conversationMaxPage = {
    pageNum: 1,
    pageSize: 40
}
let conversationList = ref([])

let conversationLoading = ref(false)
let noMoreConversation = ref(false)

function queryConversationList() {
    if (conversationLoading.value) {
        return;
    }
    if (noMoreConversation.value) {
        return;
    }

    conversationLoading.value = true;
    CmsRequest.queryConversationList(conversationMaxPage.pageNum, conversationMaxPage.pageSize, searchName.value).then(page => {
        if (page && page.list && page.list.length > 0) {
            conversationList.value.push(...page.list);
            conversationMaxPage.pageNum = page.pageNum;
            conversationMaxPage.pageSize = page.pageSize;
            if (conversationMaxPage.pageNum >= page.totalPage) {
                noMoreConversation.value = true;
            }
        } else {
            noMoreConversation.value = true;
        }
        conversationLoading.value = false;
    })
}

queryConversationList();

// 按照更新时间排序
function sortConversationList() {
    conversationList.value.sort((a, b) => {
        // 将 str 转换为 Date 对象
        if (!(a.updateTime instanceof Date)) {
            a.updateTime = new Date(a.updateTime);
        }
        if (!(b.updateTime instanceof Date)) {
            b.updateTime = new Date(b.updateTime);
        }

        // 倒序
        return b.updateTime - a.updateTime;
    })
}

// 搜索框
function searchChange() {
    conversationMaxPage.pageNum = 1;
    conversationList.value = []
    queryConversationList();
}

let searchChangedeDounce = debounce(500, () => searchChange())

// TODO 下拉加载会话
function loadConversation() {
    if (noMoreConversation.value) {
        return;
    }
    if (conversationLoading.value) {
        return;
    }

    conversationLoading.value = true;
    console.log('load')

    // ** 
    setTimeout(() => {
        for (let i = 0; i < 40; ++i) {
            conversationList.value.push({ id: 'hello' + i, nickName: 'hello ' + i })
        }
        console.log(conversationList.value.length)
        conversationLoading.value = false;
    }, 1000)
    // */
}

// 刷新
function reLoadConversation() {
    conversationMaxPage.pageNum = 1;
    conversationList.value = [];
    noMoreConversation.value = false;
    conversationLoading.value = false;
    queryConversationList()
}

// 选中样式
let conversationActive = ref({})

// 选中会话时执行
function clickConversation(conversation) {
    // 设置选中的样式类包含active
    conversationActive.value = {}
    conversationActive.value[conversation.id] = true
    if (curConversation.value.id !== conversation.id) {
        curConversation.value = conversation;
        msgMaxPage.pageNum = 1;
        msgList.value = [];
        msgLoading.value = false;
        noMoreMsg.value = false;
        requestMsg().then(() => msgScrollToBottomNextTick(true));
    }
}


let msgMaxPage = {
    pageNum: 1,
    pageSize: 50
}
let msgList = ref([])
let curConversation = ref({});
let msgLoading = ref(false)
let noMoreMsg = ref(false)

// 请求消息
async function requestMsg() {
    if (msgLoading.value) {
        return;
    }
    if (noMoreMsg.value) {
        return;
    }
    if (!curConversation.value.id) {
        return;
    }

    msgLoading.value = true
    return await CmsRequest.queryMessageOfConversation(msgMaxPage.pageNum, msgMaxPage.pageSize, curConversation.value.id).then(page => {
        if (page && page.list && page.list.length > 0) {
            msgList.value.push(...page.list);
            msgMaxPage.pageNum = page.pageNum;
            msgMaxPage.pageSize = page.pageSize;
            if (msgMaxPage.pageNum >= page.totalPage) {
                noMoreMsg.value = true;
            }
        } else {
            noMoreMsg.value = true;
        }
        msgLoading.value = false;
    })
}

// 刷新消息
function reloadMsg() {
    msgMaxPage.pageNum = 1;
    msgList.value = [];
    msgLoading.value = false;
    noMoreMsg.value = false;
    requestMsg().then(res => msgScrollToBottomNextTick());
}

// 上拉加载消息
let msgScrollBarRef = ref(null);
let msgScrolling = ref(false);
function scrollMsg({ scrollLeft, scrollTop }) {
    if (scrollTop === 0) {
        // load more message 
        if (msgLoading.value) {
            return;
        }
        if (noMoreMsg.value) {
            return;
        }
        if (msgScrolling.value) {
            return;
        }
        msgScrolling.value = true;

        msgMaxPage.pageNum += 1;
        setTimeout(() => {
            requestMsg().then(() => {
                msgScrollBarRef.value.setScrollTop(2); // 避免下次加载需要重新到顶部
                msgScrolling.value = false
            })
        }, 1000)

    }
}

const msgEndRef = ref(null); // 消息滚动条实例

function msgScrollToBottom(firstLoad) {
    // 滚动到最底部
    if (msgEndRef.value) {
        if (firstLoad) {
            msgEndRef.value.scrollIntoView({ block: 'end' });
        } else {
            msgEndRef.value.scrollIntoView({ behavior: 'smooth', block: 'end' });
        }
    }
}

function msgScrollToBottomNextTick(firstLoad) {
    nextTick(() => {
        msgScrollToBottom(firstLoad);
    })
}

// 发送消息
let msgInput = ref("")
let msgSending = ref(false);
function sendTextMsg() {
    if (msgSending.value) {
        return false;
    }

    if (!curConversation.value.id) {
        ElMessage.warning('请先选择一个会话');
        return;
    }

    msgSending.value = true;
    CmsRequest.sendMsg(curConversation.value.id, 'TEXT', msgInput.value).then(newMsg => {
        if (newMsg) {
            msgList.value.unshift(newMsg);
            msgScrollToBottomNextTick();

            // 会话
            curConversation.value.lastMsgId = newMsg.id;
            curConversation.value.lastMsg = newMsg;
            curConversation.value.updateTime = newMsg.createTime;
            sortConversationList();
        }
        msgSending.value = false;
    })
    msgInput.value = ''
}

// 换行
function newLine() {
    console.log('new line')
    msgInput.value += `
`
}

// 消息处理器
WebsokcetHandler.registerListener('/notice/msg/chat', (newMsg) => { // FIXME 常量
    newMsg = JSON.parse(newMsg);
    console.log(newMsg)
    // FIXME conversationId deal conversation sort
    msgList.value.unshift(newMsg);
})

</script>

<style scoped>
#chat {
    display: flex;
    width: 100%;
    border: 1px solid rgba(0, 0, 0, 0.115);
    height: 85vh;
    border-radius: 16px;
    /* 设置圆角 */
    overflow: hidden;
    /* 确保内部元素不会溢出 */

    padding-top: 10px;
    padding-bottom: 10px;
}

.h-gap {
    width: 2px;
    background: rgba(144, 139, 139, 0.141);
}

#chat-left {
    width: 40%;
    /* background: rgba(97, 67, 67, 0.093); */
    overflow-y: auto;
    /* padding-left: 5px; */
    /* 垂直方向上允许滚动 */
}

#chat-right {
    display: flex;

    flex-direction: column;
    width: 100%;
    /* background: rgba(17, 9, 58, 0.107); */
    overflow-y: auto;
    /* 垂直方向上允许滚动 */
}

.test {
    color: rgba(43, 180, 38, 0.959);
}

/* 选中的会话项的样式 */
.conversation-preview:hover,
.conversation-preview.active {
    background-color: rgb(235, 245, 255);
    /* 注意：RGB值应为整数 */
    color: #409EFF;
}


/* 整个滚动条 */
textarea::-webkit-scrollbar {
    width: 5px;
    /* 滚动条宽度 */
}

/* 滚动条轨道 */
textarea::-webkit-scrollbar-track {
    background: #f1f1f1;
    /* 轨道背景色 */
}

/* 滚动条滑块 */
textarea::-webkit-scrollbar-thumb {
    background: #88888848;
    /* 滑块颜色 */
    border-radius: 5px;
    /* 圆角效果 */
}

/* 当鼠标悬停时滑块的颜色 */
textarea::-webkit-scrollbar-thumb:hover {
    background: #555;
    /* 鼠标悬停时滑块颜色变化 */
}

/* Firefox滚动条宽度 */
textarea {
    scrollbar-width: thin;
    /* 设置为细 */
    scrollbar-color: #88888848 #f1f1f1;
    /* 滑块颜色 轨道颜色 */
}

.hover-back-primary:hover {
    background-color: rgb(216.8, 235.6, 255);
}
</style>