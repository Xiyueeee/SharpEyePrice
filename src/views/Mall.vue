<template>
    <div class="container">
      <!-- 返回首页按钮 -->
      <div class="back-home">
        <button @click="goHome" class="back-home-btn">返回首页</button>
      </div>
  
      <h1 class="page-title">SharpEyePrice 个人商品库</h1>
  
      <!-- 品类按钮 -->
      <div class="category-buttons">
        <button @click="fetchProductsByCategory('食品')" class="category-btn">食品</button>
        <button @click="fetchProductsByCategory('百货')" class="category-btn">百货</button>
        <button @click="fetchProductsByCategory('数码')" class="category-btn">数码</button>
        <button @click="fetchProductsByCategory('服饰')" class="category-btn">服饰</button>
        <button @click="fetchProductsByCategory('美妆')" class="category-btn">美妆</button>
        <button @click="fetchProductsByCategory('电器')" class="category-btn">电器</button>
        <button @click="fetchProductsByCategory('医药')" class="category-btn">医药</button>
        <button @click="fetchProductsByCategory('运动')" class="category-btn">运动</button>
        <button @click="fetchProductsByCategory('家居')" class="category-btn">家居</button>
        <button @click="fetchProductsByCategory('玩乐')" class="category-btn">玩乐</button>
        <button @click="fetchAllProducts" class="category-btn">查看所有商品</button>
      </div>
  
      <!-- 商品展示 -->
      <div v-if="products.length > 0" class="product-list">
        <div v-for="product in products" :key="product.id" class="product-item">
          <img :src="`/static/imgs/${product.img_guid}`" alt="Product Image" />
          <h3>{{ product.name }}</h3>
          <p>价格: ￥{{ product.price }}</p>
          <p>平台：{{ product.plat }}</p>
          <a :href="product.href" target="_blank">查看详情</a>
          
          <!-- 添加查看七天价格趋势按钮 -->
          <button @click="showPriceTrend(product)" class="price-trend-btn">查看七天价格趋势</button>
  
          <!-- 显示价格走势图 -->
          <div v-if="product.showTrend" class="price-trend">
            <canvas :id="'price-trend-' + product.id"></canvas>
          </div>

            <!-- 添加降价提醒按钮 -->
            <button @click="openPriceAlertDialog(product)" class="price-alert-btn">添加降价提醒</button>
        </div>
      </div>
      <div v-else>
        <p>没有找到商品</p>
      </div>

      <!-- 降价提醒对话框 -->
      <div v-if="showAlertDialog" class="alert-dialog">
        <div class="dialog-overlay" @click="closePriceAlertDialog"></div>
        <div class="dialog-content">
            <h3>添加降价提醒</h3>
            <label for="userName">请留下您的姓名:</label>
            <input type="text" v-model="alertForm.name" id="userName" placeholder="请输入姓名" />

            <label for="userEmail">请输入您希望收到邮件的地址:</label>
            <input type="email" v-model="alertForm.email" id="userEmail" placeholder="请输入邮箱地址" />

            <button @click="sendPriceAlert">发送提醒</button>
            <button @click="closePriceAlertDialog">关闭</button>
        </div>
        </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  import { Chart, LineController, PointElement, LineElement, LinearScale, CategoryScale, Title, Tooltip, Legend } from 'chart.js';  // 修改这里，导入整个 Chart 类
  import { nextTick } from 'vue';  // 导入 nextTick

  Chart.register(PointElement, LineController, LineElement, CategoryScale, LinearScale, Title, Tooltip, Legend);  // 注册所需模块
  
  export default {
    data() {
      return {
        products: [],  // 存储商品列表
        showAlertDialog: false, // 控制降价提醒对话框的显示
        alertForm: {
            name: '',
            email: '',
            productName: '' // 商品名称
        }
      };
    },
    methods: {
        // 打开降价提醒对话框
    openPriceAlertDialog(product) {
      this.alertForm.productName = product.name; // 设置商品名称
      this.showAlertDialog = true;
    },
    // 关闭降价提醒对话框
    closePriceAlertDialog() {
      this.showAlertDialog = false;
      this.alertForm.name = '';
      this.alertForm.email = '';
    },
    // 发送降价提醒请求
    sendPriceAlert() {
      const { name, email, productName } = this.alertForm;

      if (!name || !email || !productName) {
        alert('请填写所有字段');
        return;
      }
      // 发送请求到后端
      fetch('http://127.0.0.1:5000/sendPriceAlert', {
        method: 'POST', // 使用 POST 请求
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          name: name,
          productName: productName,
          address: email
        })
      })
      .then(response => response.json())
      .then(data => {
        if (data.message === "降价提醒邮件已发送") {
          alert('降价提醒已发送');
        } else {
          alert('发送失败，请重试');
        }
        this.closePriceAlertDialog();
      })
      .catch(error => {
        console.error('发送请求失败:', error);
        alert('发送请求失败，请稍后再试');
      });
    },
      // 获取所有商品
      fetchAllProducts() {
        axios.get('http://localhost:8080/products')
          .then(response => {
            this.products = response.data;
          })
          .catch(error => {
            console.error('Error fetching products:', error);
          });
      },
      
      // 根据品类获取商品
      fetchProductsByCategory(category) {
        axios.get(`http://localhost:8080/products/category/${category}`)
          .then(response => {
            this.products = response.data;
          })
          .catch(error => {
            console.error('Error fetching products by category:', error);
          });
      },
      // 生成七天的价格数据
      generatePriceData(currentPrice) {
        const trendData = [];
        let price = currentPrice;
        const range = this.getPriceRange(price);

        // 随机生成前六天的价格变化
        for (let i = 0; i < 6; i++) {
            const randomChange = (Math.random() * range * 2 - range).toFixed(2);  // 随机波动
            price = (parseFloat(price) + parseFloat(randomChange)).toFixed(2);
            trendData.push(price);  // 保存生成的前六天价格
        }

        // 最后一天的价格设为商品的实际价格
        trendData.push(currentPrice.toFixed(2));

        return trendData;
        },
  
      // 根据商品价格决定价格波动范围
      getPriceRange(price) {
        if (price > 1000) {
          return 50;
        } else if (price > 100) {
          return 10;
        } else {
          return 1;
        }
      },
  
      // 显示价格趋势
    showPriceTrend(product) {
      product.showTrend = !product.showTrend;  // 控制图表的显示与隐藏

      // 使用 nextTick 确保 DOM 已更新
      nextTick(() => {
        if (product.showTrend) {
          const priceData = this.generatePriceData(product.price);
          console.log(priceData);  // 输出生成的价格数据，检查是否正确
          this.renderPriceChart(product.id, priceData);
        } else {
          // 如果不显示图表，清除现有图表
          const canvas = document.getElementById(`price-trend-${product.id}`);
          if (canvas) {
            const ctx = canvas.getContext('2d');
            if (ctx.chart) {
              ctx.chart.destroy();  // 销毁现有图表
            }
          }
        }
      });
    },

    // 渲染价格走势图
    renderPriceChart(productId, priceData) {
      const canvas = document.getElementById(`price-trend-${productId}`);
      if (!canvas) {
        console.error(`Canvas element not found for product ID: ${productId}`);
        return;
      }
      const ctx = canvas.getContext('2d');

      // 如果图表已经存在，销毁它
      if (ctx.chart) {
        ctx.chart.destroy();
      }

      // 创建新的图表
      new Chart(ctx, {
        type: 'line',
        data: {
          labels: ['1', '2', '3', '4', '5', '6', '7'],
          datasets: [{
            label: '价格趋势',
            data: priceData,
            borderColor: 'rgba(75, 192, 192, 1)',
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            fill: true
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            y: {
              beginAtZero: false
            }
          }
        }
      });
    },

  
      // 返回首页
      goHome() {
        this.$router.push('/');
      }
    },
    mounted() {
      // 默认显示所有商品
      this.fetchAllProducts();
    }
  };
  </script>
  
  
  <style scoped>
  canvas {
  display: block;
  width: 100%;
  height: 100%;
}
  .price-trend {
  width: 100%;
  height: 300px;
  margin-top: 20px;
}
.price-trend-btn {
    display: inline-block;
    margin-top: 10px;
    padding: 8px 16px;
    background-color: #de6ccf;
    color: white;
    text-decoration: none;
    border-radius: 5px;
    transition: background-color 0.3s ease;
}

