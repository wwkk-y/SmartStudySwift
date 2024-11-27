<template>
    <el-menu class="header" :router="true" mode="horizontal" :ellipsis="false">
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="2">题库</el-menu-item>
        <el-menu-item index="3">兑换</el-menu-item>
        <el-menu-item index="4">讨论</el-menu-item>
        <el-menu-item index="5" style="margin-right: auto">消息</el-menu-item>

        <el-menu-item index="/about">关于我们</el-menu-item>

        <el-sub-menu index="7">
            <template #title>账号</template>
            <el-menu-item index="7-0">设置</el-menu-item>
            <el-menu-item index="/login">登录</el-menu-item>
            <el-menu-item index="/register">注册</el-menu-item>
            <el-menu-item index="7-3">退出</el-menu-item>
        </el-sub-menu>
    </el-menu>
</template>

<script setup>

</script>

<style scoped>
.header {
    height: 50px;
    padding-left: 20vw;
    padding-right: 20vw;
}

</style>