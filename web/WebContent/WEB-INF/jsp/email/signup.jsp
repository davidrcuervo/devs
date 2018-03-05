<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table>
	<tr>
		<td align="center">La e-Tienda</td>
	</tr>
	<tr>
		<td>Welcome ${user.cn} ${user.sn }. You have registered successfully.</td>
	</tr>
	<tr>
		<td>Please, validate you email address and password use the link below</td>
	</tr>
	<tr>
		<td><a href="${page.urlWithPattern}/validate/${page.simpleEncrypt(user.uid)}">Click here to validate eMail and password</a></td>
	</tr>
	<tr>
		<td>Thank you.</td>
	<tr>
	<tr>
		<td align="center">123 Main St. Nashville, TN 37212</td>
	</tr>
</table>