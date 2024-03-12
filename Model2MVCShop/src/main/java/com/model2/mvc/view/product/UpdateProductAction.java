package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;

public class UpdateProductAction extends Action {

	
	@Override
	public String execute(	HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		request. setCharacterEncoding("UTF-8");
		ProductVO productVO=new ProductVO();
		productVO.setProdNo( Integer.parseInt(request.getParameter("prodNo")) );
		productVO.setProdName( (request.getParameter("prodName")) );
		productVO.setProdDetail( (request.getParameter("prodDetail")) );
		productVO.setManuDate(request.getParameter("manuDate"));
		productVO.setPrice(Integer.parseInt(request.getParameter("price"))); 
		productVO.setFileName(request.getParameter("fileName"));
		
		System.out.println(productVO);
		ProductService service=new ProductServiceImpl();
		service.updateProduct(productVO);
		
		request.setAttribute("productVO", productVO);
		
		if(request.getParameter("menu")!=null && request.getParameter("menu").equals("manage")) {
			return "forward:product/getProduct.jsp";
		}else {
			return "forward:product/readProduct.jsp";
		}
		
	}
	
}
