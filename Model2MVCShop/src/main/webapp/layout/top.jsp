<%@ page contentType="text/html; charset=euc-kr" %>

<%@ page import="com.model2.mvc.service.domain.User" %>

<%
	User user=(User)session.getAttribute("user");
%>

<html>
<head>
<title> Model2 MVC Shop </title>

<link href="/css/left.css" rel="stylesheet" type="text/css">

</head>

<body topmargin="0" leftmargin="0">
 
<table width="100%" height="50" border="0" cellpadding="0" cellspacing="0" >
  <tr>
	<td height="10"></td>
	<td height="10" >&nbsp;</td>
  </tr>
  <tr>
    <td width="90%" height="30"><h2><a href="../main.jsp" target="rightFrame" style="text-decoration-line : none; color:black">&nbsp;Model2 MVC Shop </a></h2></td>
  </tr>
  <tr>
    <td height="20" align="right" background="/images/img_bg.gif" colspan="1">
	    <table align="right" width="200" border="0" cellspacing="0" cellpadding="0">
	        <tr> 
	          <td width="150" align="right">
		          <%	if(user == null) { %>
		              <a href="/user/loginView.jsp" target="rightFrame" style="text-decoration-line : none; color:black">로그인</a>   
		          <%}%>        
	          
		          <% if(user != null) {  %>
		            	<%if(user.getRole().equals("admin")) {%>
		            	<b>관리자</b>
		            	<%}else{ %>
		            	<b><%=user.getUserName() %></b> 
		            	<%} %>
		            	 님 | <a href="/logout.do" target="_parent" style="text-decoration-line : none; color:black">로그아웃</a>  
		           <% } %>
	          </td>
	        </tr>
	      </table>
      </td>
    <td height="20" background="/images/img_bg.gif">&nbsp;</td>
  </tr>
</table>

</body>
</html>