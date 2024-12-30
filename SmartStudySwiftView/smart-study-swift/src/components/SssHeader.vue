<template>
    <el-menu class="header" :router="true" mode="horizontal" :ellipsis="false">
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/question/bank">题库</el-menu-item>
        <el-menu-item index="3">兑换</el-menu-item>
        <el-menu-item index="4">讨论</el-menu-item>
        <el-menu-item index="/chat" style="margin-right: auto">消息</el-menu-item>

        <el-menu-item index="/about">关于我们</el-menu-item>
        <el-menu-item index="|" disabled>|</el-menu-item>
        <el-menu-item v-show="!tokenStore.token" index="/login">登录</el-menu-item>
        
        <el-menu-item v-show="!tokenStore.token" index="/register">注册</el-menu-item>

        <el-sub-menu index="7" v-if="tokenStore.getUser().username">
            <template #title>
                <el-avatar shape="circle" :src="`${fileBaseUrl}/${tokenStore.getUser().icon}`" />
            </template>
            <el-menu-item index="/account">账号</el-menu-item>
            <el-menu-item @click="logout">退出登录</el-menu-item>
        </el-sub-menu>
    </el-menu>
</template>

<script setup>
import { useTokenStore } from "@/stores/token";
import UmsRequest from '@/request/UmsRequest'
import { ElMessage } from "element-plus";
import { fileBaseUrl } from "@/config/UrlConfig";
let tokenStore = useTokenStore();

function logout() {
    UmsRequest.logout().then((msg) => {
        if(msg){
            ElMessage.success('退出成功');
        }
    })
}

</script>

<style scoped>
.header {
    height: 50px;
    padding-left: 20vw;
    padding-right: 20vw;
}
</style>