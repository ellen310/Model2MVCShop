package com.model2.mvc.service.purchase.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductDao;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseDao;
import com.model2.mvc.service.purchase.PurchaseService;

//==> 구매관련 서비스 구현한 Bean
@Service("purchaseServiceImpl")
public class PurchaseServiceImpl implements PurchaseService{
	
	///Field
	@Autowired
	@Qualifier("purchaseDaoImpl")
	private PurchaseDao purchaseDao;	
	public void setProductDao(PurchaseDao purchaseDao) {
		this.purchaseDao = purchaseDao;
	}
	///Constructor
	public PurchaseServiceImpl() {
		System.out.println(this.getClass()+"의 Default 생성자");
	}
	

	
	
	///Method
	@Override
	public void addPurchase(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		purchaseDao.addPurchase(purchase);
	}

	@Override
	public Purchase getPurchase(int tranNo) throws Exception {
		// TODO Auto-generated method stub
		return purchaseDao.getPurchase(tranNo);
	}


	@Override
	public Map<String, Object> getPurchaseList(Map map) throws Exception {
		// TODO Auto-generated method stub
		List<Purchase> list = purchaseDao.getPurchaseList(map);
		int totalCount = purchaseDao.getTotalCount((Search)map.get("search"));
		
		//Map<String,Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("totalCount", new Integer(totalCount));
		return map;
	}


	@Override
	public Purchase updatePurchase(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		purchaseDao.updatePurchase(purchase);
		return purchase;
	}


	@Override
	public void updateTranCode(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		purchaseDao.updateTranCode(purchase);
	}

	@Override
	public Map checkCoupons(Map<String,String> coupons) throws Exception{
		// TODO Auto-generated method stub
		coupons = purchaseDao.checkCoupon(coupons); 
		
		return coupons;
	}
	

}