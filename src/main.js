import { createApp } from 'vue';
import App from './App.vue';
import router from "./router";
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import {createPinia} from "pinia";
import "./api/mock.js";
import api from "./api/api";  //全局挂载

const pinia = createPinia();
const app = createApp(App)
app.config.globalProperties.$api = api;
app.use(ElementPlus);
app.use(pinia);
app.use(router);
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
}
app.mount("#app");