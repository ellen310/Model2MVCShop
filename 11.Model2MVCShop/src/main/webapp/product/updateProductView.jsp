<%@ page contentType="text/html; charset=EUC_KR"%>

<%@ page import="com.model2.mvc.service.domain.*" %>


<html>
<head>
<title>��ǰ��������</title>

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
   
   
   <!-- jQuery UI toolTip ��� CSS-->
   <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
   <!-- jQuery UI toolTip ��� JS-->
   <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	
	<!--  ///////////////////////// CSS ////////////////////////// -->
	<style>
	  body {
            padding-top : 50px;
        }
    </style>
    
     <!--  ///////////////////////// JavaScript ////////////////////////// -->
	<script type="text/javascript">
	
	
	function fncUpdateProduct(){
		document.detailForm.action='/product/updateProduct';
		document.detailForm.submit();
	}

	
	$(function(){
			
			$('#divyDate').datepicker({dateFormat: 'yy-mm-dd'});
			
			$('button:last').on("click", function(){ //����
				$("form").attr("action" , "/purchase/updatePurchase?tranNo=${purchase.tranNo}").submit();
			});
			
			$('a:last').on("click", function(){ //���
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
   	

	<!--  ȭ�鱸�� div Start /////////////////////////////////////-->
	<div class="container">
		<h1 class="bg-primary text-center">��ǰ����</h1>
		
		<!-- form Start /////////////////////////////////////-->
		<form class="form-horizontal">
			
		  <input type="hidden" name=prodNo value="${product.prodNo}"/>
		  
		  <div class="form-group">
		    <label for="buyerId" class="col-sm-offset-1 col-sm-3 control-label">��ǰ��</label>
		    <div class="col-sm-4">
		      <input type="text" class="form-control" id="prodName" name="prodName" value="${product.prodName}">
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label for="prodDetail" class="col-sm-offset-1 col-sm-3 control-label">��ǰ������</label>
		    <div class="col-sm-4">
		      <input type="text" class="form-control" id="prodDetail" name="prodDetail" value="${product.prodDetail}">
		    </div>
		  </div>
		  
		  
		  <div class="form-group">
		    <label for="manuDate" class="col-sm-offset-1 col-sm-3 control-label">��������</label>
		    <div class="col-sm-4">
		      <input type="text" class="form-control" id="manuDate" name="manuDate" value="${purchase.divyDate}">
		    </div>
		  </div>
		  
			
		  <div class="form-group">
		    <label for="price" class="col-sm-offset-1 col-sm-3 control-label">����</label>
		    <div class="col-sm-4">
		      <input type="text" class="form-control" id="price" name="price" value="${product.price}">
		    </div>
		  </div>
		  
		  
		  <div class="form-group">
		    <label for="imageFile" class="col-sm-offset-1 col-sm-3 control-label">��ǰ�̹���</label>
		    <div class="col-sm-4">
		      <input type="file" class="form-control" id="imageFile" name="imageFile" value="${purchase.divyDate}">
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <div class="col-sm-offset-4  col-sm-4 text-center">
		      <button type="button" class="btn btn-primary"  >�� &nbsp;��</button>
			  <a class="btn btn-primary btn" href="#" role="button">��&nbsp;��</a>
		    </div>
		  </div>
		</form>
		<!-- form End /////////////////////////////////////-->
		  
	<!--  ȭ�鱸�� div End /////////////////////////////////////-->	
	</div>








</body>
</html>