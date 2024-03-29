<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
    
    
<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8">
<title>구매 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
	function fncGetUserList() {
		document.detailForm.submit();
	}
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width: 98%; margin-left: 10px;">

<form name="detailForm" action="/purchase/listPurchase" method="post">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"width="15" height="37"></td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left: 10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">구매 목록조회</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37"><img src="/images/ct_ttl_img03.gif"	width="12" height="37"></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0"	style="margin-top: 10px;">
	<tr>
		<td colspan="11">전체  ${resultPage.totalCount} 건수, 현재 ${resultPage.currentPage} 페이지</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">회원ID</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">회원명</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">전화번호</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">배송현황</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">정보수정</td>
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>
	
	
	
	<c:set var="no" value="${fn:length(list)}"/> 
	<c:forEach var="i" items = "${list}" varStatus="status">
		<c:set var="vo" value="${i}"/>
		
		<tr class="ct_list_pop">
		<td align="center">
			<a href="/purchase/getPurchase?tranNo=${vo.tranNo}">${no}</a>
			<c:set var="no" value="${no-1}"/>
		</td>
		<td></td>
		<td align="left">
			<a href="/purchase/getUser?userId=${vo.buyer.userId}">${vo.buyer.userId}</a>
		</td>
		<td></td>
		<td align="left">${vo.buyer.userName}</td>
		<td></td>
		<td align="left">${vo.buyer.phone}</td>
		<td></td>
		<td align="left">
			현재
			<c:choose>
				<c:when test="${vo.tranCode eq '1'}">
					 구매완료(배송전) 
				</c:when>
				<c:when test="${vo.tranCode eq '2'}">
					 배송중 
				</c:when>
				<c:when test="${vo.tranCode eq '3'}">
					 배송완료 
				</c:when>
			</c:choose>
			상태입니다.
		</td>	
		<td></td>
		<td align="left">
			<c:if test="${vo.tranCode eq '2'}">
				<a href="/purchase/updateTranCode?tranNo=${vo.tranNo}&tranCode=3&menu=search&currentPage=${resultPage.currentPage}">물건도착</a>    
			</c:if>
		</td>  
		</tr>
		
		<tr>
			<td colspan="11" bgcolor="D6D7D6" height="1"></td>
		</tr>	
	</c:forEach>
	
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="center">
		
		  <input type="hidden" id="currentPage" name="currentPage" value=""/>
		  
		  	<c:if test="${resultPage.currentPage > resultPage.pageUnit}">
		  		<a href="/purchase/listPurchase?currentPage=${resultPage.currentPage-1}">◀ 이전</a>
		  	</c:if>
		  	
		  	<c:forEach var="i" begin="${resultPage.beginUnitPage}" end="${resultPage.endUnitPage}">
		  		<c:if test="${resultPage.currentPage eq i}">
		  			<U><B><a href="/purchase/listPurchase?currentPage=${i}">${i}</a></B></U>	
		  		</c:if>
		  		<c:if test="${resultPage.currentPage ne i}">
		  			<a href="/purchase/listPurchase?currentPage=${i}">${i}</a>
		  		</c:if>
		  	
		  	</c:forEach>
		  
		  	<c:if test="${resultPage.endUnitPage < resultPage.maxPage}">
		  		<a href="/purchase/listPurchase?currentPage=${resultPage.endUnitPage+1}">다음 ▶</a>
		  	</c:if>
		  	
		  	
			
    	</td>
	</tr>
</table>

<!--  페이지 Navigator 끝 -->
</form>

</div>
</body>
</html>