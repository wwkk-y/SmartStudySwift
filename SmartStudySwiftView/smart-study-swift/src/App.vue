<script setup>
import {RouterView } from 'vue-router'
import UmsRequst from '@/request/UmsRequst'
import SssHeader from './components/SssHeader.vue';

function login(username, password) {
  UmsRequst.login(username, null, password).then(res => console.log(res))
}

</script>

<template>
  <div id="app-v">
    <sss-header/>
    <main class="content">
      <router-view></router-view>
    </main>
  </div>
</template>

<style scoped>
/* 确保 header 固定在顶部 */
#app-v {
  position: absolute;
  top: 0;
  left: 0;
  min-height: 100vh;
  width: 100vw;
}

#app-v > .header {
  position: fixed;
  width: 100%;
  z-index: 1000; /* 确保头部不会被其他内容遮挡 */
}

/* 调整 router-view 的样式以适应头部的高度 */
#app-v > .content {
  margin-top: 70px; /* 根据实际情况调整 */
  width: 100%;
  flex: 1;
  overflow-y: auto;
  padding-left: 20vw;
  padding-right: 20vw;
}
</style>
