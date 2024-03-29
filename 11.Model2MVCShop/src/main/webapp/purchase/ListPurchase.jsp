<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
    
    
<!DOCTYPE html>

<html>
<head>
	<title>구매 목록조회</title>

	<meta charset="UTF-8"> 
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	
	<!--  ///////////////////////// Bootstrap, jQuery CDN ////////////////////////// -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" >
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" >
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" ></script>
	
	<!--  ///////////////////////// CSS ////////////////////////// -->
	<style>
	  body {
            padding-top : 50px;
        }
    </style>
    
     <!--  ///////////////////////// JavaScript ////////////////////////// -->
	<script type="text/javascript">
	
		function fncGetUserList() {
			document.detailForm.submit();
		}
	
		$(function(){
			
			$('a').on("click", function(){ //메인으로 돌아가기
				self.location = "../main.jsp";
			});
			
		})
	</script>
	
</head>

<body>

	<!-- ToolBar Start /////////////////////////////////////-->
	<jsp:include page="/layout/toolbar.jsp" />
   	<!-- ToolBar End /////////////////////////////////////-->
	
	<!--  화면구성 div Start /////////////////////////////////////-->
	<div class="container">
	
		<div class="page-header text-info">
	       <h3>구매목록조회</h3>
	    </div>
	    
	    <!-- table 위쪽 검색 Start /////////////////////////////////////-->
	    <div class="row">
	    
		    <div class="col-md-6 text-left">
		    	<p class="text-primary">
		    		전체  ${resultPage.totalCount } 건수, 현재 ${resultPage.currentPage}  페이지
		    	</p>
		    </div>
		    
		    <div class="col-md-6 text-right">
			    <form class="form-inline" name="detailForm">
			    
				  <div class="form-group">
				    <select class="form-control" name="searchCondition" >
						<option value="0"  ${ ! empty search.searchCondition && search.searchCondition==0 ? "selected" : "" }>회원ID</option>
						<option value="1"  ${ ! empty search.searchCondition && search.searchCondition==1 ? "selected" : "" }>회원명</option>
					</select>
				  </div>
				  
				  <div class="form-group">
				    <label class="sr-only" for="searchKeyword">검색어</label>
				    <input type="text" class="form-control" id="searchKeyword" name="searchKeyword"  placeholder="검색어"
				    			 value="${! empty search.searchKeyword ? search.searchKeyword : '' }"  >
				  </div>
				  
				  <button type="button" class="btn btn-default">검색</button>
				  
				  <!-- PageNavigation 선택 페이지 값을 보내는 부분 -->
				  <input type="hidden" id="currentPage" name="currentPage" value=""/>
				  
				</form>
	    	</div>
	    	
		</div>
		<!-- table 위쪽 검색 Start /////////////////////////////////////-->
		
		
      <!--  table Start /////////////////////////////////////-->
      <table class="table table-hover table-striped" >
      
        <thead>
          <tr>
            <th align="center">No</th>
            <th align="left" >회원 ID</th>
            <th align="left">회원명</th>
            <th align="left">수령자전화번호</th>
            <th align="left">배송현황</th>
            <th align="left">정보수정</th>
          </tr>
        </thead>
       
		<tbody>
		
		  <c:set var="i" value="0" />
		  <c:forEach var="purchase" items="${list}">
			<c:set var="i" value="${ i+1 }" />
			<tr>
			  <td align="center">${ i }</td>
			  <td align="left"  title="Click : 구매정보 확인">${purchase.buyer.userId}</td>
			  <td align="left">${purchase.buyer.userName}</td>
			  <td align="left">${purchase.buyer.phone}</td>
			  <td align="left">
						현재
						<c:choose>
							<c:when test="${fn:trim(purchase.tranCode) eq '1'}">
								 구매완료(배송전) 
							</c:when>
							<c:when test="${fn:trim(purchase.tranCode) eq '2'}">
								 배송중 
							</c:when>
							<c:when test="${fn:trim(purchase.tranCode) eq '3'}">
								 배송완료 
							</c:when>
						</c:choose>
						상태입니다.
			<!-- 	<i class="glyphicon glyphicon-ok" id= "${user.userId}"></i>
			  	<input type="hidden" value="${user.userId}">    -->  
			  </td>
			  
			  <td align="left">
				<c:if test="${fn:trim(purchase.tranCode) eq '2'}">
					<a href="/purchase/updateTranCode?tranNo=${purchase.tranNo}&tranCode=3&menu=search&currentPage=${resultPage.currentPage}">물건도착</a>    
				</c:if>
			  </td>
			  
			  
			</tr>
          </c:forEach>
        
        </tbody>
      
      </table>
	  <!--  table End /////////////////////////////////////-->
	  
 	</div>
 	<!--  화면구성 div End /////////////////////////////////////-->
 	
 	
 	<!-- PageNavigation Start... -->
	<jsp:include page="../common/pageNavigator_new.jsp"/>
	<!-- PageNavigation End... -->



<!-- 
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

 -->

</body>
</html>