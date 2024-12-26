<template>
    <div id="chat">
        <div id="chat-left" style="overflow: hidden">
            <div style="display: flex; padding-left: 5px;">
                <el-input v-model="searchName" placeholder="搜索" :prefix-icon="Search"
                    @input="reLoadConversationDebounce" />
                <el-button :icon="Plus" plain style="width: 20px; margin-left: 5px;" @click="initfindUserDialog" />
            </div>

            <!--  会话列表 -->
            <el-scrollbar max-height="80vh">
                <div v-infinite-scroll="loadConversation" :infinite-scroll-disabled="conversationLoading">
                    <el-menu :ellipsis="false">
                        <!-- 临时会话 当会话列表里没有这些会话时才加载-->
                        <el-menu-item class="conversation-preview"
                            :class="{ active: conversationActive[conversation.id] }"
                            v-for="conversation of tempConversationList" :key="conversation.id" style="padding: 0;"
                            @click="selectConversation(conversation)" v-show="!conversationLoad[conversation.id]">
                            <cms-conversation-list-item :conversation="conversation" />
                        </el-menu-item>
                        <!-- 会话列表 -->
                        <el-menu-item class="conversation-preview"
                            :class="{ active: conversationActive[conversation.id] }"
                            v-for="conversation of conversationList" :key="conversation.id" style="padding: 0;"
                            @click="selectConversation(conversation)">
                            <cms-conversation-list-item :conversation="conversation"></cms-conversation-list-item>
                        </el-menu-item>
                        <!-- 加载状态 -->
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
                                <!-- TODO发消息时渲染状态 发送中 - 发送成功 - 发送失败 -->
                                <template v-if="msg.sendState === 1">
                                    <!-- 发送中 -->
                                    发送中
                                </template>
                                <template v-else-if="msg.sendState === 2">
                                    <!-- 发送失败 -->
                                    发送失败
                                </template>
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
            <!-- 消息输入框 -->
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
                    <textarea v-model="msgInput" @keyup.enter="sendTextMsg(msgInput)" @keyup.shift.space="newLine" style="width: 100%; height: 100%; 
                    border: none; outline: none;
                    font-family: 'PingFang SC', 'Microsoft YaHei', Arial, sans-serif;"></textarea>
                </div>
                <div style="text-align: right; margin-right: 10px; margin-top: 10px; ">
                    <el-tooltip effect="dark" content="按Enter键发送消息，按Alt+Enter键换行">
                        <el-button @click="sendTextMsg(msgInput)">发送</el-button>
                    </el-tooltip>
                </div>
            </div>
        </div>
        <!-- 新增会话 -->
        <el-dialog v-model="findUserDialogVisible" title="添加会话" width="800px" style="height: 600px;" align-center>
            <div style="display: flex;">
                <el-input v-model="userNameSearched" placeholder="请输入用户名" :prefix-icon="Search"
                    @input="searchUserNameDounce" style="padding-right: 10px; margin-right: auto" />
                <el-button @click="searchUserName" type="primary">查找</el-button>
            </div>
            <el-scrollbar height="450px" style="margin-top: 20px;">
                <div style="display: flex; flex-wrap: wrap; ">
                    <div v-for="user of userSearchList" style="
                        display: flex;
                        width: 250px;
                        height: 100px;
                        /* border: 1px solid black; */
                        padding: 10px
                    ">
                        <!-- 头像 -->
                        <div style="width: 40%; align-items: center; display: flex; justify-content: center;">
                            <el-avatar shape="circle" :src="`${fileBaseUrl}/${user.icon}`" :size="65" />
                        </div>
                        <div style="width: 60%; display: flex; flex-direction: column; justify-content: center;">
                            <!-- 用户名 -->
                            <div style="display: flex; font-size: 15px">
                                {{ user.nickName }}
                                <div style="opacity: 0.7;">
                                    ({{ user.username }})
                                </div>
                            </div>
                            <!-- 登录时间 -->
                            <div style="opacity: 0.5; font-size: 11px; padding-left: 2px;"> 登录时间：{{
                                formatDateString(user.loginTime) }} </div>
                            <!-- 去聊天 -->
                            <div style="opacity: 0.6;"><el-button size="small" type="primary" :icon="ChatRound"
                                    style="padding: 0 5px;" @click="selectUserToChat(user.id)">聊天</el-button></div>
                        </div>
                    </div>
                </div>
                <div v-if="userSearching" style="text-align: center; opacity: 0.5; margin-top: 10px;">
                    搜索中...
                </div>
                <div v-if="userSearchList.length <= 0 && noMoreUser"
                    style="text-align: center; opacity: 0.8; margin-top: 100px">
                    没有搜索到相关结果
                </div>
                <div v-if="userSearchList.length > 0 && noMoreUser"
                    style="text-align: center; opacity: 0.5; margin-top: 10px;">
                    没有更多了...
                </div>
            </el-scrollbar>
        </el-dialog>
    </div>
</template>

