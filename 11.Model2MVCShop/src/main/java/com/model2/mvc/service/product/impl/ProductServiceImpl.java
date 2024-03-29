package com.model2.mvc.service.product.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductDao;
import com.model2.mvc.service.product.ProductService;

@Service("productServiceImpl")
public class ProductServiceImpl implements ProductService{
	
	///Field
	@Autowired
	@Qualifier("productDaoImpl")
	private ProductDao productDao;	
	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}
	
	
	///Constructor
	public ProductServiceImpl() {
		System.out.println(this.getClass()+"의 Default 생성자");
	}
	

	///Method
	@Override
	public void addProduct(Product product) throws Exception {
		// TODO Auto-generated method stub
		
		productDao.addProduct(product);
	}
	
	public Product getProduct(int productNo) throws Exception {
		
		return productDao.getProduct(productNo);
	}

	public Map<String,Object> getProductList(Search search) throws Exception {
		List<Product> list = productDao.getProductList(search);
		List<Map<String, String>> productTranList = productDao.getProductTranList(search);//{tranNO:~, tranCode:~}형식의 map 여러개가 list에 담김
		int totalCount = productDao.getTotalCount(search);
		
		System.out.println("트랜리스트!!!!!!!!!!!!!!!!!!!!!!!!"+productTranList);
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("productTranList", productTranList);
		map.put("totalCount", new Integer(totalCount));
		return map;
	}

	@Override
	public Product updateProduct(Product product) throws Exception {
		// TODO Auto-generated method stub
		productDao.updateProduct(product);
		return product;
	}


	@Override
	public boolean checkDuplication(int prodNo) throws Exception {
		boolean result=true;
		Product product=productDao.getProduct(prodNo);
		if(product != null) {
			result=false;
		}
		return result;
	}


	@Override
	public List getAutoComplete(String searchKeyword) throws Exception {
		// TODO Auto-generated method stub
		return productDao.getAutoComplete(searchKeyword);
	}
	

}