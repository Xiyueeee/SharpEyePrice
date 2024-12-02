<script setup>

import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const registerForm = reactive({
    username: '',
    password: '',
    email: ''
})

const router = useRouter()

//注册操作
const handleRegister = async () => {
    //验证表单内容
    if (registerForm.password.length < 6) {
        ElMessage.error('密码长度必须大于6个字符')
        return
    }

    const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/
    if (!emailPattern.test(registerForm.email)) {
        ElMessage.error('请输入有效的电子邮件')
        return
    }

    try {
        const res = await proxy.$api.register(registerForm)
        if (res.success) {
            ElMessage.success('注册成功！请登录')
            //注册成功后跳转到登录页面
            router.push('/login')
        } else {
            ElMessage.error(res.message || '注册失败，请重试')
        }
    } catch (error) {
        ElMessage.error('注册时发生错误，请稍后再试')
    }
}
</script>

<template>
    <div class="body-register">
        <el-form :model="registerForm" class="register-container">
            <h1>用户注册</h1>
            <el-form-item label="用户名" :rules="[{ required: true, message: '请输入用户名', trigger: 'blur' }]">
                <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item label="密码" :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]">
                <el-input v-model="registerForm.password" type="password" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item label="电子邮件" :rules="[{ required: true, message: '请输入电子邮件', trigger: 'blur' }]">
                <el-input v-model="registerForm.email" placeholder="请输入电子邮件"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="handleRegister">注册</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<style scoped lang="less">
.body-register {
    width: 100%;
    height: 100%;
    background-image: url("../assets/images/background2.jpeg");
    background-size: 100%;
    overflow: hidden;
}
.register-container {
    width: 400px;
    background-color: #fff;
    border: 1px solid #eaeaea;
    border-radius: 15px;
    padding: 35px 35px 15px 35px;
    box-shadow: 0 0 25px #cacaca;
    margin: 250px auto;
    h1 {
        text-align: center;
        margin-bottom: 20px;
        color: #505450;
    }
    :deep(.el-form-item__content) {
        justify-content: center;
    }
}
</style>