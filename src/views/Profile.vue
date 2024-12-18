<template>
  <div v-if="userInfo">
    <h2>欢迎, {{ userInfo.account }}!</h2>
    <p><strong>用户名:</strong> {{ userInfo.account }}</p>
    <p><strong>邮箱:</strong> {{ userInfo.email }}</p>
  </div>
  <div v-else>
    <p>加载中...</p>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import axios from 'axios';

// 响应式数据，保存用户信息
const userInfo = reactive({
  account: '',
  email: '',
});

// 获取用户信息的函数
const getUserInfo = async () => {
  try {
    // 调用后端接口获取用户信息
    const res = await axios.get('http://localhost:8080/user/info', {
      withCredentials: true,  // 确保携带 session 信息
    });
    console.log(res.data);
    if (res.data && res.data.account) {
      console.log('用户账号:', res.data.account);
      // 执行后续操作
    } 
    // 如果获取成功
    if (res.data.success) {
      // 将返回的用户信息填充到响应式数据中
      userInfo.account = res.data.account;
      userInfo.email = res.data.email;
    } else {
      // 如果未登录或请求失败，弹出错误信息并跳转到登录页面
      ElMessage.error(res.data.message || '获取用户信息失败');
      router.push('/login');  // 未登录则跳转到登录页
    }
  } catch (error) {
    console.error('获取用户信息失败:', error);
    ElMessage.error('获取用户信息失败');
  }
};

// 使用 vue-router 进行页面跳转
const router = useRouter();

// 页面加载时获取用户信息
onMounted(() => {
  getUserInfo();
});
</script>

<style scoped>
/* 样式可以根据需要自定义 */
h2 {
  font-size: 24px;
  color: #333;
}

p {
  font-size: 16px;
  color: #666;
}

strong {
  color: #333;
}
</style>
