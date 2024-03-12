package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;

public class UpdatePurchaseAction extends Action {

	public String execute(	HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		request. setCharacterEncoding("UTF-8");
		
		PurchaseVO purchaseVO = new PurchaseVO();
		PurchaseService service=new PurchaseServiceImpl();
		
		purchaseVO = service.getPurchase(Integer.parseInt(request.getParameter("tranNo")));
		purchaseVO.setPaymentOption(request.getParameter("paymentOption"));
		purchaseVO.setReceiverName(request.getParameter("receiverName"));
		purchaseVO.setReceiverPhone(request.getParameter("receiverPhone"));
		purchaseVO.setDivyAddr(request.getParameter("receiverAddr"));
		purchaseVO.setDivyRequest(request.getParameter("receiverRequest"));
		purchaseVO.setDivyDate(request.getParameter("divyDate"));
		
		//업뎃할때: 구매자아이디, 구매자이름(?구매자 이름을 바꾼다는건 사용자이름을 바꾼단건데? //////구매방법, 수령자주소, 수령자연락처, 구매요청사항, 배송희망일자 변경.
		//해당 tranNo의 위 4개를 바꿈. 
		purchaseVO = service.updatePurchase(purchaseVO); 
		
		//화면에 보여줄 purchaseVO설정 (구매자정보&구매이력정보 통합)
		UserService service2 = new UserServiceImpl();
		User user = service2.getUser(purchaseVO.getBuyer().getUserId());
		purchaseVO.getBuyer().setUserName( user.getUserName() );
		
		request.setAttribute("purchaseVO", purchaseVO);
		
		return "forward:purchase/GetPurchase.jsp";
		
		
	}
}
