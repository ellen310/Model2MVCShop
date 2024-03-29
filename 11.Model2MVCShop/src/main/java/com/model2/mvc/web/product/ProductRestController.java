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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseService;

@RestController
@RequestMapping("/product/*")
public class ProductRestController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	
	@Value("#{commonProperties['pageSize']}")
	private int pageSize;
	
	@Value("#{commonProperties['pageUnit']}")
	private int pageUnit;
	
	
	public ProductRestController(){
		System.out.println(this.getClass());
	}
	
	
	
	
	@RequestMapping( value="json/getAutoComplete/{searchKeyword}", method=RequestMethod.GET )
	public List getAutoComplete(@PathVariable("searchKeyword") String searchKeyword) throws Exception {
		
		System.out.println("json/getAutoComplete/"+searchKeyword);
		
		return productService.getAutoComplete(searchKeyword);
	}//end of addProduct
	
	
	
	
	
	@RequestMapping( value="json/getScrollData")
	public Map getAutoComplete(@RequestBody Search search) throws Exception {
		
		System.out.println("json/getScrollData");
		
		search.setCurrentPage(search.getCurrentPage());
		search.setPageSize(pageSize);
		
		return productService.getProductList(search);
		
	}//end of addProduct

	
	
	
	
	
}
