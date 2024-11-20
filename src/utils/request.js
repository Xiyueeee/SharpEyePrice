// src/utils/request.js
import axios from "axios";

const request = axios.create({
  baseURL: "http://xxx.xxx.xxx.xxx // 请求的后端接口地址",
});
/**
 * 请求拦截器
 */
request.interceptors.request.use((config) => {
  console.log("请求参数：", config);
});
/**
 * 响应拦截器
 */
request.interceptors.response.use((response) => {
  console.log("响应参数：", response);
});

export default request;
