<template>
    <div v-if="msg.type === 'TEXT'" style="white-space: pre-wrap;">
        {{ msg.content }}
    </div>
    <template v-else-if="msg.type === 'IMAGE'">
        <el-image :src="`${fileBaseUrl}/${msg.content}`" fit="cover" style="width: auto; height: auto;" lazy>
        </el-image>
    </template>
    <template v-else-if="msg.type === 'FILE'">
        <div>
            <el-icon><Files /></el-icon>
            <!-- FIX FILE TODO -->
        </div>
    </template>
    <div v-else>
        [暂不支持此消息类型-{{ msg.type }}]
    </div>
</template>

<script setup>
import { fileBaseUrl } from '@/config/UrlConfig';
/**
 * 根据不同type
 * 渲染msg
 */
const props = defineProps(['msg'])
</script>

<style></style>