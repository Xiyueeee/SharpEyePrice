<template>
    <div class="exception">
        
        <el-button class="btn-home" @click="goHome">回到上一个页面</el-button>
    </div>
</template>

<script setup>
import {useRouter} from 'vue-router'
const router = useRouter();

/*const getImageUrl = (img) => {
    return new URL(`../assets/images/${img}.jpg`, import.meta.url).href;
};
*/
const goHome=()=>{
    //go方法，按指定方向访问历史。如果是正数则是路由记录向前跳转，如果是负数则是向后退
    //这里回退一个页面到跳转前的页面
    router.go(-1)
};

</script>

<style lang="less">
.exception {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background-image: url("../assets/images/404-page.jpg");
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    background-color:red;
    flex-direction: column;
    text-align: center;
    color: white;
    padding: 20px;
}
/*.exception-image {
    width:100%;
    max-width: 300px;
    height: auto;
    margin-bottom: 30px;
}*/
.btn-home {
    background-color: #42b983;
    color: white;
    border: none;
    padding: 12px,24px;
    font-size: 16px;
    cursor: pointer;
    transition: background-color 0.3s ease, transform 0.2s ease;
}
.btn-home:hover {
    background-color: #36a572;
    transform: scale(1.05);
}
</style>