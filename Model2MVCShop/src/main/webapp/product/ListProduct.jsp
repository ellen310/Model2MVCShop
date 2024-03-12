<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<html>
<head>
<title>상품 목록조회</title>
<meta charset="UTF-8"> 
<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
	// 검색 / page 두가지 경우 모두 Form 전송을 위해 JavaScrpt 이용  ======> 안그러면 검색누른쪽만 파라미터 넘어가니까. page클릭으로 넘기는 쪽은 getElementById.value로 처리
	function fncGetProductList(currentPage) {
		document.getElementById("currentPage").value = currentPage;
	   	document.detailForm.submit();		
	}
</script>


</head>

<body bgcolor="#ffffff" text="#000000">


<div style="width:98%; margin-left:10px;">

<form name="detailForm" action="/listProduct.do?menu=${param.menu}" method="post">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37">
			<img src="/images/ct_ttl_img01.gif" width="15" height="37"/>
		</td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">
							${param.menu eq 'manage'? "상품관리" : "상품목록조회"}
					</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37">
			<img src="/images/ct_ttl_img03.gif" width="12" height="37"/>
		</td>
	</tr>
</table>


<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		
		<td align="right">
			<select name="searchCondition" class="ct_input_g" style="width:80px">
				
				<option value="0" ${search.searchCondition eq '0'? "selected":""}>상품번호</option>
				<option value="1" ${search.searchCondition eq '1'? "selected":""}>상품명</option>
				<option value="2" ${search.searchCondition eq '2'? "selected":""}>상품가격</option>
				
			</select>
			<input type="text" name="searchKeyword"  class="ct_input_g" style="width:200px; height:19px" value="${!empty search.searchKeyword ? search.searchKeyword: ''}"/>
		</td>
	
		
		<td align="right" width="70">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="17" height="23">
						<img src="/images/ct_btnbg01.gif" width="17" height="23">
					</td>
					<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
						<a href="javascript:fncGetProductList(1);">검색</a> 
					</td>
					<td width="14" height="23">
						<img src="/images/ct_btnbg03.gif" width="14" height="23">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>


<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td colspan="11" >전체  ${resultPage.totalCount} 건수, 현재  ${resultPage.currentPage} 페이지</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">상품명</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">가격</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">등록일</td>	
		<td class="ct_line02"></td>
		<td class="ct_list_b">현재상태</td>	
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>
	
	<c:set var="no" value="${fn:length(list)}"/> 
	<c:forEach var="i" items = "${list}" varStatus="status">
		<c:set var="vo" value="${i}"/>
		<tr class="ct_list_pop">
			<td align="center">${no}
				<c:set var="no" value="${no-1}"/>
			</td>
			<td></td>
			<td align="left"><a href="/getProduct.do?prodNo=${vo.prodNo}&menu=${param.menu}">${vo.prodName}</a></td>
			<td></td>
			<td align="left">${vo.price}</td>
			<td></td>
			<td align="left">${vo.regDate}</td>
			<td></td>
			<td align="left">
				<c:choose>
					<c:when test="${tranCodeList[status.index] eq '1'}">
						<c:if test="${param.menu eq 'manage'}">
							구매완료 | <a href="/updateTranCode.do?tranNo=${tranNoList[status.index]}&tranCode=2&menu=${param.menu}&currentPage=${resultPage.currentPage}">배송하기</a>   
						</c:if>
						<c:if test="${param.menu eq 'search'}">
							재고없음  
						</c:if>
					</c:when>
					<c:when test="${tranCodeList[status.index] eq '2'}">
						<c:if test="${param.menu eq 'manage'}">
							배송중   
						</c:if>
						<c:if test="${param.menu eq 'search'}">
							재고없음  
						</c:if>
					</c:when>
					<c:when test="${tranCodeList[status.index] eq '3'}">
						<c:if test="${param.menu eq 'manage'}">
							배송완료   
						</c:if>
						<c:if test="${param.menu eq 'search'}">
							재고없음  
						</c:if>
					</c:when>
					<c:otherwise>
						판매중
					</c:otherwise>
				</c:choose>
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
		  <input type="hidden" id="menu" name="menu" value="${param.menu}"/>
		  
		  
		  	<c:if test="${resultPage.currentPage > resultPage.pageUnit}">
		  		<a href="javascript:fncGetProductList('${resultPage.currentPage-1}')">◀ 이전</a>
		  	</c:if>
		  	
		  	<c:forEach var="i" begin="${resultPage.beginUnitPage}" end="${resultPage.endUnitPage}">
		  		<c:if test="${resultPage.currentPage eq i}">
		  			<U><B><a href="javascript:fncGetProductList(${i})">${i}</a></B></U>	
		  		</c:if>
		  		<c:if test="${resultPage.currentPage ne i}">
		  			<a href="javascript:fncGetProductList(${i})">${i}</a>
		  		</c:if>
		  	
		  	</c:forEach>
		  
		  	<c:if test="${resultPage.endUnitPage < resultPage.maxPage}">
		  		<a href="javascript:fncGetProductList('${resultPage.endUnitPage+1}')">다음 ▶</a>
		  	</c:if>
		  	
		  	
			
    	</td>
	</tr>
</table>


<!--  페이지 Navigator 끝 -->

</form>

</div>
</body>
</html>
