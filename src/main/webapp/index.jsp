<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.nv.util.FrontendUtils" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript" src="<%=FrontendUtils.getJQueryPath()%>"></script>
<script type="text/JavaScript" src="/js/neua.js?v=<%=FrontendUtils.getJsFileVersion()%>"></script>
<script type="text/javascript">
const $j = jQuery.noConflict();

$j(document).ready(function() {
	NeuAWebSocket.connect();
	NeuAWebSocket.init();
});
</script>
</head>
<body>
	<br />
	測試傳送<br />
	<input type="text" id="testMsg" name="testMsg" value='{"router":"record","data":{"record":{"project":"LCS","websiteIdx":0,"website":"Home","userId":"000tony01"}}}' style="width: 600px;" />
	<input type="button" id="send" name="send" value="send" />
</body>
</html>