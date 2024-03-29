<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<html>
<head>
	<title>상품 목록조회</title>

	<meta charset="UTF-8"> 
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	
	<!--  ///////////////////////// Bootstrap, jQuery CDN ////////////////////////// -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" >
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" >
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" ></script>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
	<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
	<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
	
	<!--  ///////////////////////// CSS ////////////////////////// -->
	<style>
       body > div.container{
        	border: 3px solid #D6CDB7;
            margin-top: 100px;
        }
    </style>
    
     <!--  ///////////////////////// JavaScript ////////////////////////// -->
     
     
     
	<script type="text/javascript">
	
	
	function fncGetUserList(currentPage) {
		document.getElementById("currentPage").value = currentPage;
	   	document.detailForm.submit();		
	}
	
		$(function(){
			
			$('button:last').on("click", function(){ //확인
				self.location = "/product/listProduct?menu=manage";
			});
			
			$('a:last').on("click", function(){ //추가등록
				self.location = "../product/addProductView.jsp";
			});
			
		})
	</script>




<script type="text/javascript">
	function fncGetProductList(currentPage) {
		document.getElementById("currentPage").value = currentPage;
	   	document.detailForm.submit();		
	}
	
	
	$(function(){
		$('input[type=text]').on("keyup", function(){
			
			if($('option:selected').val() == 1){
				
				var searchKeyword = $('input').val();
				
				$.ajax({
					
					url : "/product/json/getAutoComplete/"+searchKeyword ,
					method : "GET" ,
					dataType : "json" ,
					headers : {
						"Accept" : "application/json",
						"Content-Type" : "application/json"
					},
					success : function(JSONData, status){ 
						  $( "input[type=text]" ).autocomplete({
						      source: JSONData
						  });
					}
					
				})//end of ajax
			}//end of if
		})//end of on
		
		
		
		var currentPage = 1;
		var no = ${search.pageSize};
		
		
		
		
		$(document).scroll(function(){
			
			if( (${resultPage.totalCount}/${search.pageSize}) < currentPage ) { //끝페이지라면 더 이상 데이터를 조회하지 않도록
				return;
			}
			
			var innerHeight = $(window).height() - $(window.parent.frames["topFrame"]).height();  //parent: index.jsp  높이==> rightFrame+topFrame
			var rightFrameHeight = $(this).height();
				
			if( ($(window).height() - window.innerHeight) == $(window).scrollTop()){
				
				$.ajax({
					
					url : "/product/json/getScrollData" ,
					method : "POST" ,
					data:JSON.stringify({ currentPage:++currentPage,searchCondition:"${search.searchCondition}",searchKeyword:"${search.searchKeyword}"}),
					dataType : "json" ,
					headers : {
						"Accept" : "application/json",
						"Content-Type" : "application/json"
					},
					success : function(JSONData, status){
						 
						var productList = JSONData.list;
						var productTranList = JSONData.productTranList;
						
						for(let i=0 ; i< productList.length ; i++){
							
								var strDate = new Date( productList[i].regDate);
								strDate = strDate.getFullYear() + '-' + strDate.getMonth() + '-' + strDate.getDate();
							
								var newRecord = '<tr class="ct_list_pop">'+
								'<td align="center">'+(++no)+
									'<c:set var="no" value="'+(++no)+'"/>'+ 
								'</td>'+
									'<td></td>'+
								'<td align="left"><a href="/product/getProduct?prodNo='+productList[i].prodNo+'&menu=${menu}">'+productList[i].prodName+'</a></td>'+
								'<td></td>'+
								'<td align="left">'+productList[i].price+'</td>'+
								'<td></td>'+
								'<td align="left">'+strDate+'</td>'+
								'<td></td>'+
								'<td align="left">';
								
								
								
									
								if(productTranList[i]==null){
									
									newRecord += '판매중'
									
								}else if(productTranList[i].TRAN_STATUS_CODE.trim()=='1'){
									
									newRecord += ('${menu}'=='manage' )? '구매완료 | <a href="/purchase/updateTranCode?tranNo='+productTranList[i].TRAN_NO+'&tranCode=2&menu=${menu}&currentPage=${resultPage.currentPage}">배송하기</a>':'재고없음';
									
								}else if(productTranList[i].TRAN_STATUS_CODE.trim() == '2'){
									
									newRecord += ('${menu}'=='manage' )? '배송중':'재고없음';
									
								}else if(productTranList[i].TRAN_STATUS_CODE.trim() == '3'){
									newRecord += ('${menu}'=='manage' )? '배송완료':'';
									
								}//end of if
								
								
								
								
								newRecord += '</td>'+	  
										'</tr>'+
										
										'<tr>'+
											'<td colspan="11" bgcolor="D6D7D6" height="1"></td>'
										'</tr>	';
				
										
										
										
							$('table:last').append(newRecord);
							
						}//end of for
						
					}
					
				})//end of ajax
				
				
			}//end of if

		})//end of scroll
		
	})//end of function
		
	
