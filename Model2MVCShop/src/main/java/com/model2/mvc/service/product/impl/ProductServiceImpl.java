package com.model2.mvc.service.product.impl;

import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.product.dao.ProductDAO;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.product.ProductService;


public class ProductServiceImpl implements ProductService{
	
	private ProductDAO productDAO;
	
	public ProductServiceImpl() {
		productDAO = new ProductDAO();
	}

	public void addProduct(ProductVO productVO) throws Exception {
		productDAO.insertProduct(productVO);
	}

	
	public ProductVO findProduct(int productNo) throws Exception {
		
		return productDAO.findProduct(productNo);
	}

	public Map<String,Object> getProductList(Search searchVO) throws Exception {
		return productDAO.getProductList(searchVO);
	}

	public ProductVO updateProduct(ProductVO productVO) throws Exception {
		productDAO.updateProduct(productVO);
		return productVO;
	}

}