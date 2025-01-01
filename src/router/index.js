import {createRouter,createWebHashHistory} from 'vue-router'

//制定路由规则

const routes = [
  {
    path: '/',
    name:'main',
    component:()=>import('../views/Main.vue'),
    redirect: "/login",
    children:[
      {
        path:'/home',
        name: 'home',
        component: () => import('../views/Home.vue'),
        meta: { requiresAuth: false },
      },   //可以在这里添加更多需要认证的子路由
    ],
  },
  {
    path: '/login',
        name:'login',
        component:()=>import('../views/Login.vue'),
        meta: {requiresAuth: false },   //登录页面不需要认证
  },
  {
    path: '/user',
      name: 'user',
      component: () => import('../views/User.vue'),
      meta: {requiresAuth: false},  //用户管理页面不需要认证
  },
  {
    path: "/mall",
    name: "mall",
    component: () => import("../views/Mall.vue"),
    meta: {requiresAuth: false},  //比比价页面不需要认证
  },
  {
    path: "/register",
    name: "register",
    component: () => import("../views/Register.vue"),
    meta: {requiresAuth: false},  //注册页面不需要认证
  },
  {
    path: "/1688",
    name: "1688",
    component: () => import("../views/1688.vue"),
    meta: {requiresAuth: false},  //1688比价页面不需要认证
  },
  {
    path: "/JD",
    name: "JD",
    component: () => import("../views/JD.vue"),
    meta: {requiresAuth: false},  //京东比价页面不需要认证
  },
  {
    path: "/SN",
    name: "SN",
    component: () => import("../views/SN.vue"),
    meta: {requiresAuth: false},  //SN比价页面不需要认证
  },
  {
    path: "/profile",
    name: "profile",
    component: () => import("../views/Profile.vue"),
    meta: {requiresAuth: false},  //个人中心页面不需要认证
  },
  {
    path: "/404",
    name: "404",
    component: () => import("../views/404.vue"),
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404',   //路径不匹配时重定向到404页面
  }
];

const routers = createRouter({
  //设置路由的模式
  history: createWebHashHistory(),
  routes,
});

//路由守卫（全局前置守卫）
routers.beforeEach((to, from, next) => {
  //const isAuthenticated = !!userStore.token; // 检查是否有 token
  const isAuthenticated = localStorage.getItem('isLoggedIn') === 'true';   //检查是否已登录
  // const token = localStorage.getItem('token');
  console.log('isAuthenticated:', isAuthenticated);  // 打印 isAuthenticated 的值
  console.log('Checking route guard for:', to.path);
  if (to.meta.requiresAuth && !isAuthenticated) {
    console.log('Redirecting to login');
    //如果目标路由需要认证且用户未登录，重定向到登录页面
    next('/login');
  } else {
    console.log('Allowing navigation to', to.path);
    next();   //允许访问
  }
})

export default routers;