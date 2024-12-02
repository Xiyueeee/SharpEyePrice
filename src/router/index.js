import {createRouter,createWebHashHistory} from 'vue-router'

//制定路由规则

const routes = [
  {
    path: '/',
    name:'main',
    component:()=>import('../views/Main.vue'),
    redirect: "/home",
    children:[
      {
        path:'/home',
        name: 'home',
        component: () => import('../views/Home.vue'),
        meta: { requiresAuth: true },
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
    path: "/register",
    name: "register",
    component: () => import("../views/Register.vue"),
    meta: {requiresAuth: false },   //注册页面不需要认证
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
  const isAuthenticated = localStorage.getItem('isLoggedIn') === 'true';   //检查是否已登录
  if (to.meta.requiresAuth && !isAuthenticated) {
    //如果目标路由需要认证且用户未登录，重定向到登录页面
    next('/login');
  } else {
    next();   //允许访问
  }
})

export default routers;