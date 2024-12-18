<template>
    <div class="body_JD">
      <h1>苏宁易购 商品搜索</h1>
  
      <!-- 搜索框 -->
      <div>
        <input 
          v-model="name" 
          type="text" 
          placeholder="请输入关键词" 
          @keyup.enter="searchProducts" 
        />
        <button @click="searchProducts">搜索</button>
        <router-link to="/" class="go-home-btn">返回首页</router-link>
      </div>
  
      <!-- 加载提示 -->
      <div v-if="loading">加载中...</div>
  
      <!-- 错误提示 -->
      <div v-if="error" class="error">{{ error }}</div>
  
      <!-- 商品列表 -->
      <div v-if="products.length">
        <div class="product-list">
          <div 
            v-for="product in products" 
            :key="product.img_guid" 
            class="product-card"
          >
            <img :src="`/static/imgs/${product.img_guid}`" alt="product image" />
            <h3>{{ product.name }}</h3>
            <p class="price">{{ product.price }} 元</p>
            <a :href="product.href" target="_blank" rel="noopener noreferrer">查看商品链接</a>
          </div>
        </div>
      </div>
  
      <!-- 如果没有搜索结果 -->
      <div v-if="!loading && !error && !products.length" class="no-results">
        未找到相关商品
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import axios from 'axios';
  
  // 定义响应式数据
  const name = ref('');
  const products = ref([]);
  const loading = ref(false);
  const error = ref('');
  
  // 商品搜索方法
  const searchProducts = async () => {
    // 校验关键词
    if (!name.value.trim()) {
      error.value = '请输入关键词';
      return;
    }
    
    // 重置状态
    loading.value = true;
    error.value = '';
    products.value = [];
  
    try {
      // 发送请求到后端
      const response = await axios.get('http://127.0.0.1:5000/searchSN', {
        params: { 
          name: name.value,
         }
      });
      
      console.log('Response Data:', response.data); // 调试用，查看返回数据
      // 如果响应数据为空
      if (response.data && Array.isArray(response.data.data) && response.data.data.length > 0) {
        products.value = response.data.data;
      } else {
        error.value = '没有找到符合条件的商品';
      }
    } catch (err) {
      // 请求失败
      error.value = '加载失败，请稍后重试';
    } finally {
      // 请求完成
      loading.value = false;
    }
  };
  </script>
  
  <style scoped>
  .body_JD {
      width: 100%;
      min-height: 100vh;
      background-image: url("../assets/images/SN.jpg");
      background-size: cover;
      background-repeat: no-repeat;
      overflow: hidden;
  }
  
  .go-home-btn {
    padding: 8px 12px;
    background-color: #007bff;
    color: white;
    border: none;
    cursor: pointer;
    font-size: 16px;
    border-radius: 4px;
  }
  
  .go-home-btn:hover {
    background-color: #0056b3;
  }
  
  .product-list {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    justify-content: center;
  }
  
  .product-card {
    width: 30%; /* 一行显示两个商品 */
    border: 1px solid #ccc;
    padding: 10px;
    text-align: center;
    box-sizing: border-box;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .product-card img {
    width: 100%;
    height: 200px;
    object-fit: cover;
    border-radius: 8px;
  }
  
  .product-card h3 {
    font-size: 18px;
    margin-top: 10px;
  }
  
  .product-card p {
    color: #d9534f;
    font-size: 16px;
    margin-top: 5px;
  }
  
  .product-card .price {
    font-weight: bold;
    margin-top: 10px;
  }
  
  .error {
    color: red;
    text-align: center;
    margin-top: 20px;
  }
  
  .no-results {
    text-align: center;
    margin-top: 20px;
    color: #777;
  }
  
  input[type="text"] {
    padding: 8px;
    margin-right: 10px;
  }
  
  button {
    padding: 8px 15px;
    cursor: pointer;
    background-color: #e5a327;
    color: white;
    border: none;
    font-size: 16px;
    border-radius: 4px;
  }
  
  button:hover {
    background-color: #034b13;
  }
  
  /* 媒体查询：屏幕宽度小于 1024px时，改为每行显示2个商品 */
  @media (max-width: 1024px) {
    .product-card {
      width: 45%; /* 每行显示2个商品 */
    }
  }
  
  /* 媒体查询：屏幕宽度小于 768px时，改为每行显示1个商品 */
  @media (max-width: 768px) {
    .product-card {
      width: 100%; /* 每行显示1个商品 */
    }
  }
  
  /* 屏幕宽度小于768px时，使用contain */
  @media (max-width: 768px) {
      .body_JD {
          background-size: contain;
      }
  }
  </style>
  