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
        path: 'home',
        name:'home',
        component:()=>import('../views/Home.vue')
      },
    ],
  },
];

const routers = createRouter({
  //设置路由的模式
  history: createWebHashHistory(),
  routes,
});

export default routers;