<script setup>
import { nextTick, ref, watch } from 'vue';
import { Search, Plus, ChatDotRound, ChatRound } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router';
import { useTokenStore } from '@/stores/token';
import CmsRequest from '@/request/CmsRequest'
import { debounce } from 'throttle-debounce';
import { fileBaseUrl } from "@/config/UrlConfig";
import { formatDateString } from '@/util/TimeUtil'
import CmsMsgRender from '@/components/CmsMsgRender.vue';
import { ElMessage } from 'element-plus';
import WebsokcetHandler from '@/request/WebsokcetHandler';
import UmsRequest from '@/request/UmsRequest';
import CmsConversationListItem from '@/components/CmsConversationListItem.vue';
import { v4 as uuidv4 } from 'uuid';

let router = useRouter();
let tokenStore = useTokenStore();

if (!tokenStore.token) {
    router.push('/login')
}


//////////////////////////////////////////////////////////////////////////////////////////////////////////
// 新增会话相关
//#region
let findUserDialogVisible = ref(false);
let userNameSearched = ref('')
let userSearchList = ref([])
let userSearchMaxPage = {
    pageNum: 1,
    pageSize: 20
}

let userSearching = ref(false);
let noMoreUser = ref(false);
// 查找用户
async function searchUser() {
    if (userSearching.value) {
        return;
    }

    userSearching.value = true;
    return await UmsRequest.queryUserList(userSearchMaxPage.pageNum, userSearchMaxPage.pageSize, userNameSearched.value).then(page => {
        if (page) {
            userSearchMaxPage.pageNum = page.pageNum;
            userSearchMaxPage.pageSize = page.pageSize;
            if (page.pageNum >= page.totalPage) {
                noMoreUser.value = true;
            }

            userSearchList.value.push(...page.list)
        }

        userSearching.value = false;
    })
}

// 初始化，进入对话框时执行
function initfindUserDialog() {
    findUserDialogVisible.value = true;
    userNameSearched.value = ''
    userSearchList.value = []
    userSearchMaxPage.pageNum = 1;
    userSearching.value = false;
    noMoreUser.value = false;
    searchUser();
}

// 查找用户
function searchUserName() {
    userSearchList.value = [];
    userSearchMaxPage.pageNum = 1;
    userSearching.value = false;
    noMoreUser.value = false;
    searchUser();
}
let searchUserNameDounce = debounce(500, () => searchUserName());


// 选择一个用户聊天
let selectUserToChatDoing = ref(false)
async function selectUserToChat(uid) {
    // post -> 尝试创建会话 -> 会话详情
    // 如果会话存在 
    //   会话本地未加载 -> 放到最上面临界区
    // 如果会话不存在 -> 创建 放到最上面临界区
    // 临界区的会话等会话已经加载就不显示 => 如何判断会话已加载，加载会话时设置map id -> bool
    // 选中会话
    if (selectUserToChatDoing.value) {
        ElMessage.warning("上一个请求正在执行中，请稍等");
        return;
    }
    selectUserToChatDoing.value = true;
    return await CmsRequest.tryConversationWithUser(uid).then(conversation => {
        if (conversation) {
            // 会话没加载
            tryAddTempConversation(conversation);
            // 选中会话
            selectConversation(conversation);
            findUserDialogVisible.value = false;
        }
        selectUserToChatDoing.value = false;
    })
}

//#endregion
//////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////////////////
// 会话列表相关
//#region
let searchName = ref("")

// 会话列表
let conversationMaxPage = {
    pageNum: 1,
    pageSize: 20
}
let conversationList = ref([])
let conversationLoad = ref({}) // conversationId -> bool 表示会话id本地加载没有（conversationList中的），用来控制临界区会话显不显示
let tempConversationList = ref([]) // 临界区会话

let conversationLoading = ref(false)
let noMoreConversation = ref(false)

// 临界区会话放入时需要保证不重复，且未加载，如果重复 -> 更新信息
function tryAddTempConversation(conversation) {
    if (!conversation) {
        return;
    }

    // 已加载
    if (conversationLoad.value[conversation.id]) {
        return;
    }

    // 如果已经存在 -> 更新信息
    let hasConversation = false; // 是否已经放到临界区了
    for (let i = 0; i < tempConversationList.value.length; ++i) {
        if (tempConversationList.value[i].id == conversation.id) {
            hasConversation = true;

            // 重复了, 更新信息
            tempConversationList.value[i] = conversation;
        }
    }

    // 不存在 -> 添加到头部
    if (!hasConversation) {
        tempConversationList.value.unshift(conversation);
    }
}

// 查询会话列表
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

            // 这些会话已加载
            page.list.forEach(conversation => {
                conversationLoad.value[conversation.id] = true;
            })
        } else {
            noMoreConversation.value = true;
        }
        conversationLoading.value = false;
    })
}

queryConversationList();

