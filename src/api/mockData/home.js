export default {
    getTableData: ()=> {
        return {
            code:200,
            data: {
                tableData:[
                    {
                        name:"oppo",
                        price1:2000,
                        price2:5000,
                    },
                    {
                        name:"vivo",
                        price1:1500,
                        price2:2500,
                    },
                    {
                        name:"华为",
                        price1:3200,
                        price2:6400,
                    },
                    {
                        name:"三星",
                        price1:3200,
                        price2:3500,
                    },
                    {
                        name:"苹果",
                        price1:5200,
                        price2:5500,
                    },
                ],
            },
        }
    },
    getCountData: ()=> {
        return {
            code:200,
            data: [
                {
                    name: "今日收藏订单",
                    value: 210,
                    icon: "StarFilled",
                    color: "#ffb980",
                },
                {
                    name: "今日成交量",
                    value: 1500,
                    icon: "SuccessFilled",
                    color: "#2ec7c9",
                },
                {
                    name: "今日新增用户",
                    value: 10,
                    icon: "UserFilled",
                    color: "#5e8ef6",
                },
                {
                    name: "本月收藏订单",
                    value: 100,
                    icon: "StarFilled",
                    color: "#ffb980",
                },
                {
                    name: "本月成交量",
                    value: 5000,
                    icon: "SuccessFilled",
                    color: "#2ec7c9",
                },
                {
                    name: "本月新增用户",
                    value: 20,
                    icon: "UserFilled",
                    color: "#5e8ef6",
                },
            ],
        };
    },
    getChartData: ()=> {
        return {
            code:200,
            data: {
                orderData: {
                    date: [
                        "2022-01-01",
                        "2022-01-02",
                        "2022-01-03",
                        "2022-01-04",
                        "2022-01-05",
                        "2022-01-06",
                        "2022-01-07",
                    ],
                    data: [
                        {
                        oppo:2673,
                        vivo:2550,
                        华为:2400,
                        三星:2200,
                        苹果:5000,
                        },
                        {
                        oppo:2698,
                        vivo:3550,
                        华为:5400,
                        三星:4200,
                        苹果:6000,
                        },
                        {
                        oppo:2673,
                        vivo:2550,
                        华为:2400,
                        三星:2200,
                        苹果:2000,
                        },
                        {
                        oppo:4673,
                        vivo:4550,
                        华为:1400,
                        三星:3200,
                        苹果:6000,
                        },
                        {
                        oppo:3673,
                        vivo:4550,
                        华为:5400,
                        三星:3200,
                        苹果:4000,
                        },
                        {
                        oppo:3673,
                        vivo:1550,
                        华为:2400,
                        三星:2600,
                        苹果:2000,
                        },
                        {
                        oppo:2473,
                        vivo:2150,
                        华为:2300,
                        三星:2100,
                        苹果:2900,
                        },
                    ],
                },
                videoData: [
                    {name: "oppo", value: 2999},
                    {name: "vivo", value: 2699},
                    {name: "华为", value: 2599},
                    {name: "三星", value: 2499},
                    {name: "苹果", value: 2399},
                ],
                userData: [
                    {date: "周一", new: 5, active: 200},
                    {date: "周二", new: 8, active: 300},
                    {date: "周三", new: 12, active: 400},
                    {date: "周四", new: 15, active: 500},
                    {date: "周五", new: 18, active: 600},
                    {date: "周六", new: 22, active: 700},
                    {date: "周日", new: 25, active: 800},
                ],
            },
        };
    },
};