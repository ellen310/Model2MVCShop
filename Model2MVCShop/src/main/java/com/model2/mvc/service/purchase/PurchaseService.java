package com.model2.mvc.service.purchase;

import java.util.HashMap;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.purchase.vo.PurchaseVO;


public interface PurchaseService {
	
	public void addPurchase(PurchaseVO purchaseVO) throws Exception;
	
	public PurchaseVO getPurchase(int purchaseNo) throws Exception;
	
	public Map<String, Object> getPurchaseList(Search searchVO, String userId) throws Exception;
	
	//public Map<String, Object> getSaleList(Search searchVO) throws Exception;
	
	public PurchaseVO updatePurchase(PurchaseVO purchaseVO) throws Exception;
	
	public void updateTranCode(PurchaseVO purchaseVO) throws Exception;
	
	public Map checkCoupons(Map<String,String> map) throws Exception;
	
}