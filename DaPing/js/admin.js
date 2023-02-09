var utility = {
    date : {
    //字符串转化为日期
    toDate : function(source,splitChar) {
        splitChar = splitChar || "-";
        var reg = new RegExp(splitChar, "g");
        return new Date(source.replace(reg, '/'));
    },
    add: function (source, datepart, number) {
        var dtTmp = $.type(source) == "string" ? utility.date.toDate(source) : source;
        var newDate;
        switch (datepart) {
            case 'y': newDate = new Date((dtTmp.getFullYear() + number), dtTmp.getMonth(), dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds()); break;
            case 'm': newDate = new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + number, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds()); break;
            case 'd': newDate = new Date(Date.parse(dtTmp) + (86400000 * number)); break;
            case 'w': newDate = new Date(Date.parse(dtTmp) + ((86400000 * 7) * number)); break;
            case 'h': newDate = new Date(Date.parse(dtTmp) + (3600000 * number)); break;
            case 'n': newDate = new Date(Date.parse(dtTmp) + (60000 * number)); break;
            case 's': newDate = new Date(Date.parse(dtTmp) + (1000 * number)); break;
        }
        return newDate;
    },
    //日期格式化
    format: function (date, pattern) {
        if (!date) {
            return "";
        }

        if (arguments.length == 1) {
            pattern = arguments[0];
            date = new Date();
        }
        if ($.type(date) == "string") {
            date = utility.date.toDate(date)
        }
        pattern = pattern || "yyyy-MM-dd";
        function replacer(patternPart, result) {
            pattern = pattern.replace(patternPart, result);
        }

        var _pan = function (source, len, char, from) {
            char = char || "0";
            source = source.toString();
            var pre = source.length < len ? (new Array(len - source.length + 1)).join(char) : "";
            return from == "left" ? (pre + source) : (source + pre);
        }

        //对目标字符串的前面补0，使其达到要求的长度
        function pad(source, length) {
            return (source < 0 ? "-" : "") + _pan(source, length, "0", "left")
        }
        var year = date.getFullYear(),
            month = date.getMonth() + 1,
            date2 = date.getDate(),
            hours = date.getHours(),
            minutes = date.getMinutes(),
            seconds = date.getSeconds();

        replacer(/yyyy/g, pad(year, 4));
        replacer(/yy/g, pad(year.toString().slice(2), 2));
        replacer(/MM/g, pad(month, 2));
        replacer(/M/g, month);
        replacer(/dd/g, pad(date2, 2));
        replacer(/d/g, date2);

        replacer(/HH/g, pad(hours, 2));
        replacer(/H/g, hours);
        replacer(/hh/g, pad(hours % 12, 2));
        replacer(/h/g, hours % 12);
        replacer(/mm/g, pad(minutes, 2));
        replacer(/m/g, minutes);
        replacer(/ss/g, pad(seconds, 2));
        replacer(/s/g, seconds);

        return pattern;
    }
}


}

