package com.model2.mvc.view.product;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;

public class GetProductAction extends Action {

	public String execute(	HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		String prodNo=request.getParameter("prodNo");
		System.out.println("find할 제품의 prodNO는===>"+prodNo);
		
		ProductService service = new ProductServiceImpl();
		ProductVO vo = service.findProduct(Integer.parseInt(prodNo));
		
		request.setAttribute("productVO", vo);
		
		
		if(request.getParameter("menu").equals("manage")) {
			return "forward:/product/UpdateProductView.jsp";
		}else {
			
			Cookie[] cookies = request.getCookies();
			boolean isHistoryExist=false;
			if (cookies!=null && cookies.length > 0) {
				for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
					if (cookie.getName().equals("history")) {
						isHistoryExist=true;
						String str = cookie.getValue()+"&"+prodNo;
						cookie.setValue(str);
						System.out.println(cookie.getValue()+"기존쿠키값");
						
						response.addCookie(new Cookie("history",str));
						
					}
				}
				if(!isHistoryExist) {
					response.addCookie(new Cookie("history", prodNo.toString()));
					System.out.println("쿠키생성함");
				}
			
			}
			
			
			return "forward:/product/readProduct.jsp";
		}
		
	}//end of execute
}
