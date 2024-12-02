/**
 * 整个项目api的统一管理
 */

import request from "./request";

//请求首页左侧的表格的数据

export default {
    getTableData() {
        return request({
            method: "get",
            url: "/home/getTableData",  ///本地的数据
            //mock: true,
        });
    },
    getCountData() {
        return request({
            method: "get",
            url: "/home/getCountData",  ///api/home/getTableData
            //mock: true,
        });
    },
    getChartData() {
        return request({
            method: "get",
            url: "/home/getChartData",  ///api/home/getTableData
            //mock: true,
        });
    },
    getUserData(data) {
        return request({
            method: "get",
            url: "/home/getUserData",  ///api/home/getTableData
            data,//mock: true,
        });
    },
    deleteUser(data) {
        return request({
            method: "get",
            url: "/user/deleteUser",  ///api/home/getTableData
            data,//mock: true,
        });
    },
    addUser(data) {
        return request({
            method: "post",
            url: "/user/addUser",  ///api/home/getTableData
            data,//mock: true,
        });
    },
    editUser(data) {
        return request({
            method: "post",
            url: "/user/editUser",  ///api/home/getTableData
            data,//mock: true,
        });
    },
    getMenu(params) {
        return request({
            url: '/permission/getMenu',
            method: 'post',
            data:params
        })
    }
};