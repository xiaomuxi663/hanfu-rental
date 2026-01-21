import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || 'null'))
  const isLogin = computed(() => !!token.value)
  const isAdmin = computed(() => userInfo.value?.role === 'admin')
  const isStaff = computed(() => userInfo.value?.role === 'staff')
  const isRenter = computed(() => userInfo.value?.role === 'renter')

  function setToken(newToken) { token.value = newToken; localStorage.setItem('token', newToken) }
  function setUserInfo(info) { userInfo.value = info; localStorage.setItem('userInfo', JSON.stringify(info)) }
  function login(tokenValue, user) { setToken(tokenValue); setUserInfo(user) }
  function logout() { token.value = ''; userInfo.value = null; localStorage.removeItem('token'); localStorage.removeItem('userInfo') }

  return { token, userInfo, isLogin, isAdmin, isStaff, isRenter, setToken, setUserInfo, login, logout }
})
