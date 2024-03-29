package com.model2.mvc.service.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;


//==> 회원관리에서 CRUD 추상화/캡슐화한 DAO Interface Definition
public interface ProductDao {
	
	// INSERT
	public void addProduct(Product product) throws Exception ;
	
	
	

	// SELECT ONE
	public Product getProduct(int prodNo) throws Exception ;

	// SELECT LIST
	public List<Product> getProductList(Search search) throws Exception ;
	
	//SELECT LIST
	public List<Map<String, String>> getProductTranList(Search search) throws Exception;
	
	
	

	// UPDATE
	public void updateProduct(Product product) throws Exception ;
	
	// 게시판 Page 처리를 위한 전체Row(totalCount)  return
	public int getTotalCount(Search search) throws Exception ;
	
	
	
	//SELECT LIST
	//추가기능:: 자동완성 기능
	public List getAutoComplete(String searchKeyword) throws Exception ;
	
}