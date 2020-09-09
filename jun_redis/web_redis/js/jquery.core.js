var ylzans = {
	checkcard : function(obj,cardno,func) {
		var tmp = obj.GetSSSECardno("3501", 121);
		if(tmp.indexOf("+OK:") == -1){
			var json={succ:"0",
					desc:"读取社保卡失败！"};
			func(json);
			return;
		}
		if(tmp.indexOf(cardno) != -1&&cardno!=""){
			var json={succ:"2",
					desc:""};
			func(json);
			return;
		}
		var xmlHttp;
		// 创建xmlHttp
		if (window.ActiveXObject) {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		} else if (window.XMLHttpRequest) {
			xmlHttp = new XMLHttpRequest();
		}
		var url = "./checkcard?key=" + new Date().getTime();
		var queryString = "keyvalue=" + encodeURI(tmp);
		xmlHttp.open("POST", url, true);
		xmlHttp.onreadystatechange = function() {
			if (xmlHttp.readyState == 4) {
				if (xmlHttp.status == 200) {
					var json = eval('(' + xmlHttp.responseText.replace("[", "").replace("]", "") + ')');
					func(json);
				}
			}
		};
		xmlHttp.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded");
		xmlHttp.send(queryString);
	}
	
};