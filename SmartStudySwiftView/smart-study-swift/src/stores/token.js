import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import UmsRequest from '@/request/UmsRequest';
import { debounce } from 'throttle-debounce';

const TOKEN_STORE_KEY = "sss-token";

export const useTokenStore = defineStore('token', () => {
  const token = ref(localStorage.getItem(TOKEN_STORE_KEY));
  const user = ref({ username: '' })

  function flushUser() {
    if (token.value) {
      // 消抖
      UmsRequest.accountInfo().then(info => user.value = info);
    } else {
      user.value = { username: '' };
    }
  }

  const flushUserDebounce = debounce(1000, () => flushUser());

  function getUser() {
    if (token.value && user.value && !user.value.username) {
      flushUserDebounce();
    }
    if (user.value) {
      return user.value;
    } else {
      return { username: '' };
    }
  }

  /**
   * 设置新token并更新user
   * @param {String} newToken 
   */
  function setToken(newToken) {
    token.value = newToken;
    localStorage.setItem(TOKEN_STORE_KEY, newToken);
    flushUser();
  }

  return { token, setToken, getUser, flushUser }
})