function random(lower, upper) {
    return Math.floor(Math.random() * (upper - lower)) + lower;
}
var ns = {
    charts:[],
    getColor:function(index){
        var colors = [
            [{offset: 0, color: '#24caff'}, {offset: 1, color: '#248aff'}],
            [{offset: 0, color: '#8602f0'}, {offset: 1, color: '#db00ff'}],
            [{offset: 0, color: '#4448d0'}, {offset: 1, color: '#6f48dc'}],
            [{offset: 0, color: '#711d8d'}, {offset: 1, color: '#8e1d89'}],
            [{offset: 0, color: '#2e33d1'}, {offset: 1, color: '#5a2ed1'}],
            [{offset: 0, color: '#3f8ad8'}, {offset: 1, color: '#3f5ed8'}],
            [{offset: 0, color: '#1588ff'}, {offset: 1, color: '#1544ff'}],
        ];
        
        if(index >= colors.length){
            index = 0;
        }
        return {
            color: 
                {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: colors[index],
                    global: false // 缺省为 false
                }
        }
    },
    autoPlay:function(myChart,time){
        //自动播放
        myChart.dispatchAction({
            type:"highlight",
            dataIndex:0
        });
        var i = 1;
        var _autoTime = setInterval(function(){

            myChart.dispatchAction({
                type:"downplay",
                seriesIndex:0
            });

            myChart.dispatchAction({
                type:"highlight",
                dataIndex:i
            });
            i++;
            if(i >= 5){
                i = 0;
            }
        },time);

        //鼠标移入停止自动播放
        myChart.on('mouseover',function(e){

            //鼠标移入停止播放
            myChart.dispatchAction({
                type:"downplay",
                seriesIndex:0
            });

            //下次自动播放的索引+1
            i = e.dataIndex + 1;

            //高亮当前鼠标移入块块
            myChart.dispatchAction({
                type:"highlight",
                dataIndex:e.dataIndex
            });

            //停止自动播放
            _autoTime && clearInterval(_autoTime);

        });
        //鼠标移开开始自动播放
        myChart.on('mouseout',function(){
            _autoTime && clearInterval(_autoTime); 
            _autoTime = setInterval(function(){
                myChart.dispatchAction({
                    type:"downplay",
                    seriesIndex:0
                });

                myChart.dispatchAction({
                    type:"highlight",
                    dataIndex:i
                });
                i++;
                if(i >= 5){
                    i = 0;
                }
            },time);
        })
    },
    chart1:function(){
        var self = this;
        var option = {
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            legend: {
                top:"20%",
                orient: 'vertical',
                left: 10,
                data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
            },
            series: [
                {
                    name: '访问来源',
                    type: 'pie',
                    radius: ['50%', '70%'],
                    hoverAnimation : true,
                    animation:true,
                    avoidLabelOverlap: false,
                    label: {
                        show: false,
                        position: 'center'
                    },
                    emphasis: {
                        label: {
                            show: true,
                            fontSize: '20',
                            fontWeight: 'bold',
                            color:"rgba(255,255,255,0.5)",
                            formatter: '{b}\n {c} ({d}%)'
                        }
                    },
                    data: [
                        {value: 335, name: '直接访问',itemStyle:self.getColor(0)},
                        {value: 310, name: '邮件营销',itemStyle:self.getColor(1)},
                        {value: 234, name: '联盟广告',itemStyle:self.getColor(2)},
                        {value: 135, name: '视频广告',itemStyle:self.getColor(3)},
                        {value: 1548, name: '搜索引擎',itemStyle:self.getColor(4)}
                    ]
                }
            ]
        };
        
        var myChart = echarts.init($("#chart-1")[0], 'mytheme');
        myChart.setOption(option);
        this.charts.push(myChart);
        this.autoPlay(myChart,2000);
    },
    chart3:function(){
        var self = this;

        var option = {
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            legend: {
                top:"20%",
                orient: 'vertical',
                left: 10,
                data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
            },
            series: [
                {
                    name: '访问来源',
                    type: 'pie',
                    radius: ['50%', '70%'],
                    hoverAnimation : true,
                    animation:true,
                    avoidLabelOverlap: false,
                    label: {
                        show: false,
                        position: 'center'
                    },
                    emphasis: {
                        label: {
                            show: true,
                            fontSize: '20',
                            fontWeight: 'bold',
                            color:"rgba(255,255,255,0.5)",
                            formatter: '{b}\n {c} ({d}%)'
                        }
                    },
                    data: [
                        {value: 335, name: '直接访问',itemStyle:self.getColor(3)},
                        {value: 310, name: '邮件营销',itemStyle:self.getColor(4)},
                        {value: 234, name: '联盟广告',itemStyle:self.getColor(0)},
                        {value: 135, name: '视频广告',itemStyle:self.getColor(1)},
                        {value: 1548, name: '搜索引擎',itemStyle:self.getColor(2)}
                    ]
                }
            ]
        };
        
        var myChart = echarts.init($("#chart-3")[0], 'mytheme');
        myChart.setOption(option);
        this.charts.push(myChart);
        this.autoPlay(myChart,2200);
    },
    chart4:function(){
        layui.use('table', function(){
        var table = layui.table;

        //第一个实例
        table.render({
        elem: '#demo',
        height: 'full-204',
        //url: '/demo/table/user/',
        page: true,
        data:[{
          "id": "10001"
          ,"username": "杜甫"
          ,"email": "xianxin@layui.com"
          ,"sex": "男"
          ,"city": "浙江杭州"
          ,"sign": "人生恰似一场修行"
          ,"experience": "116"
          ,"ip": "192.168.0.8"
          ,"logins": "108"
          ,"joinTime": "2016-10-14"
        }, {
          "id": "10002"
          ,"username": "李白"
          ,"email": "xianxin@layui.com"
          ,"sex": "男"
          ,"city": "浙江杭州"
          ,"sign": "人生恰似一场修行"
          ,"experience": "12"
          ,"ip": "192.168.0.8"
          ,"logins": "106"
          ,"joinTime": "2016-10-14"
          ,"LAY_CHECKED": true
        }, {
          "id": "10003"
          ,"username": "王勃"
          ,"email": "xianxin@layui.com"
          ,"sex": "男"
          ,"city": "浙江杭州"
          ,"sign": "人生恰似一场修行"
          ,"experience": "65"
          ,"ip": "192.168.0.8"
          ,"logins": "106"
          ,"joinTime": "2016-10-14"
        }, {
          "id": "10004"
          ,"username": "贤心"
          ,"email": "xianxin@layui.com"
          ,"sex": "男"
          ,"city": "浙江杭州"
          ,"sign": "人生恰似一场修行"
          ,"experience": "666"
          ,"ip": "192.168.0.8"
          ,"logins": "106"
          ,"joinTime": "2016-10-14"
        }, {
          "id": "10005"
          ,"username": "贤心"
          ,"email": "xianxin@layui.com"
          ,"sex": "男"
          ,"city": "浙江杭州"
          ,"sign": "人生恰似一场修行"
          ,"experience": "86"
          ,"ip": "192.168.0.8"
          ,"logins": "106"
          ,"joinTime": "2016-10-14"
        }, {
          "id": "10006"
          ,"username": "贤心"
          ,"email": "xianxin@layui.com"
          ,"sex": "男"
          ,"city": "浙江杭州"
          ,"sign": "人生恰似一场修行"
          ,"experience": "12"
          ,"ip": "192.168.0.8"
          ,"logins": "106"
          ,"joinTime": "2016-10-14"
        }, {
          "id": "10007"
          ,"username": "贤心"
          ,"email": "xianxin@layui.com"
          ,"sex": "男"
          ,"city": "浙江杭州"
          ,"sign": "人生恰似一场修行"
          ,"experience": "16"
          ,"ip": "192.168.0.8"
          ,"logins": "106"
          ,"joinTime": "2016-10-14"
        }, {
          "id": "10008"
          ,"username": "贤心"
          ,"email": "xianxin@layui.com"
          ,"sex": "男"
          ,"city": "浙江杭州"
          ,"sign": "人生恰似一场修行"
          ,"experience": "106"
          ,"ip": "192.168.0.8"
          ,"logins": "106"
          ,"joinTime": "2016-10-14"
        }],
        cols: [[ //表头
            {type:'checkbox'},
          {field: 'id', title: 'ID', width:80},
          {field: 'username', title: '用户名'},
          {field: 'sex', title: '性别', width:80},
          {field: 'city', title: '城市'} ,
          {field: 'sign', title: '签名', width: 177},
          {field: 'experience', title: '积分', width: 80},
          {field: 'score', title: '评分', width: 80},
          {field: 'classify', title: '职业', width: 80},
          {field: 'wealth', title: '财富', width: 135}
        ]]
        });

        });
    },
    renderCharts:function(){
        echarts.registerTheme('mytheme', mytheme);
        this.chart1();
        this.chart3();
        this.chart4();
    },

    getSearch:function(){
        
    },
    dosearch:function(){
        
    },
    bindEvt:function(){
        var self = this;
        $(window).on("resize",function(){
            for(var i = 0;i<self.charts.length;i++){
                self.charts[i].resize();
            }
        })
        
    },
    init:function(){
        this.renderCharts();
        this.bindEvt();
    }
}
$(document).ready(function(){
    ns.init();
})