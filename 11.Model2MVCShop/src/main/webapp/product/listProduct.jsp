<%@ page language="java" contentType="text/html; charset=EUC_KR" pageEncoding="EUC_KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<html>
<head>
	<title>상품 목록조회</title>

	<meta charset="EUC_KR"> 
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
		
		//자동 완성
		$('#searchKeyword').on("keyup", function(){
			
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
		
		
		
		//무한 스크롤
		var currentPage = 1;
		var no = ${search.pageSize};
		$(document).scroll(function(){
			
			if( (${resultPage.totalCount}/${search.pageSize}) < currentPage ) { //끝페이지라면 더 이상 데이터를 조회하지 않도록
				return;
			}
				
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
							
								var newRecord = 	'<div class="col-sm-4">'+
												   		 '<div class="thumbnail">'+
													      '<img src="data:image/jpeg;base64,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" alt="...">'+
													      '<div class="caption">'+
													        '<h3>'+productList[i].prodName+'</h3>'+
													        '<h3>'+productList[i].price+'</h3>'+
													        '<p>';
													        
													        
								if(productTranList[i]==null){
									
									newRecord += '판매중'
									
								}else if(productTranList[i].TRAN_STATUS_CODE.trim()=='1'){
									
									newRecord += ('${menu}'=='manage' )? '구매완료 | <a href="/purchase/updateTranCode?tranNo='+productTranList[i].TRAN_NO+'&tranCode=2&menu=${menu}&currentPage=${resultPage.currentPage}">배송하기</a>':'재고없음';
									
								}else if(productTranList[i].TRAN_STATUS_CODE.trim() == '2'){
									
									newRecord += ('${menu}'=='manage' )? '배송중':'재고없음';
									
								}else if(productTranList[i].TRAN_STATUS_CODE.trim() == '3'){
									newRecord += ('${menu}'=='manage' )? '배송완료':'';
									
								}//end of if
												
								
								newRecord += '</p>'+
										        '<p><a href="#" class="btn btn-primary" role="button" data-value="'+productList[i].prodNo+'">상세보기</a> <a href="#" class="btn btn-default" role="button">구매</a></p>'+
											      '</div>'+
											 '</div>'+
									  	'</div>';
										
							$('table:last').append(newRecord);
							
						}//end of for
					}
				})//end of ajax
			}//end of if
		})//end of 무한 스크롤
		
		
		$('button:contains("검색")').on("click", function(){
			fncGetUserList(1);
		});
		
		
		//새로생긴 썸네일들의 버튼 이벤트 처리를 위해 event delegation.
		$(document).on('click', 'a:contains("상세보기")', function(){
			self.location = "/product/getProduct?prodNo="+$(this).attr("data-value")+"&menu=${menu}"
		});
		
		
		$('a:contains("구매")').on("click", function(){
			
		});
		
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
			    <form class="form-inline" name="detailForm" action="/product/listProduct?">
			    
				  <div class="form-group">
				    <select class="form-control" name="searchCondition" >
						<option value="1"  ${ ! empty search.searchCondition && search.searchCondition==1 ? "selected" : "" }>상품명</option>
						<option value="0"  ${ ! empty search.searchCondition && search.searchCondition==0 ? "selected" : "" }>상품번호</option>
						<option value="2"  ${ ! empty search.searchCondition && search.searchCondition==2 ? "selected" : "" }>상품가격</option>
					</select>
				  </div>
				  
				  <div class="form-group">
				    <label class="sr-only" for="searchKeyword">검색어</label>
				    <input type="text" class="form-control" id="searchKeyword" name="searchKeyword"  placeholder="검색어"
				    			 value="${! empty search.searchKeyword ? search.searchKeyword : '' }"  >
				  </div>
				  
				  <input type="hidden" id="menu" name="menu" value="${menu}"/>
				  
				  <button type="button" class="btn btn-default">검색</button>
				  
				  <!-- PageNavigation 선택 페이지 값을 보내는 부분 -->
				  <input type="hidden" id="currentPage" name="currentPage" value=""/>
				  
				</form>
	    	</div>
	    	
		</div>
		<!-- table 위쪽 검색 End /////////////////////////////////////-->
		
		
      <!--  table Start /////////////////////////////////////-->
      <table class="table table-hover table-striped" >
      
       
		<tbody>
		
			<div class="row">
			
				  <c:set var="i" value="0" />
				  <c:forEach var="product" items="${list}">
						
					
						<div class="col-sm-4">
					   		 <div class="thumbnail">
							      <img src="\images\uploadFiles\AHlbAAAAug1vsgAA.jpg" alt="...">
							      <div class="caption">
							        <h3>${product.prodName}</h3>
							        <h3>${product.price} 원</h3>
							        <p>
							        
							        	<c:choose>
							        	
											<c:when test="${fn:trim(productTranList[i].TRAN_STATUS_CODE) eq '1'}">
												<c:if test="${menu eq 'manage'}">
													구매완료 | <a href="/purchase/updateTranCode?tranNo=${fn:trim(productTranList[status.index].TRAN_NO)}&tranCode=2&menu=${menu}&currentPage=${resultPage.currentPage}">배송하기</a>   
												</c:if>
												<c:if test="${menu eq 'search'}">
													재고없음  
												</c:if>
											</c:when>
											<c:when test="${fn:trim(productTranList[i].TRAN_STATUS_CODE) eq '2'}">
												<c:if test="${menu eq 'manage'}">
													배송중   
												</c:if>
												<c:if test="${menu eq 'search'}">
													재고없음  
												</c:if>
											</c:when>
											<c:when test="${fn:trim(productTranList[i].TRAN_STATUS_CODE) eq '3'}">
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
							        
							        </p>
							        <p><a href="#" class="btn btn-primary" role="button" data-value="${product.prodNo}">상세보기</a> <a href="#" class="btn btn-default" role="button">구매</a></p>
							      </div>
							 </div>
					  	</div>
					  	<c:set var="i" value="${ i+1 }" />
		          </c:forEach>
	          
			</div>
        
        </tbody>
      
      
      
      </table>
	  <!--  table End /////////////////////////////////////-->
	  
 	</div>
 	<!--  화면구성 div End /////////////////////////////////////-->
 	
 


<!--  페이지 Navigator 끝 -->


</div>
</body>
</html>
