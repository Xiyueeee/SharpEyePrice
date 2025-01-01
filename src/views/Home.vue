<script setup>
import { ref,getCurrentInstance,onMounted,reactive } from "vue";
import * as echarts from 'echarts'
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
const observer = ref(null)
const countData = ref([])
const chartData = ref([])

//折线图、柱状图公用数据
const xOptions = reactive({
    //图例文字颜色
    textStyle: {
        color: '#333',
    },
    legend: {},
    grid: {
        left: "20%",
    },
    //提示框
    tooltip: {
        trigger: "axis",
    },
    xAxis: {
        type: "category",  //类目轴
        data: [],
        axisline: {
            lineStyle:{
                color:"#17b3a3",
            },
        },
        axisLable: {
            interval :0,
            color: "#333",
        },
    },
    yAxis: [
        {
            type: "value",
            axisLine: {
                lineStyle: {
                    color: "#17b3a3",
                },
            },
        },
    ],
    color: ["#2ec7c9", "#b6a2de", "#5ab1ef", "#ffb980", "d87a80", "#8d98b3"],
    series: [],
})
const getTableData =async ()=>{
    const data = await proxy.$api.getTableData()
    //console.log(data);
    tableData.value = data.tableData

}
const getCountData =async ()=>{
    const data = await proxy.$api.getCountData()
    //console.log(data);
    countData.value = data
}

const getChartData =async ()=>{
    const {orderData,userData,videoData} = await proxy.$api.getChartData()
    //对第一个图表进行x轴和series配置赋值
    xOptions.xAxis.data = orderData.date;
    xOptions.series = Object.keys(orderData.data[0]).map(val=>({
        name:val,
        data:orderData.data.map(item => item[val]),
        type:'line'
    }))
    const oneEchart = echarts.init(proxy.$refs['echart'])
    oneEchart.setOption(xOptions)
    
    //对第二个表格进行渲染
    xOptions.xAxis.data = userData.map(item=>item.date)
    xOptions.series = [
        {
            name:'新增用户',
            data:userData.map(item=>item.new),
            type:'bar'
        },
        {
            name:'活跃用户',
            data:userData.map(item=>item.active),
            type:'bar'
        },
    ]
    const twoEchart = echarts.init(proxy.$refs['userEchart'])
    twoEchart.setOption(xOptions)

    //监听页面的变化
    //如果监听的容器大小发生了变化，会执行回调函数
    observer.value = new ResizeObserver(()=>{
        oneEchart.resize()
        twoEchart.resize()
    })

    //容器存在
    if(proxy.$refs['echart']){
        observer.value.observe(proxy.$refs['echart'])
    }
}

onMounted(()=>{
    getTableData()
    getCountData()
    getChartData()
})
</script>

<template>
    <!-- Header -->
    <header class="header">
        <div class="logo">
            <span class="logo-text">SharpEyePrice</span>
            <span class="slogan">慧眼识价，精准购物</span>
        </div>
    </header>
    <el-row class="home" :gutter="20">
        <el-col :span="8" style="margin-top:20px">
            <el-card shadow="hover">
                <div class="user">
                    <img :src="getImageUrl('user')" class="user"/>
                    <div class="user-info">
                        <p class="user-info-admin">User</p>
                        <p class="user-info-p">用户</p>
                    </div>
                </div>
                <div class="login-info">
                    <p>上次登录时间：<span>2024-12-23</span></p>
                    <p>上次登录地点：<span>杭州</span></p>
                </div>
            </el-card>

            <!--el-card shadow="hover" class="user-table">
                <el-table :data="tableData" height="250">   
                    <el-table-column
                    v-for="(val,key) in tableLabel"
                    :key="key"
                    :prop="key"
                    :label="val">
                        
                    </el-table-column>
                </el-table>
            </！el-card-->
        </el-col>
        <!--el-col :span="16" style="margin-top:20px">
            <div class="num">
                <el-card
                :body-style="{display:'flex',padding:0}"
                v-for="item in countData"
                :key="item.name">
                <component :is="item.icon" class="icons" :style="{background:item.color}"></component>
                <div class="detail">
                    <p class="num">￥{{ item.value }}</p>
                    <p class="txt">￥{{ item.name }}</p>
                </div>
                </el-card>
            </div>
            <el-card class="top-echarts">
                <div ref="echart" style="height:280px"></div>
            </el-card>
            <div class="graph">
                <el-card>
                    <div ref="userEchart" style="height:240px"></div>
                </el-card>
                <el-card>
                    <div ref="videoEchart" style="height:240px"></div>
                </el-card>
            </div>
        </!--el-col-->
    </el-row>
</template>

<style scoped lang="less">
.header {
  background-color: #17b3a3;
  color: white;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header .logo {
  font-size: 24px;
  font-weight: bold;
}

.header .logo .logo-text {
  font-size: 26px;
  font-style: italic;
  background: linear-gradient(45deg, #ff6f61, #d2a1ff);  /* 渐变颜色 */
  -webkit-background-clip: text; /* 使渐变仅应用于文字 */
  background-clip: text; /* 使渐变仅应用于文字 */
  color: transparent; /* 使文字颜色透明 */
}

.header .logo .slogan {
  font-size: 16px;
  font-style: italic;
  background: linear-gradient(45deg, #ff6f61, #d2a1ff);  /* 渐变颜色 */
  -webkit-background-clip: text; /* 使渐变仅应用于文字 */
  background-clip: text; /* 使渐变仅应用于文字 */
  color: transparent; /* 使文字颜色透明 */
}
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
    .num{
        display:flex;
        flex-wrap:wrap;
        justify-content:space-between;
        .el-card{
            width:32%;
            margin-bottom:20px;
        }
        .icons{
            width:80px;
            height:80px;
            font-size:30px;
            text-align:center;
            line-height:80px;
            color:#fff;
        }
        .detail{
            margin-left:15px;
            display:flex;
            flex-direction:column;
            justify-content: center;
            .num{
                font-size:30px;
                margin-bottom:10px;
            }
            .txt{
                font-size:15px;
                text-align:center;
                color:#999;
            }
        }
    }
}
</style>