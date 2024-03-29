<%@ page language="java" contentType="text/html; charset=EUC_KR"
    pageEncoding="EUC_KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
        
<!DOCTYPE html>

<html>
<head>
	<meta charset="EUC_KR">
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	
	<!--  ///////////////////////// Bootstrap, jQuery CDN ////////////////////////// -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" >
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" >
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" ></script>
	
	
   <!-- Bootstrap Dropdown Hover CSS -->
   <link href="/css/animate.min.css" rel="stylesheet">
   <link href="/css/bootstrap-dropdownhover.min.css" rel="stylesheet">
   <!-- Bootstrap Dropdown Hover JS -->
   <script src="/javascript/bootstrap-dropdownhover.min.js"></script>
   
   
   <!-- jQuery UI toolTip 사용 CSS-->
   <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
   <!-- jQuery UI toolTip 사용 JS-->
   <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	
	<!--  ///////////////////////// CSS ////////////////////////// -->
	<style>
	  body {
            padding-top : 50px;
        }
    </style>
    
     <!--  ///////////////////////// JavaScript ////////////////////////// -->
	<script type="text/javascript">
$(function(){
		
		$('#divyDate').datepicker({dateFormat: 'yy-mm-dd'});
		
		$('button:last').on("click", function(){ //수정
			$("form").attr("action" , "/purchase/updatePurchase?tranNo=${purchase.tranNo}").submit();
		});
		
		$('a:last').on("click", function(){ //취소
			$('form')[0].reset();
		});
		
	})
	</script>
</head>

<body>


	<!-- ToolBar Start /////////////////////////////////////-->
	<div class="navbar  navbar-default">
        <div class="container">
        	<a class="navbar-brand" href="/index.jsp">Model2 MVC Shop</a>
   		</div>
   	</div>
   	<!-- ToolBar End /////////////////////////////////////-->
   	

	<!--  화면구성 div Start /////////////////////////////////////-->
	<div class="container">
		<h1 class="bg-primary text-center">구매정보수정</h1>
		
		<!-- form Start /////////////////////////////////////-->
		<form class="form-horizontal">
			
		  <input type="hidden" name=tranNo value="${purchase.tranNo}"/>
		  
		  <div class="form-group">
		    <label for="buyerId" class="col-sm-offset-1 col-sm-3 control-label">구매자아이디</label>
		    <div class="col-sm-4">
		      <input type="buyerId" class="form-control" id="buyerId" name="buyerId" value="${purchase.buyer.userId}" readonly>
		    </div>
		  </div>
		  
		
		  <div class="form-group">
		    <label for="paymentOption" class="col-sm-offset-1 col-sm-3 control-label">구매방법</label>
		    <div class="col-sm-4">
		      <select 	name="paymentOption" 	class="ct_input_g" style="width: 100px; height: 19px" 
							maxLength="20">
				<option value="${purchase.paymentOption}" selected="${fn:trim(purchase.paymentOption) eq '1'}? 'selected' : '' " >현금구매</option>
				<option value="${purchase.paymentOption}" selected="${fn:trim(purchase.paymentOption) eq '2'}? 'selected' : '' " >카드구매</option>
				
			  </select>
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label for="receiverName" class="col-sm-offset-1 col-sm-3 control-label">수령자이름</label>
		    <div class="col-sm-4">
		      <input type="receiverName" class="form-control" id="receiverName" name="receiverName" value="${purchase.receiverName}">
		    </div>
		  </div>
		  
		   <div class="form-group">
		    <label for="receiverPhone" class="col-sm-offset-1 col-sm-3 control-label">수령자연락처</label>
		    <div class="col-sm-4">
		      <input type="receiverPhone" class="form-control" id="receiverPhone" name="receiverPhone" value="${purchase.receiverPhone}">
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label for="divyAddr" class="col-sm-offset-1 col-sm-3 control-label">수령자주소</label>
		    <div class="col-sm-4">
		      <input type="divyAddr" class="form-control" id="divyAddr" name="divyAddr" value="${purchase.divyAddr}" >
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label for="divyRequest" class="col-sm-offset-1 col-sm-3 control-label">구매요청사항</label>
		    <div class="col-sm-4">
		      <input type="divyRequest" class="form-control" id="divyRequest" name="divyRequest" value="${purchase.divyRequest}">
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label for="divyDate" class="col-sm-offset-1 col-sm-3 control-label">배송희망일자</label>
		    <div class="col-sm-4">
		      <input type="divyDate" class="form-control" id="divyDate" name="divyDate" value="${purchase.divyDate}">
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <div class="col-sm-offset-4  col-sm-4 text-center">
		      <button type="button" class="btn btn-primary"  >수 &nbsp;정</button>
			  <a class="btn btn-primary btn" href="#" role="button">취&nbsp;소</a>
		    </div>
		  </div>
		</form>
		<!-- form End /////////////////////////////////////-->
		  
	<!--  화면구성 div End /////////////////////////////////////-->	
	</div>


</body>
</html>