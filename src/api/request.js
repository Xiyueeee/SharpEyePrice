import axios from "axios";
import {ElMessage} from 'element-plus'
import config from "../config";


const service = axios.create({
    baseURL:config.baseApi,
    timeout: 10000,   //设置请求超时
});  //使用实例的方式
const NETWORK_ERROR = '网络错误...'
// 添加请求拦截器
service.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    return config;
  }, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
  });

// 添加响应拦截器
service.interceptors.response.use(
    (res)=>{
        const {code,data,msg} = res.data
        if(code === 200){
            return data;
        }else{
            //const NETWORK_ERROR = '网络错误...';
            ElMessage.error(msg || NETWORK_ERROR);
            return Promise.reject(msg || NETWORK_ERROR);
        }
    },
    /*(error) => {
        ElMessage.error(NETWORK_ERROR);
        return Promise.reject(error);
    }*/
);

function request(options){
    options.method = options.method || 'get';
    //关于get请求参数的调整
    if(options.method.toLowerCase() === 'get'){
        options.params = options.data;
    }
    //对mock的开关做一个处理
    let isMock = config.mock;
    if(typeof options.mock !== 'undefined'){  //说明传了数据
        isMock = options.mock;
    }

    //针对环境做一个处理
    if(config.env === 'prod') {   //线上环境
        //不能用mock
        service.defaults.baseURL = config.baseApi;
    }else{
        service.defaults.baseURL = isMock ? config.mockApi : config.baseApi;
    } 
    return service(options);
}

//注册请求
export const register = (registerForm) => {
    return request({
        url: '/register',
        method: 'POST',
        data: registerForm,
    });
};
export default request;