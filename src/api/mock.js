import Mock from "mockjs";
import homeApi from './mockData/home'
import userApi from './mockData/user'
//1.拦截的路径  2.方法  3.制造出的假数据
import menuApi from './mockData/permission'
Mock.mock(/api\/home\/getTableData/,"get",homeApi.getTableData);   //正则表达式
Mock.mock(/api\/home\/getCountData/,"get",homeApi.getCountData);   //正则表达式
Mock.mock(/api\/home\/getChartData/,"get",homeApi.getChartData);   //正则表达式
Mock.mock(/api\/home\/getUserData/,"get",userApi.getUserList);   //正则表达式
Mock.mock(/api\/user\/deleteUser/,"get",userApi.deleteUser);   //正则表达式
Mock.mock(/api\/user\/addUser/,"post",userApi.createUser);   //正则表达式
Mock.mock(/api\/user\/editUser/,"post",userApi.updateUser);   //正则表达式
Mock.mock(/api\/permission\/getMenu/,"post",menuApi.getMenu);   //正则表达式