<template>
    <div style="display: flex; width: 100%; height: 100%; ">
        <!-- 头像 -->
        <div style="width: 20%; height: 100%; display: flex; justify-content: center; align-items: center">
            <el-avatar shape="circle" :src="`${fileBaseUrl}/${conversation.icon}`" />
        </div>

        <div style="width: 80%; display: flex; flex-direction: column; margin-left: 5px; margin-top: 10px; margin-bottom: 10px">
            <div style="height: 50%; display: flex; align-items: center">
                <!-- 昵称 -->
                <div style="margin-right: auto;">{{ conversation.nickName }}</div>
                <!-- 时间 -->
                <div style="margin-right: 15px;">{{ formatDateString(conversation.updateTime) }}</div>
            </div>
            <div style="height: 50%; display: flex; align-items: center; opacity: 0.7;">
                <!-- 最后一条消息 -->
                <div style="margin-right: auto; font-size: small; overflow: hidden; max-width: 75%">
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
                <!-- 未读消息数量 -->
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
</template>

<script setup>
import { fileBaseUrl } from "@/config/UrlConfig";
import { formatDateString } from '@/util/TimeUtil'

const props = defineProps(['conversation']) // 需要渲染的会话


</script>

<style scoped></style>