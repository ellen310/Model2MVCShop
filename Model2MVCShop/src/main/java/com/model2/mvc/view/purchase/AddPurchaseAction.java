package com.model2.mvc.view.purchase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.impl.UserServiceImpl;

public class AddPurchaseAction extends Action {

	public String execute(	HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		
		PurchaseVO purchaseVO = new PurchaseVO();
		purchaseVO.setPurchaseProd( new ProductServiceImpl().findProduct(Integer.parseInt(request.getParameter("prod_no"))) );
		purchaseVO.setBuyer( new UserServiceImpl().getUser( ((User)request.getSession().getAttribute("user")).getUserId() ) );
		purchaseVO.setPaymentOption(request.getParameter("paymentOption"));  
		purchaseVO.setReceiverName(request.getParameter("receiverName"));
		purchaseVO.setReceiverPhone(request.getParameter("receiverPhone"));
		purchaseVO.setDivyAddr(request.getParameter("receiverAddr"));
		purchaseVO.setDivyRequest(request.getParameter("receiverRequest"));
		purchaseVO.setDivyDate(request.getParameter("receiverDate"));
		purchaseVO.setTranCode("1"); //1: 구매완료/재고없음, 2: 배송중, 3: 배송완료
		
		PurchaseService service = new PurchaseServiceImpl();
		service.addPurchase(purchaseVO);

		request.setAttribute("purchaseVO", purchaseVO);
		
		return "forward:/purchase/AddPurchase.jsp";
			
	}//end of execute
	
}
