package com.model2.mvc.service.product;

import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.product.vo.ProductVO;


public interface ProductService {
	
	public void addProduct(ProductVO productVO) throws Exception;
	
	public ProductVO findProduct(int productNo) throws Exception;
	
	public Map<String, Object> getProductList(Search searchVO) throws Exception;
	
	public ProductVO updateProduct(ProductVO productVO) throws Exception;
	
	
}