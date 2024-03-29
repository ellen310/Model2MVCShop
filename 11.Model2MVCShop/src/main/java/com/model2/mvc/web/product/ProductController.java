package com.model2.mvc.web.product;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseService;

@Controller
@RequestMapping("/product/*")
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Value("#{commonProperties['pageSize']}")
	private int pageSize;
	
	@Value("#{commonProperties['pageUnit']}")
	private int pageUnit;
	
	
	///Method
	//@RequestMapping("/addProduct.do")   //파일시스템 추가 전
//	@RequestMapping(value="addProduct")
//	public String addProduct(@ModelAttribute("product") Product product) throws Exception {
//		
//		System.out.println("product/addProduct");
//		productService.addProduct(product);
//		return "forward:/product/addProduct.jsp";
//		
//	}//end of addProduct
	
	
	
	//@RequestMapping("/addProduct.do")
	@RequestMapping(value="addProduct")
	public String addProduct(@ModelAttribute("product") Product product) throws Exception {
		
		
		
		
		System.out.println("product/addProduct");
		productService.addProduct(product);
		return "forward:/product/addProduct.jsp";
	}//end of addProduct
	
	
	
	
	//@RequestMapping("/updateProductView.do")
	@RequestMapping(value="updateProductView")
	public String updateProductView() throws Exception{
		
		System.out.println("/product/updateProductView");

		return "redirect:/product/updateProductView.jsp";
	}//end of updateProductView
	
	
	
	
	
	//@RequestMapping("/updateProduct.do")
	@RequestMapping(value="updateProduct")
	public String updateProduct( @ModelAttribute("product") Product product) throws Exception{

		System.out.println("/product/updateProduct");
		
		productService.updateProduct(product);
		
		
		return "forward:/product/getProduct.jsp";
		
	}//end of updateProduct
	
	//@RequestMapping("/getProduct.do")
	@RequestMapping(value="getProduct")
	public String getProduct(@RequestParam("prodNo") int prodNo, @RequestParam("menu") String menu, 
								HttpServletRequest request,HttpServletResponse response ,Model model) throws Exception {
		
		
		Product product = productService.getProduct(prodNo);
		
		model.addAttribute("product", product);
		
		if(menu.equals("manage")) {
			return "forward:/product/updateProductView.jsp";
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
					response.addCookie(new Cookie("history", prodNo+""));
					System.out.println("쿠키생성함");
				}
			
			}
		}
			
		return "forward:/product/readProduct.jsp";
		
	}//end of getProduct
	
	
	
	//@RequestMapping("/listProduct.do")
	@RequestMapping(value="listProduct")
	public String listProduct( @ModelAttribute("search") Search search , @RequestParam String menu, Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/product/listProduct");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		
		if(map.get("productTranList")!=null) {
			model.addAttribute("productTranList", map.get("productTranList"));
		}
		
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		model.addAttribute("menu", menu);
		
		return "forward:/product/listProduct.jsp";
		
	}//end of listProduct
	
	

}
