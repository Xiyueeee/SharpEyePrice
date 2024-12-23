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

            <!--添加到商品库按钮-->
            <button @click="openCategoryDialog(product)">添加到商品库</button>
          </div>
        </div>
      </div>
  
      <!-- 如果没有搜索结果 -->
      <div v-if="!loading && !error && !products.length" class="no-results">
        未找到相关商品
      </div>

      <!-- 弹出品类选择对话框 -->
      <div v-if="showDialog" class="dialog-overlay">
        <div class="dialog">
          <h3>请选择商品品类</h3>
          <select v-model="selectedCategory">
            <option disabled value="">请选择品类</option>
            <option value="食品">食品</option>
            <option value="百货">百货</option>
            <option value="数码">数码</option>
            <option value="服饰">服饰</option>
            <option value="美妆">美妆</option>
            <option value="电器">电器</option>
            <option value="医药">医药</option>
            <option value="运动">运动</option>
            <option value="家居">家居</option>
            <option value="玩乐">玩乐</option>
          </select>
          <div>
            <button @click="closeCategoryDialog">取消</button>
            <button @click="confirmCategorySelection">确认</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref,toRaw } from 'vue';
  import axios from 'axios';
  
  // 定义响应式数据
  const name = ref('');
  const products = ref([]);
  const loading = ref(false);
  const error = ref('');
  const showDialog = ref(false);  // 控制对话框显示
  const selectedCategory = ref('');  // 存储选择的品类
  const currentProduct = ref(null);  // 当前选中的商品
  
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

  // 打开品类选择对话框
const openCategoryDialog = (product) => {
  currentProduct.value = product;  // 保存当前商品
  showDialog.value = true;  // 显示对话框
};
  // 关闭品类选择对话框
const closeCategoryDialog = () => {
  showDialog.value = false;
  selectedCategory.value = '';  // 清空选择的品类
};
// 确认选择品类并添加商品
const confirmCategorySelection = async () => {
  if (!selectedCategory.value) {
    alert('请先选择品类');
    return;
  }

  const rawProduct = toRaw(currentProduct.value);  // 使用 toRaw 获取原始数据对象
  console.log('Raw Product:', rawProduct);

  if (!rawProduct) {
    alert('传入的商品无效');
    return;
  }

  let priceStr = rawProduct.price;
  // 1. 去除人民币符号
  let cleanedPriceStr = priceStr.replace('¥', '');

  // 2. 转换为浮动类型（double）
  let priceNum = parseFloat(cleanedPriceStr);

  // 打印输出确认
  console.log(priceNum);  // 输出：30.00
  try {
    // 发送请求到后端
    const response = await axios.post('http://127.0.0.1:8080/products', {
      name: rawProduct.name,
      price: priceNum,
      img_guid: rawProduct.img_guid,
      href: rawProduct.href,
      plat: rawProduct.plat,
      category: selectedCategory.value  // 将选择的品类传到后端
    },{
      withCredentials: true,   // 确保请求中携带凭证例如cookie
      headers: {
        'Content-Type': 'application/json'
      }
    });

    console.log('Response Data:', response); // 调试用，查看返回数据
    if(response.status === 200) {
      alert('商品已添加到商品库');
    } else {
      alert('添加失败');
    }
  } catch(err) {
    console.error("error:", err);
    alert("添加商品失败，请稍后再试");
  } finally {
    // 关闭对话框
    closeCategoryDialog();
  }
};
  </script>
  
  <style scoped>
  .dialog-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .dialog {
    background-color: white;
    padding: 20px;
    border-radius: 8px;
    width: 300px;
  }
  select {
    width: 100%;
    padding: 8px;
    margin-top: 10px;
  }
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
  