import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'



// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],

  // 添加服务器代理配置
  server: {
    proxy: {
      '/user': 'http://localhost:8080',  // 将所有以 '/user' 开头的请求代理到后端
      '/products': 'http://localhost:8080',
      '/hello': 'http://localhost:5000',
      '/searchJD': 'http://localhost:5000',
      '/searchSN': 'http://localhost:5000',
      '/sendPriceAlert': 'http://localhost:5000',
      '/products/category': 'http://localhost:8080',
    },
  },

  //resolve表示添加的别名
  resolve:{
    alias:[
      {
        find: "@",
        replacement: "/src",
      },
    ],
  },
});
