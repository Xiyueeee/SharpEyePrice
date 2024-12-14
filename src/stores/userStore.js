import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    menuList: JSON.parse(localStorage.getItem('menuList')) || [],
  }),
  actions: {
    setToken(token) {
      this.token = token;
      localStorage.setItem('token', token); // 将 token 存储到 localStorage
    },
    setMenuList(menuList) {
      this.menuList = menuList;
      localStorage.setItem('menuList', JSON.stringify(menuList)); // 存储菜单列表
    },
    logout() {
      this.token = '';
      this.menuList = [];
      localStorage.removeItem('token');
      localStorage.removeItem('menuList');
    }
  }
});