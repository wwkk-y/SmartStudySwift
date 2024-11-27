import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

const TOKEN_STORE_KEY = "sss-token";

export const useTokenStore = defineStore('token', () => {
  const token = ref("");
  let tokenStore = localStorage.getItem(TOKEN_STORE_KEY);
  if (tokenStore) {
    token.value = tokenStore;
  }

  function getToken() {
    return token.value;
  }

  function setToken(newToken) {
    token.value = newToken;
    localStorage.setItem(TOKEN_STORE_KEY, newToken);
  }

  return { getToken, setToken }
})