.price-trend-btn:hover {
    background-color: #9d19d6;
}
  /* 页面容器 */
  .container {
     /* 使用线性渐变背景 */
    background: linear-gradient(to bottom right, #369ac2, #feb47b); /* 这是一个从左上到右下的渐变效果 */
        
    background-size: cover; /* 背景渐变不会重复 */
    background-position: center; /* 背景居中 */
    min-height: 100vh; /* 确保背景覆盖整个视口的高度 */
  }
  
  /* 返回首页按钮样式 */
  .back-home {
    text-align: center;
    margin-bottom: 20px;
  }
  
  .back-home-btn {
    background-color: #ff5722;
    color: white;
    border: none;
    padding: 12px 24px;
    font-size: 1.2rem;
    cursor: pointer;
    border-radius: 5px;
    transition: background-color 0.3s ease, transform 0.3s ease;
  }
  
  /* 返回首页按钮悬停效果 */
  .back-home-btn:hover {
    background-color: #e64a19;
    transform: translateY(-3px);
  }
  
  /* 标题样式 */
  .page-title {
    font-size: 2rem;
    font-weight: bold;
    color: transparent; /* 设置文字颜色为透明 */
    background: linear-gradient(to right, #750d4c, #cf4b4b); /* 定义渐变色 */
    -webkit-background-clip: text; /* 使用 Webkit 特性将背景渐变应用到文字上 */
    background-clip: text; /* 这条规则用于非 Webkit 浏览器 */
    text-align: center;
    margin-bottom: 20px;
  }
  
  /* 品类按钮区域样式 */
  .category-buttons {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    margin-bottom: 30px;
  }
  
  /* 单个按钮样式 */
  .category-btn {
    background-color: #39d253;
    color: white;
    border: none;
    padding: 12px 24px;
    margin: 10px;
    font-size: 1.2rem;
    cursor: pointer;
    border-radius: 5px;
    transition: background-color 0.3s ease, transform 0.3s ease;
  }
  
  /* 鼠标悬停时按钮样式 */
  .category-btn:hover {
    background-color: #45a049;
    transform: translateY(-3px);
  }
  
  /* 商品列表样式 */
  .product-list {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
  }
  
  /* 商品项样式 */
  .product-item {
    border: 1px solid #ddd;
    padding: 15px;
    margin: 15px;
    text-align: center;
    width: 200px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    transition: transform 0.2s ease;
  }
  
  /* 商品项悬停效果 */
  .product-item:hover {
    transform: translateY(-5px);
  }
  
  /* 商品图片样式 */
  .product-item img {
    width: 100%;
    height: auto;
    border-radius: 5px;
    margin-bottom: 10px;
  }
  
  /* 商品名称和价格样式 */
  .product-item h3 {
    font-size: 1.2rem;
    color: #333;
  }
  
  .product-item p {
    font-size: 1rem;
    color: #666;
  }
  
  /* 详情链接样式 */
  .product-item a {
    display: inline-block;
    margin-top: 10px;
    padding: 8px 16px;
    background-color: #4ab3c6;
    color: white;
    text-decoration: none;
    border-radius: 5px;
    transition: background-color 0.3s ease;
  }
  
  .product-item a:hover {
    background-color: #4a76d5;
  }

  .alert-dialog {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5); /* 半透明背景 */
  display: flex;
  justify-content: center;
  align-items: center;
}

.dialog-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  z-index: 1;
}

.dialog-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  z-index: 2;
  width: 400px;
}

input {
  width: 100%;
  padding: 8px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  padding: 8px 16px;
  background-color: #007BFF;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
  </style>