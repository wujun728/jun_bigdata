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
    chart2:function(){
        var option = {
            tooltip: {
                show:false,
                formatter: '{b} : 100%'
            },

            series: [
                {
                    name: '消息准确率',
                    radius :'70%',
                    axisLine:{
                        lineStyle:{
                            color:[[0.25, '#24cbff'], [0.5, '#9000ff'], [0.75, '#484cdc'] ,[1,'#9000ff']],
                            width:3
                        },
                    },
                    pointer: {
                      show: false//不显示指针
                    },
                    detail: {
                      formatter: "{value}%",
                      show: true,
                      fontWeight: "bolder",
                      fontSize: 50,
                      offsetCenter: [0, "20%"],
                      color:"#d100ff"
                    },//仪表盘详情
                    splitLine:{
                        lineStyle:{
                            width:2
                        },
                        length:6
                    },
                    axisTick:{
                        length:5
                    },
                    type: 'gauge',
                    title:{
                        color:"#24b8ff",
                        fontWeight:"bold",
                        offsetCenter: [0, "-20%"]
                    },
                    data: [{value: 100, name: '消息准确率'}]
                }
            ]
        };

        var myChart = echarts.init($("#chart-2")[0], 'mytheme');
        myChart.setOption(option);
        this.charts.push(myChart)

        setInterval(function () {
            //option.series[0].data[0].value = random(95,101);
            //myChart.setOption(option, true);
        },100);

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
        var self = this;
        var xAxisData = [];
        var datas = []
        for (var i = 0; i < 20; i++) {
            xAxisData.push('上海保利花语' + i);
            for(var a = 0 ;a < 6; a++){
                if(!datas[a]){
                   datas[a] = []; 
                }
                datas[a].push(random(1,random(1,100)));
            }
        }

        var option = {
            grid: {
                left: 50,
                right:0,
                top:20,
                bottom:50
              },
            legend: {
                data: ['待办消息数量', '待办消息异常数量', '异常消息修复数量','111', '222', '333'],
                bottom: 0
            },
            tooltip: {},
            xAxis: {
                show : true,
                data: xAxisData,
                splitLine: {
                    show: false
                }
            },
            yAxis: {
            },
            series: [
                {
                    name: '待办消息数量',
                    type: 'bar',
                    data: datas[0],
                    stack: 'one',
                    label: {show: true},
                    barMinHeight: 20,
                    //itemStyle:self.getColor(0),
                    animationDelay: function (idx) {
                        return idx * 10;
                    }
                },
                {
                    name: '待办消息异常数量',
                    type: 'bar',
                    data: datas[1],
                    stack: 'one',
                    label: {show: true},
                    barMinHeight: 20,
                    //itemStyle:self.getColor(1),
                    animationDelay: function (idx) {
                        return idx * 10 + 100;
                    }
                },
                {
                    name: '异常消息修复数量',
                    type: 'bar',
                    data: datas[2],
                    label: {show: true},
                    barMinHeight: 20,
                    stack: 'one',
                    //itemStyle:self.getColor(2),
                    animationDelay: function (idx) {
                        return idx * 10 + 100;
                    }
                },
                {
                    name: '111',
                    type: 'bar',
                    data: datas[3],
                    stack: 'two',
                    label: {show: true},
                    barMinHeight: 20,
                    //itemStyle:self.getColor(3),
                    animationDelay: function (idx) {
                        return idx * 10 + 100;
                    }
                },
                {
                    name: '222',
                    type: 'bar',
                    data: datas[4],
                    stack: 'two',
                    label: {show: true},
                    barMinHeight: 20,
                    //itemStyle:self.getColor(4),
                    animationDelay: function (idx) {
                        return idx * 10 + 100;
                    }
                },
                {
                    name: '333',
                    type: 'bar',
                    data: datas[5],
                    stack: 'two',
                    label: {show: true},
                    barMinHeight: 20,
                    //itemStyle:self.getColor(5),
                    animationDelay: function (idx) {
                        return idx * 10 + 100;
                    }
                }
            ],
            animationEasing: 'elasticOut',
            animationDelayUpdate: function (idx) {
                return idx * 5;
            }
        };
        
        var myChart = echarts.init($("#chart-4")[0], 'mytheme');
        myChart.setOption(option);
        this.charts.push(myChart);

        function _setData(){
            //先清空数据
            _.each(option.series,function(item){
                item.data = [];
            })
            myChart.setOption(option);

            //再返回数据
            _.each(option.series,function(item,index){
                item.data = datas[index];
            })
            myChart.setOption(option);
        }

        var autoTime = setInterval(function(){
            _setData();
        },3000)

        //鼠标移入停止自动播放
        myChart.on('mouseover',function(e){
            //停止自动播放
            autoTime && clearInterval(autoTime);

        });
        //鼠标移开开始自动播放
        myChart.on('mouseout',function(){
            autoTime && clearInterval(autoTime); 
            autoTime = setInterval(function(){
                _setData();
            },3000)
        })
    },
    chart5:function(){
        var data = {
            loadingTime: null,
            loadingIndex: 0,
            messageTop:0,
            data:[],
            messages:[
                ["正在监控保利消息数据中...","发消息正常","关消息异常，<span class='yc'>发现3条异常数据.</span>",'<span class="yc2">数据修复中...</span>','<span class="yc2">数据修复成功...</span>'],
                ["正在监控雅居乐消息数据中...","发消息正常","关消息异常，<span class='yc'>发现3条异常数据.</span>",'<span class="yc2">数据修复中...</span>','<span class="yc2">数据修复成功...</span>'],
                ["正在监控北辰消息数据中...","发消息正常","关消息异常，<span class='yc'>发现3条异常数据.</span>",'<span class="yc2">数据修复中...</span>','<span class="yc2">数据修复成功...</span>']
            ]
        }
        var myChart = new Vue({
            el: "#vue-messagelist",
            methods:{
                //显示loading
                showLoading:function(){
                    var self = this;
                    self.pushData(["<span class='showInBottom a-time003 block'>-------------</span>",
                        "<span class='showInRight a-time007 block'>消息统计中</span>",
                        "<span class='showInLeft a-time005 block'>></span>"
                        ]);
                    var loadingStr = ">";
                    self.loadingTime = setInterval(function(){

                        loadingStr = loadingStr + ">";
                        Vue.set(self.data, self.data.length - 1, loadingStr);//设置loading指示器

                        self.loadingIndex++;
                        if(self.loadingIndex>3){
                            self.loadingIndex = 0;
                            loadingStr = "";//如果指示器大于3，还原到 >
                        }

                    },300);
                },
                //停止loading
                stopLoading:function(){
                    console.log('停止loading')
                    var self = this;
                    var stopTime = setInterval(function(){
                        console.log(self.loadingIndex)
                        if(self.loadingIndex == 2){
                            self.pushData(["<span class='showInRight a-time003 block'>消息统计成功</span>",
                                "<span class='fadeIn a-time003 a-yjfx'>>>></span>"]);
                            self.loadingTime && clearInterval(self.loadingTime);
                            clearInterval(stopTime);
                            setTimeout(function(){
                                Vue.set(self.data, self.data.length-1, ">>>");//设置loading指示器
                                self.pushMessage();
                            },1500)
                        }
                    },100)
                },
                //将消息塞到数据内
                pushMessage:function(){
                    var self = this;
                    //对消息进行一个遍历
                    if(self.messages.length){
                        var firstGroup = self.messages[0];
                        var i = 0;
                        self.showMessAge = setInterval(function(){
                            var str = "<p class='showInBottom a-time003'>"+ firstGroup[i] +"</p>"
                            self.pushData(str);
                            i++;
                            if(i>=firstGroup.length){
                                clearInterval(self.showMessAge);
                                self.messages.splice(0,1);//消息展示完毕后，从队列删除第一组，然后再开始展示新的一组
                                if(self.messages.length){//如果消息队列还存在消息，则开始新的消息展示
                                    self.showLoading();//新的一组从loading开始
                                    setTimeout(function(){
                                        self.stopLoading();
                                    },2000)
                                }else{//没有消息队列后，尝试获取最新数据
                                    self.getData();
                                }
                            }
                        },1000);
                    }
                },
                //追加数据
                pushData:function(data){
                    var self = this;
                    //Vue.set(self.data, self.data.length, data);//设置loading指示器
                    if(_.isObject(data)){
                        self.data = self.data.concat(data);
                    }else{
                        self.data.push(data);
                    }
                    setTimeout(function(){
                        var listHeight = $(".messagelist").height();
                        var wrapHeight = $(".messagelist-wrap-p").height();
                        if((listHeight + 40) > wrapHeight){//如果消息列表大于容器高度，则将消息往上移动一部分。
                            if(listHeight <= wrapHeight){
                                self.messageTop = 0;
                            }else{
                                //self.messageTop += $(".messagelist li:last").height();
                                self.messageTop = listHeight - wrapHeight  + 20;
                            }
                            
                        }
                    },10)
                },
                //获取数据
                getData:function(){
                    console.log('开始获取数据')
                    var self = this;
                    self.showLoading();//新的一组从loading开始
                    setTimeout(function(){
                        var NewData  = [
                            ["正在监控保利消息数据中...","发消息正常","关消息异常，<span class='yc'>发现3条异常数据.</span>",'<span class="yc2">数据修复中...</span>','<span class="yc2">数据修复成功...</span>'],
                            ["正在监控雅居乐消息数据中...","发消息正常","关消息异常，<span class='yc'>发现3条异常数据.</span>",'<span class="yc2">数据修复中...</span>','<span class="yc2">数据修复成功...</span>']
                        ];
                        self.messages = NewData;
                        //如果存在数据
                        if(NewData){
                            console.log('获取到数据')
                            self.stopLoading();
                        }
                    },1200)
                }
            },
            created:function(){
                this.getData();
            },
            data: data,
        })
    },
    renderCharts:function(){
        echarts.registerTheme('mytheme', mytheme);
        this.chart1();
        this.chart2();
        this.chart3();
        this.chart4();
        this.chart5();
    },

    getSearch:function(){
        var data ={
            zuhu:"",
            time:""
        }
        
        var type = $(".search-item.on").attr("type");
        var dataRange = [];
        var nowDate = new Date();//;
        switch (type) {
            case "30":
                var begin = utility.date.format(utility.date.add(nowDate, "d", -29), "yyyy-MM-dd");//开始日期
                var end = utility.date.format(nowDate, "yyyy-MM-dd");//结束日期
                dataRange = [begin, end];
                break;
            case "90":
                var begin = utility.date.format(utility.date.add(nowDate, "d", -89), "yyyy-MM-dd");//开始日期
                var end = utility.date.format(nowDate, "yyyy-MM-dd");//结束日期
                dataRange = [begin, end];
                break;
            case "180":
                var begin = utility.date.format(utility.date.add(nowDate, "d", -179), "yyyy-MM-dd");//开始日期
                var end = utility.date.format(nowDate, "yyyy-MM-dd");//结束日期
                dataRange = [begin, end];
                break;
            default:
                break;
        }
        var zuhu = $("#js-select").val();
        data.time = dataRange;
        data.zuhu = zuhu;
        console.log(data)
    },
    dosearch:function(){
        this.getSearch();
    },
    bindEvt:function(){
        var self = this;
        $(window).on("resize",function(){
            for(var i = 0;i<self.charts.length;i++){
                self.charts[i].resize();
            }
        })
        $(".search-item").on("click",function(){
            $(this).addClass("on").siblings().removeClass("on");
            self.dosearch();
        })
        $("#js-select").on("change",function(){
            self.dosearch();
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