// 会话按照更新时间排序
function sortConversationList() {
    conversationList.value.sort((a, b) => {
        if (!a.updateTime) {
            return 1; // 交换，没有updateTime的在后面
        }

        if (!b.updateTime) {
            return -1; // 不交换
        }

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


// TODO 下拉加载会话
function loadConversation() {
    if (noMoreConversation.value) {
        return;
    }
    if (conversationLoading.value) {
        return;
    }

    conversationMaxPage.pageNum += 1;
    queryConversationList();
}

// 刷新会话
function reLoadConversation() {
    conversationMaxPage.pageNum = 1;
    conversationList.value = [];
    conversationLoad.value = {}
    tempConversationList.value = []
    conversationLoading.value = false;
    noMoreConversation.value = false;
    queryConversationList()
}

let reLoadConversationDebounce = debounce(500, () => reLoadConversation())

// 选中的会话 id -> bool
let conversationActive = ref({})

// 选中会话时执行
function selectConversation(conversation) {
    // 设置选中的样式类包含active
    conversationActive.value = {}
    conversationActive.value[conversation.id] = true
    if (curConversation.value.id !== conversation.id) {
        curConversation.value = conversation;

        // 加载消息 -> 清除该会话未读消息
        reloadMsg().then(() => {
            clearUnreadMsg();
        })
    }
}

// 清除会话未读消息
function clearUnreadMsg() {
    curConversation.value.unreadMsgNum = 0;
    // 清除未读消息
    CmsRequest.clearUnreadMsg(curConversation.value.id)
}
//#endregion
//////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////////////////
// 消息框相关
//#region
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
async function reloadMsg() {
    msgMaxPage.pageNum = 1;
    msgList.value = [];
    msgLoading.value = false;
    noMoreMsg.value = false;
    return requestMsg().then(() => msgScrollToBottomNextTick());
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

// 消息框滚动到最底部
function msgScrollToBottom() {
    if (msgEndRef.value) {
        msgEndRef.value.scrollIntoView({ block: 'end' });
    }
}

// 消息框滚动到最底部 下一帧执行
function msgScrollToBottomNextTick() {
    nextTick(() => {
        msgScrollToBottom();
    })
}

// 发送消息
let msgInput = ref("")
function sendTextMsg(msg) {
    if (!curConversation.value.id) {
        ElMessage.warning('请先选择一个会话');
        return;
    }

    let newTextMsg = {
        "id": uuidv4(),
        "senderId": tokenStore.getUser().id,
        "conversationId": curConversation.value.id,
        "type": "TEXT",
        "content": msg,
        "sendState": 1 // 不存在|0 -> 发送成功 | 1 -> 发送中 | 2 -> 发送失败 用做前端发消息时渲染，后端不存
    
    }
    console.log(newTextMsg)
    msgList.value.unshift(newTextMsg)
    msgScrollToBottomNextTick();
    CmsRequest.sendMsg(newTextMsg.conversationId, newTextMsg.type, newTextMsg.content).then(newMsg => {
        // 为了vue监听到数据变化，需要通过msgList修改原对象
        if (newMsg) {
            // 发送成功
            for(let i = 0; i < msgList.value.length; ++i){
                if(msgList.value[i].id = newTextMsg.id){
                    msgList.value[i] = newMsg;
                    break;
                }
            }
        } else {
            // 发送失败
            for(let i = 0; i < msgList.value.length; ++i){
                if(msgList.value[i].id = newTextMsg.id){
                    msgList.value[i].sendState = 2;
                    break;
                }
            }
        }
    })
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
    // conversationId deal conversation sort
    let hasConversation = false;
    for (let i = 0; i < conversationList.value.length; ++i) {
        if (conversationList.value[i].id === newMsg.conversationId) {
            // 本地已经加载这个会话
            conversationList.value[i].lastMsgId = newMsg.id;
            conversationList.value[i].lastMsg = newMsg;
            conversationList.value[i].updateTime = newMsg.createTime;

            // 未读消息
            if (curConversation.value.id !== newMsg.conversationId) {
                if (newMsg.senderId !== tokenStore.getUser().id) {
                    // 不是自己发的消息，未读消息+1
                    conversationList.value[i].unreadMsgNum += 1;
                }
            } else {
                // 正好在当前会话

                // 判断是否添加一条新消息 (新消息通知不但给对方发，也给自己发，考虑同一个用户在线多台设备的场景)
                let hasMsg = false;
                for (let i = 0; i < msgList.value.length; ++i) {
                    if (msgList.value[i].id == newMsg.id) {
                        hasMsg = true;
                    }
                }
                if (!hasMsg) {
                    msgList.value.unshift(newMsg);
                }
                // 如果不是自己发的消息，正好看到了 清除未读消息
                if (newMsg.senderId !== tokenStore.getUser().id) {
                    clearUnreadMsg();
                }
            }

            sortConversationList();
            hasConversation = true;
            break;
        }
    }

    if (!hasConversation) {
        // 新会话放到最前面，舍弃最后一个会话，维持分页有效性
        //    -- OR 当前所有放到temp区, 重新加载会话
        CmsRequest.queryConversaionInfo(newMsg.conversationId).then(conversation => {
            conversationList.value.unshift(conversation);
            conversationList.value.pop();
        })
    }
})
//#endregion
//////////////////////////////////////////////////////////////////////////////////////////////////////////

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