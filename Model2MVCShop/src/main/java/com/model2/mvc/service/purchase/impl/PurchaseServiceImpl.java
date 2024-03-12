package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.dao.PurchaseDAO;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class PurchaseServiceImpl implements PurchaseService {

	private PurchaseDAO purchaseDAO;
	
	public PurchaseServiceImpl() {
		purchaseDAO = new PurchaseDAO();
	}

	@Override
	public void addPurchase(PurchaseVO purchaseVO) throws Exception {
		// TODO Auto-generated method stub
		purchaseDAO.insertPurchase(purchaseVO);
	}

	@Override
	public PurchaseVO getPurchase(int tranNo) throws Exception {
		// TODO Auto-generated method stub
		return purchaseDAO.findPurchase(tranNo);
	}

	
	@Override
	public Map<String, Object> getPurchaseList(Search searchVO, String userId) throws Exception {
		// TODO Auto-generated method stub
		return purchaseDAO.getPurchaseList(searchVO, userId);
	}

	/*아직 불필요해서 미구현
	@Override
	public HashMap<String, Object> getSaleList(Search searchVO) throws Exception {
		// TODO Auto-generated method stub
		return purchaseDAO.getSaleList(searchVO);
	}
	*/

	@Override
	public PurchaseVO updatePurchase(PurchaseVO purchaseVO) throws Exception {
		purchaseDAO.updatePurchase(purchaseVO);
		
		//여기서 tranNO로 PurchaseVO를 새로 생성하기.(구매자 아이디와 이름이 세팅된.) 그걸 리턴해줘라.
		//UserService service2 = new UserServiceImpl();
		//User user = service2.getUser(purchaseVO.getBuyer().getUserId());
		//purchaseVO.getBuyer().setUserName( user.getUserName() );
		return purchaseVO; //수정되지않은 값이 그대로 return된다면, Purchase2생성해서 purchaseVO.tranNo 넣어서 get
	}

	@Override
	public void updateTranCode(PurchaseVO purchaseVO) throws Exception {
		// TODO Auto-generated method stub
		purchaseDAO.updateTranCode(purchaseVO);
	}
	
	public Map checkCoupons(Map<String,String> coupons) throws Exception{
		
		coupons = purchaseDAO.checkNewbieCoupon(coupons); 
		//coupons = purchaseDAO.checkTalentCoupon(coupons);  //이런 식으로 새로운 쿠폰 생기면 추가...
		
		return coupons;
	}

}
