package com.model2.mvc.view.purchase;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;

public class AddPurchaseViewAction extends Action {

	
	public String execute(	HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		
		ProductService service = new ProductServiceImpl();
		ProductVO productVO = service.findProduct(Integer.parseInt(request.getParameter("prod_no")));
		
		PurchaseService purchaseService = new PurchaseServiceImpl();
		
		Map<String, String> coupons = new HashMap<String, String>();
		coupons.put("userId", ((User)request.getSession().getAttribute("user")).getUserId() );
		coupons = purchaseService.checkCoupons(coupons); //쿠폰별 <쿠폰명, 할인율>이 담긴 coupons map. 조건에 맞는 쿠폰들을 모두 map에 담는다.
		
		//PurchaseView에서 쿠폰을 선택하여 view페이지를 reload한 경우
		if(request.getParameter("couponName")!=null) {
			
			String selectedCoupon = request.getParameter("couponName");
			System.out.println(selectedCoupon);
			
			int discount= Integer.parseInt( coupons.get(request.getParameter("couponName")) );
			int price = productVO.getPrice();
			productVO.setPrice( price *= ( (100-discount)/(float)100 )   );		
			
			System.out.println("할인율은"+discount+"할인상품가는"+productVO.getPrice());
			
		}
		
		
		
		for (String strKey : coupons.keySet() ) {
			 System.out.println("Debugging:: 이건 쿠폰정보@!!!!!!!!!!!!");
			 System.out.println( strKey+ " : " + coupons.get(strKey));        
		 }
		
		
		request.setAttribute("productVO", productVO); 
		request.setAttribute("coupons", coupons); //JSP View에서는 coupons의 쿠폰명을 select의 value로 띄우고, 그게 selected되면 JQuery로 price동적으로 할인률만큼 깎아줘라. 근데 여기 불가피하게 userId도 같이 담았다. 그니까 key가 "~coupon"포함 안하고있다면 continue하도록은 해줘라.
		
		
		
		 
		
		
		System.out.println("이건 coupons정보입니다!!!!!!!!!!!!!!!!!!!"+coupons);
		
		
		
		return "forward:/purchase/AddPurchaseView.jsp";
		
	}//end of execute
	
	
}
