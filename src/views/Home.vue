<script setup>
import { ref,getCurrentInstance,onMounted } from "vue";
//import axios from 'axios'
//import { get } from "@vueuse/core";
const {proxy} = getCurrentInstance()
const getImageUrl = (user)=>{
    return new URL(`../assets/images/${user}.png`,import.meta.url).href; //注意这里路径千万不要有多余的空格
}

const tableData = ref([
    {
        name: "商品1",
        price1: 100,
        price2: 120,
    },
    {
        name: "商品2",
        price1: 100,
        price2: 120,
    }
])

const tableLabel = ref({
    name: "商品",
    price1: "价格1",
    price2: "价格2",
})

const getTableData =async ()=>{
    const data = await proxy.$api.getTableData()
    tableData.value = data.tableData

}
onMounted(()=>{
    getTableData()
})
</script>

<template>
    <el-row class="home" :gutter="20">
        <el-col :span="8" style="margin-top:20px">
            <el-card shadow="hover">
                <div class="user">
                    <img :src="getImageUrl('user')" class="user"/>
                    <div class="user-info">
                        <p class="user-info-admin">Admin</p>
                        <p class="user-info-p">超级管理员</p>
                    </div>
                </div>
                <div class="login-info">
                    <p>上次登录时间：<span>2024-11-18</span></p>
                    <p>上次登录地点：<span>杭州</span></p>
                </div>
            </el-card>

            <el-card shadow="hover" class="user-table">
                <el-table :data="tableData" height="250">   
                    <el-table-column
                    v-for="(val,key) in tableLabel"
                    :key="key"
                    :prop="key"
                    :label="val">
                        
                    </el-table-column>
                </el-table>
            </el-card>
        </el-col>
    </el-row>
</template>

<style scoped lang="less">
.home{
    height:100%;
    overflow:hidden;   
    .user{
        display:flex;
        align-items: center;
        border-bottom:1px solid #ccc;
        margin-bottom:20px;
        img{
            width:150px;
            height:150px;
            border-radius:50%;
            margin-right:40px;
        }
        .user-info{
            p{
                line-height:40px;
            }
            .user-info-p{
                color:#999;
            }
            .user-info-admin{
                font-size:30px;
            }
        }
    }
    .login-info{
        p{
            line-height:30px;
            font-size:14px;
            color:#999;
            span{
                color:#666;
                margin-left:60px;
            }
        }
    }
    .user-table{
        margin-top:20px;
    }
}

</style>