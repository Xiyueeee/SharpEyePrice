<script setup>
import {reactive,getCurrentInstance} from 'vue'
//import {useAllDataStore} from '../stores'
import {useRouter} from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'
const loginForm = reactive({
    account:'',
    password:''
})
//const {proxy} = getCurrentInstance()
//const store = useAllDataStore()
const router = useRouter()
const handleLogin = async () => {
    try {
        const res = await axios.post('/user/login',
            {
                account:loginForm.account,
                password:loginForm.password
            },
            {
            withCredentials: true,   //确保请求中携带凭证例如cookie
            headers: {
                'Content-Type': 'application/json',
                }
            }
        );
        console.log(res.data);
        
        //假设返回的res.data包含menuList和token
        if (res.data.success) {
            ElMessage.success('登录成功')
            console.log('Redirecting to /home...');
            router.push('/home')
        } else {
            ElMessage.error(res.data.message || '登录失败，请重试')
        }
    } catch (error) {
        console.error('登录失败：', error)  
        if (error.response) {
        // 请求已发出，服务器有响应
        console.error('响应状态码:', error.response.status);
        console.error('响应数据:', error.response.data);
    } else if (error.request) {
        // 请求已发出，但没有响应
        console.error('请求信息:', error.request);
    } else {
        // 其他错误
        console.error('错误信息:', error.message);
    }
        ElMessage.error('登录失败，请重试')
    }
}

//跳转到注册页面
const goToRegister = () => {
    router.push('/register')
}

</script>

<template>
    <div class="body-login">
        <el-form :model="loginForm" class="login-container">
            <h1>SharpEyePrice慧眼识价</h1>
            <h1>欢迎登录</h1>
            <el-form-item>
                <el-input type="input" placeholder="请输入账号" v-model="loginForm.account">
                </el-input>
            </el-form-item>
            <el-form-item>
                <el-input type="password" placeholder="请输入密码" v-model="loginForm.password">
                </el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="handleLogin">登录</el-button>
            </el-form-item>
            <!--注册按钮-->
            <el-form-item>
                <el-button @click="goToRegister" type="link">没有账号？点击注册</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<style scoped lang="less">
.body-login{
    width:100%;
    height:100%;
    background-image:url("../assets/images/background2.jpeg");
    background-size:100%;
    overflow:hidden;
}
.login-container{
    width:400px;
    background-color:#fff;
    border:1px solid #eaeaea;
    border-radius:15px;
    padding:35px 35px 15px 35px;
    box-shadow:0 0 25px #cacaca;
    margin:250px auto;
    h1{
        text-align:center;
        margin-bottom:20px;
        color:#505450;
    }
    :deep(.el-form-item__content){
        justify-content:center;
    }
}
</style>