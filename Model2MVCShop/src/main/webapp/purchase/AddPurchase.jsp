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

<form name="updatePurchase" action="/updatePurchaseView.do?tranNo=${purchaseVO.tranNo}" method="post">

다음과 같이 구매가 되었습니다.

<table border=1>
	<tr>
		<td>물품번호</td>
		<td>${purchaseVO.purchaseProd.prodNo}</td>
		<td></td>
	</tr>
	<tr>
		<td>구매자아이디</td>
		<td>${purchaseVO.buyer.userId}</td>
		<td></td>
	</tr>
	<tr>
		<td>구매방법</td>
		<td>
			<c:if test="${purchaseVO.paymentOption eq '1'}" >
				현금구매
			</c:if>
			
			<c:if test="${purchaseVO.paymentOption eq '2'}" >
				신용구매
			</c:if>
			
		</td>
		<td></td>
	</tr>
	<tr>
		<td>수령자 이름</td>
		<td>${purchaseVO.receiverName}</td> <!-- 구매자 이름: ${purchaseVO.buyer.userName} -->
		<td></td>
	</tr>
	<tr>
		<td>수령자 연락처</td>
		<td>${purchaseVO.receiverPhone}</td> <!-- 구매자 연락처: ${purchaseVO.buyer.phone} -->
		<td></td>
	</tr>
	<tr>
		<td>수령자 주소</td>
		<td>${purchaseVO.divyAddr}</td>
		<td></td>
	</tr>
		<tr>
		<td>구매요청사항</td>
		<td>${purchaseVO.divyRequest}</td>
		<td></td>
	</tr>
	<tr>
		<td>배송희망일자</td>
		<td>${purchaseVO.divyDate}</td>
		<td></td>
	</tr>
</table>
</form>

</body>
</html>