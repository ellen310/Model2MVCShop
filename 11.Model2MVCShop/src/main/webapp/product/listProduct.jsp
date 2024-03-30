<%@ page language="java" contentType="text/html; charset=EUC_KR" pageEncoding="EUC_KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<html>
<head>
	<title>��ǰ �����ȸ</title>

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
		
		//�ڵ� �ϼ�
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
		
		
		
		//���� ��ũ��
		var currentPage = 1;
		var no = ${search.pageSize};
		$(document).scroll(function(){
			
			if( (${resultPage.totalCount}/${search.pageSize}) < currentPage ) { //����������� �� �̻� �����͸� ��ȸ���� �ʵ���
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
													      '<img src="../images/uploadFiles/'+productList[i].fileName+'">'+
													      '<div class="caption">'+
													        '<h3>'+productList[i].prodName+'</h3>'+
													        '<h3>'+productList[i].price+'</h3>'+
													        '<p>';
													        
													        
								if(productTranList[i]==null){
									
									newRecord += '�Ǹ���'
									
								}else if(productTranList[i].TRAN_STATUS_CODE.trim()=='1'){
									
									newRecord += ('${menu}'=='manage' )? '���ſϷ� | <a href="/purchase/updateTranCode?tranNo='+productTranList[i].TRAN_NO+'&tranCode=2&menu=${menu}&currentPage=${resultPage.currentPage}">����ϱ�</a>':'������';
									
								}else if(productTranList[i].TRAN_STATUS_CODE.trim() == '2'){
									
									newRecord += ('${menu}'=='manage' )? '�����':'������';
									
								}else if(productTranList[i].TRAN_STATUS_CODE.trim() == '3'){
									newRecord += ('${menu}'=='manage' )? '��ۿϷ�':'';
									
								}//end of if
												
								
								newRecord += '</p>'+
										        '<p><a href="#" class="btn btn-primary" role="button" data-value="'+productList[i].prodNo+'">�󼼺���</a> <a href="#" class="btn btn-default" role="button">����</a></p>'+
											      '</div>'+
											 '</div>'+
									  	'</div>';
										
							$('table:last').append(newRecord);
							
						}//end of for
					}
				})//end of ajax
			}//end of if
		})//end of ���� ��ũ��
		
		
		$('button:contains("�˻�")').on("click", function(){
			fncGetUserList(1);
		});
		
		
		//���λ��� ����ϵ��� ��ư �̺�Ʈ ó���� ���� event delegation.
		$(document).on('click', 'a:contains("�󼼺���")', function(){
			self.location = "/product/getProduct?prodNo="+$(this).attr("data-value")+"&menu=${menu}"
		});
		
		
		$('a:contains("����")').on("click", function(){
			
		});
		
	})//end of function
		
	
</script>


</head>

<body>

	<!-- ToolBar Start /////////////////////////////////////-->
	<jsp:include page="/layout/toolbar.jsp" />
   	<!-- ToolBar End /////////////////////////////////////-->


<!--  ȭ�鱸�� div Start /////////////////////////////////////-->
	<div class="container">
	
		<div class="page-header text-info">
	       <h3>${menu eq 'manage'? "��ǰ����" : "��ǰ�����ȸ"}</h3>
	    </div>
	    
	    <!-- table ���� �˻� Start /////////////////////////////////////-->
	    <div class="row">
	    
		    <div class="col-md-6 text-left">
		    	<p class="text-primary">
		    		��ü  ${resultPage.totalCount } �Ǽ�, ���� ${resultPage.currentPage}  ������
		    	</p>
		    </div>
		    
		    <div class="col-md-6 text-right">
			    <form class="form-inline" name="detailForm" action="/product/listProduct?">
			    
				  <div class="form-group">
				    <select class="form-control" name="searchCondition" >
						<option value="1"  ${ ! empty search.searchCondition && search.searchCondition==1 ? "selected" : "" }>��ǰ��</option>
						<option value="0"  ${ ! empty search.searchCondition && search.searchCondition==0 ? "selected" : "" }>��ǰ��ȣ</option>
						<option value="2"  ${ ! empty search.searchCondition && search.searchCondition==2 ? "selected" : "" }>��ǰ����</option>
					</select>
				  </div>
				  
				  <div class="form-group">
				    <label class="sr-only" for="searchKeyword">�˻���</label>
				    <input type="text" class="form-control" id="searchKeyword" name="searchKeyword"  placeholder="�˻���"
				    			 value="${! empty search.searchKeyword ? search.searchKeyword : '' }"  >
				  </div>
				  
				  <input type="hidden" id="menu" name="menu" value="${menu}"/>
				  
				  <button type="button" class="btn btn-default">�˻�</button>
				  
				  <!-- PageNavigation ���� ������ ���� ������ �κ� -->
				  <input type="hidden" id="currentPage" name="currentPage" value=""/>
				  
				</form>
	    	</div>
	    	
		</div>
		<!-- table ���� �˻� End /////////////////////////////////////-->
		
		
      <!--  table Start /////////////////////////////////////-->
      <table class="table table-hover table-striped" >
      
       
		<tbody>
		
			<div class="row">
			
				  <c:set var="i" value="0" />
				  <c:forEach var="product" items="${list}">
						
					
						<div class="col-sm-4">
					   		 <div class="thumbnail">
							      <img src="../images/uploadFiles/${product.fileName}" alt="...">
							      <div class="caption">
							        <h3>${product.prodName}</h3>
							        <h3>${product.price} ��</h3>
							        <p>
							        
							        	<c:choose>
							        	
											<c:when test="${fn:trim(productTranList[i].TRAN_STATUS_CODE) eq '1'}">
												<c:if test="${menu eq 'manage'}">
													���ſϷ� | <a href="/purchase/updateTranCode?tranNo=${fn:trim(productTranList[i].TRAN_NO)}&tranCode=2&menu=${menu}&currentPage=${resultPage.currentPage}">����ϱ�</a>   
												</c:if>
												<c:if test="${menu eq 'search'}">
													������  
												</c:if>
											</c:when>
											<c:when test="${fn:trim(productTranList[i].TRAN_STATUS_CODE) eq '2'}">
												<c:if test="${menu eq 'manage'}">
													�����   
												</c:if>
												<c:if test="${menu eq 'search'}">
													������  
												</c:if>
											</c:when>
											<c:when test="${fn:trim(productTranList[i].TRAN_STATUS_CODE) eq '3'}">
												<c:if test="${menu eq 'manage'}">
													��ۿϷ�   
												</c:if>
												<c:if test="${menu eq 'search'}">
													������  
												</c:if>
											</c:when>
											<c:otherwise>
												�Ǹ���
											</c:otherwise>
										</c:choose>
							        
							        </p>
							        <p><a href="#" class="btn btn-primary" role="button" data-value="${product.prodNo}">�󼼺���</a> <a href="#" class="btn btn-default" role="button">����</a></p>
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
 	<!--  ȭ�鱸�� div End /////////////////////////////////////-->
 	
 


<!--  ������ Navigator �� -->


</div>
</body>
</html>
