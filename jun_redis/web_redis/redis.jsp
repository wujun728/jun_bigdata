<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'index.jsp' starting page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<SCRIPT src="js/jquery-1.7.2.min.js" type="text/javascript"></SCRIPT>
<SCRIPT src="js/jquery.core.js" type="text/javascript"></SCRIPT>
<style>
#show {
	width: 350px;
	height: 200px;
	border: 1px solid #ccc;
	margin: 0 auto;
	overflow:auto;
}
</style>
<script type="text/javascript">
	function sub() {
		$.post("./SubServlet",  function(data) {
		  
			if (data != "") {
			
				$("#show").append(data + "<br />");
			}
			sub();
		});

	}
	sub();
	function pub() {
	$.post("./PubServlet",{'content' : $('textarea').val()});
	}
</script>
</head>

<body>
	<div id="show"></div>
	<center>
		<textarea cols="48" row="5"></textarea>
		<br />
		<button onclick="pub()">发布</button>
	</center>
</body>
</html>
