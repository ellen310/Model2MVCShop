<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

    
    
<!DOCTYPE html>
<html>
<meta charset="UTF-8">
<head>
<title>Insert title here</title>
</head>
<body>

<form name="updatePurchase" action="/purchase/updatePurchaseView?tranNo=${purchase.tranNo}" method="post">

다음과 같이 구매가 되었습니다.

<table border=1>
	<tr>
		<td>물품번호</td>
		<td>${purchase.purchaseProd.prodNo}</td>
		<td></td>
	</tr>
	<tr>
		<td>구매자아이디</td>
		<td>${purchase.buyer.userId}</td>
		<td></td>
	</tr>
	<tr>
		<td>구매방법</td>
		<td>
			<c:if test="${purchase.paymentOption eq '1'}" >
				현금구매
			</c:if>
			
			<c:if test="${purchase.paymentOption eq '2'}" >
				신용구매
			</c:if>
			
		</td>
		<td></td>
	</tr>
	<tr>
		<td>수령자 이름</td>
		<td>${purchase.receiverName}</td> <!-- 구매자 이름: ${purchase.buyer.userName} -->
		<td></td>
	</tr>
	<tr>
		<td>수령자 연락처</td>
		<td>${purchase.receiverPhone}</td> <!-- 구매자 연락처: ${purchase.buyer.phone} -->
		<td></td>
	</tr>
	<tr>
		<td>수령자 주소</td>
		<td>${purchase.divyAddr}</td>
		<td></td>
	</tr>
		<tr>
		<td>구매요청사항</td>
		<td>${purchase.divyRequest}</td>
		<td></td>
	</tr>
	<tr>
		<td>배송희망일자</td>
		<td>${purchase.divyDate}</td>
		<td></td>
	</tr>
</table>
</form>

</body>
</html>