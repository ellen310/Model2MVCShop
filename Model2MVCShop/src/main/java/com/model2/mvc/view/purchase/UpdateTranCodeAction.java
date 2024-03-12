package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class UpdateTranCodeAction extends Action {
	
	public String execute(	HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		PurchaseVO purchaseVO = new PurchaseVO();
		
		purchaseVO.setTranNo(Integer.parseInt(request.getParameter("tranNo")));
		purchaseVO.setTranCode(request.getParameter("tranCode"));
		
		PurchaseService service = new PurchaseServiceImpl();
		service.updateTranCode(purchaseVO);
		request.setAttribute("purchaseVO", purchaseVO);
		
		if(request.getParameter("menu")!=null) {
			return "forward:/listProduct.do"; 
		}else {
			return "forward:/listPurchase.do"; 
		}
		
		
	}//end of execute

}