</script>


</head>

<body>

	<!-- ToolBar Start /////////////////////////////////////-->
	<jsp:include page="/layout/toolbar.jsp" />
   	<!-- ToolBar End /////////////////////////////////////-->


<!--  화면구성 div Start /////////////////////////////////////-->
	<div class="container">
	
		<div class="page-header text-info">
	       <h3>${menu eq 'manage'? "상품관리" : "상품목록조회"}</h3>
	    </div>
	    
	    <!-- table 위쪽 검색 Start /////////////////////////////////////-->
	    <div class="row">
	    
		    <div class="col-md-6 text-left">
		    	<p class="text-primary">
		    		전체  ${resultPage.totalCount } 건수, 현재 ${resultPage.currentPage}  페이지
		    	</p>
		    </div>
		    
		    <div class="col-md-6 text-right">
			    <form class="form-inline" name="detailForm" action="/product/listProduct?menu=${menu}">
			    
				  <div class="form-group">
				    <select class="form-control" name="searchCondition" >
						<option value="0"  ${ ! empty search.searchCondition && search.searchCondition==0 ? "selected" : "" }>상품번호</option>
						<option value="1"  ${ ! empty search.searchCondition && search.searchCondition==1 ? "selected" : "" }>상품명</option>
						<option value="2"  ${ ! empty search.searchCondition && search.searchCondition==2 ? "selected" : "" }>상품가격</option>
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
            <th align="left" >상품명</th>
            <th align="left">가격</th>
            <th align="left">등록일</th>
            <th align="left">현재상태</th>
          </tr>
        </thead>
       
		<tbody>
		
		  <c:set var="i" value="0" />
		  <c:forEach var="product" items="${list}">
			<c:set var="i" value="${ i+1 }" />
			<tr>
			  <td align="center">${ i }</td>
			  <td align="left"  title="Click : 상품정보 확인">${product.prodName}</td>
			  <td align="left">${product.price}</td>
			  <td align="left">${product.regDate}</td>
			  <td align="left">
			  	<c:choose>
					<c:when test="${fn:trim(productTranList[status.index].TRAN_STATUS_CODE) eq '1'}">
						<c:if test="${menu eq 'manage'}">
							구매완료 | <a href="/purchase/updateTranCode?tranNo=${fn:trim(productTranList[status.index].TRAN_NO)}&tranCode=2&menu=${menu}&currentPage=${resultPage.currentPage}">배송하기</a>   
						</c:if>
						<c:if test="${menu eq 'search'}">
							재고없음  
						</c:if>
					</c:when>
					<c:when test="${fn:trim(productTranList[status.index].TRAN_STATUS_CODE) eq '2'}">
						<c:if test="${menu eq 'manage'}">
							배송중   
						</c:if>
						<c:if test="${menu eq 'search'}">
							재고없음  
						</c:if>
					</c:when>
					<c:when test="${fn:trim(productTranList[status.index].TRAN_STATUS_CODE) eq '3'}">
						<c:if test="${menu eq 'manage'}">
							배송완료   
						</c:if>
						<c:if test="${menu eq 'search'}">
							재고없음  
						</c:if>
					</c:when>
					<c:otherwise>
						판매중
					</c:otherwise>
				</c:choose>
			
			  
			  </td>
			</tr>
			
				
          </c:forEach>
        
        </tbody>
      
   
      
      
      
      
      
      
      </table>
	  <!--  table End /////////////////////////////////////-->
	  
 	</div>
 	<!--  화면구성 div End /////////////////////////////////////-->
 	
 


<!--  페이지 Navigator 끝 -->

</form>

</div>
</body>
</html>
