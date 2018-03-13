<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>${title == null ? 'WEB' : title}</title>
	
	<%-- LOADING STYLES --%>
	<c:forEach var="style" items="${page.styles}">
		${style}
	</c:forEach>
	
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
		
	<%-- LOADING SCRIPTS --%>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

	<script src='//cdn.tinymce.com/4/tinymce.min.js'></script>
	
	<c:forEach var="script" items="${page.scripts}">
		${script}
	</c:forEach>
	
</head>
<body>
	<div class="container">
	<div class="pull-right" style="margin-top: 5px;">${nav_fast_button}</div>
	<div style="margin-top: 5px;">
		
		<c:if test="${fn:length(allpathParts[0]) > 0}">
			<a href="${page.rootUrl}" class="btn btn-link"><span class="glyphicon glyphicon-home"></span>&nbsp; Home</a> &nbsp; | &nbsp;  
		</c:if>
	 
		<% 	String[] allpathParts = (String[])request.getAttribute("allpathParts"); 
			
			for(int c=0; c < allpathParts.length; c++){
				out.print("<a href=\"");
				for(int b=0; b <= c; b++){
					out.print("/" + allpathParts[b]);
				}
				out.print("\" >" + URLDecoder.decode(allpathParts[c], "UTF-8") + "</a>");
				out.print( c < allpathParts.length - 1 ? " <small><span class=\"glyphicon glyphicon-arrow-right\"></span></small> " : "");
			}
		%>
	</div>
	<div>${content}</div>
	</div>
</body>
</html>