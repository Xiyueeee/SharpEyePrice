import Mock from "mockjs";
import homeApi from './mockData/home'
//1.拦截的路径  2.方法  3.制造出的假数据
Mock.mock(/api\/home\/getTableData/,"get",homeApi.getTableData);   //正则表达式