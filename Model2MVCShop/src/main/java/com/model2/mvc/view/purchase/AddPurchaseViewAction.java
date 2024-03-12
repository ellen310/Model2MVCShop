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
		coupons = purchaseService.checkCoupons(coupons); //������ <������, ������>�� ��� coupons map. ���ǿ� �´� �������� ��� map�� ��´�.
		
		//PurchaseView���� ������ �����Ͽ� view�������� reload�� ���
		if(request.getParameter("couponName")!=null) {
			
			String selectedCoupon = request.getParameter("couponName");
			System.out.println(selectedCoupon);
			
			int discount= Integer.parseInt( coupons.get(request.getParameter("couponName")) );
			int price = productVO.getPrice();
			productVO.setPrice( price *= ( (100-discount)/(float)100 )   );		
			
			System.out.println("��������"+discount+"���λ�ǰ����"+productVO.getPrice());
			
		}
		
		
		
		for (String strKey : coupons.keySet() ) {
			 System.out.println("Debugging:: �̰� ��������@!!!!!!!!!!!!");
			 System.out.println( strKey+ " : " + coupons.get(strKey));        
		 }
		
		
		request.setAttribute("productVO", productVO); 
		request.setAttribute("coupons", coupons); //JSP View������ coupons�� �������� select�� value�� ����, �װ� selected�Ǹ� JQuery�� price�������� ���η���ŭ ������. �ٵ� ���� �Ұ����ϰ� userId�� ���� ��Ҵ�. �״ϱ� key�� "~coupon"���� ���ϰ��ִٸ� continue�ϵ����� �����.
		
		
		
		 
		
		
		System.out.println("�̰� coupons�����Դϴ�!!!!!!!!!!!!!!!!!!!"+coupons);
		
		
		
		return "forward:/purchase/AddPurchaseView.jsp";
		
	}//end of execute
	
	
}
