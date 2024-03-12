package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.dao.ProductDAO;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.impl.UserServiceImpl;

import oracle.sql.DATE;

public class PurchaseDAO {
	
	public PurchaseDAO(){
	}

	public void insertPurchase(PurchaseVO purchaseVO) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "select MAX(TRAN_NO) from TRANSACTION";
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		int tranNo=0;
		if(rs.next()) {
			tranNo = rs.getInt("MAX(TRAN_NO)");
		}
		
		sql = "insert into TRANSACTION values (?,?,?,?,?,?,?,?,?,?,?)";
		stmt = con.prepareStatement(sql);
		stmt.setInt(1, ++tranNo);
		stmt.setInt(2, purchaseVO.getPurchaseProd().getProdNo());
		stmt.setString(3, purchaseVO.getBuyer().getUserId());
		stmt.setString(4, purchaseVO.getPaymentOption());
		stmt.setString(5, purchaseVO.getReceiverName());
		stmt.setString(6, purchaseVO.getReceiverPhone());
		stmt.setString(7, purchaseVO.getDivyAddr());
		stmt.setString(8, purchaseVO.getDivyRequest());
		stmt.setString(9, purchaseVO.getTranCode());
		stmt.setDate(10, new Date(System.currentTimeMillis()));
		stmt.setString(11, purchaseVO.getDivyDate());
		
		stmt.executeUpdate();
		con.close();
	}

	public PurchaseVO findPurchase(int tranNo) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "select * from TRANSACTION where TRAN_NO=?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, tranNo);

		ResultSet rs = pStmt.executeQuery();

		PurchaseVO purchaseVO = new PurchaseVO();
		purchaseVO.setBuyer(new User());
		purchaseVO.setPurchaseProd(new ProductVO());
		while (rs.next()) {
			purchaseVO.setTranNo(tranNo);
			purchaseVO.getPurchaseProd().setProdNo(rs.getInt("prod_no"));
			purchaseVO.getBuyer().setUserId(rs.getString("buyer_id"));
			purchaseVO.setPaymentOption(rs.getString("payment_option").trim());
			purchaseVO.setReceiverName(rs.getString("receiver_name"));
			purchaseVO.setReceiverPhone(rs.getString("receiver_phone"));
			purchaseVO.setDivyAddr(rs.getString("dlvy_addr"));
			purchaseVO.setDivyRequest(rs.getString("dlvy_request"));
			purchaseVO.setTranCode(rs.getString("tran_status_code").trim());
			purchaseVO.setOrderDate(rs.getDate("order_date"));
			purchaseVO.setDivyDate(rs.getString("dlvy_date"));
		}
		
		System.out.print("구매상세목록DAO-findPurchase결과: ");
		System.out.println(purchaseVO);
		
		con.close();

		return purchaseVO;
	}

	public Map<String,Object> getPurchaseList(Search searchVO, String userId) throws Exception {
		
		Map<String , Object>  map = new HashMap<String, Object>();
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT buyer_id, user_name, cell_phone, tran_status_code, tran_no FROM users, transaction WHERE buyer_id = user_id AND buyer_id='"+userId+"'";

		//==> TotalCount GET
		int totalCount = this.getTotalCount(sql);
		System.out.println("ProductDAO :: totalCount  :: " + totalCount);
				
		//==> CurrentPage 게시물만 받도록 Query 다시구성
		sql = makeCurrentPageSql(sql, searchVO);
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
			
		List<PurchaseVO> list = new ArrayList<PurchaseVO>();
		String[] tranCodeList = new String[searchVO.getPageSize()];
		int[] tranNoList = new int[searchVO.getPageSize()];
		int i=0;
		while(rs.next()){
			PurchaseVO purchaseVO = new PurchaseVO();
			purchaseVO.setBuyer(new User());
			purchaseVO.getBuyer().setUserId(rs.getString("buyer_id"));
			purchaseVO.getBuyer().setUserName(rs.getString("user_name"));
			purchaseVO.getBuyer().setPhone(rs.getString("cell_phone"));
			
			list.add(purchaseVO);
			tranCodeList[i] = (rs.getString("TRAN_STATUS_CODE")==null)?"-1":rs.getString("tran_status_code").trim();
			tranNoList[i] = rs.getInt("TRAN_NO");
			i++;
		}
		
		
		//==> totalCount 정보 저장
		map.put("totalCount", new Integer(totalCount));
		
		//==> currentPage 의 게시물 정보 갖는 List 저장
		map.put("list", list);
		System.out.println("list.size() : "+ list.size());
		
		//==> tranCode 정보 갖는 List 저장
		System.out.println("tranCodeList.size() : "+ tranCodeList.length);
		map.put("tranCodeList", tranCodeList);
		
		//==> tranCode 정보 갖는 List 저장
		System.out.println("tranNoList.size() : "+ tranNoList.length);
		map.put("tranNoList", tranNoList);

		rs.close();
		pStmt.close();
		con.close();

		return map;
	}

/*
	//아직 구현X. 필요성X
	public HashMap<String,Object> getSaleList(Search searchVO) throws Exception {
	}
*/
	
	public void updatePurchase(PurchaseVO purchaseVO) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		//purchaseVO = findPurchase(purchaseVO.getTranNo());
		
		String sql = "UPDATE transaction SET payment_option=?, receiver_name=?, receiver_phone=?, dlvy_addr=?, dlvy_request=?, dlvy_date=? WHERE tran_no = ?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		
		pStmt.setString(1, purchaseVO.getPaymentOption());
		pStmt.setString(2, purchaseVO.getReceiverName());
		pStmt.setString(3, purchaseVO.getReceiverPhone());
		pStmt.setString(4, purchaseVO.getDivyAddr());
		pStmt.setString(5, purchaseVO.getDivyRequest());
		pStmt.setString(6, purchaseVO.getDivyDate());
		pStmt.setInt(7, purchaseVO.getTranNo());
		pStmt.executeUpdate();
		
		con.close();
		
		System.out.println("업데이트결과: "+ findPurchase(purchaseVO.getTranNo()));
	}
	

	public void updateTranCode(PurchaseVO purchaseVO) throws Exception {
			
			Connection con = DBUtil.getConnection();
	
			String sql = "UPDATE transaction SET tran_status_code=? WHERE tran_no=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, purchaseVO.getTranCode());
			stmt.setInt(2, purchaseVO.getTranNo());
			System.out.println(stmt.executeUpdate()+"개 행의 tran_status_code가 "+purchaseVO.getTranCode()+"로 Update됨");
			
			con.close();
	}
	
	
	
	// 게시판 Page 처리를 위한 전체 Row(totalCount)  return
	private int getTotalCount(String sql) throws Exception {
		
		sql = "SELECT COUNT(*) "+
		          "FROM ( " +sql+ ") countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if( rs.next() ){
			totalCount = rs.getInt(1);
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}
	
	// 게시판 currentPage Row 만  return 
	private String makeCurrentPageSql(String sql , Search search){
		sql = 	"SELECT * "+ 
					"FROM (		SELECT inner_table.* ,  ROWNUM AS row_seq " +
									" 	FROM (	"+sql+" ) inner_table "+
									"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
					"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
		
		System.out.println("ProductDAO :: make SQL :: "+ sql);	
		
		return sql;
	}

	
	public Map checkNewbieCoupon(Map<String,String> coupons) throws SQLException {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT reg_date FROM users WHERE user_id=?";
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, coupons.get("userId"));
		
		
		ResultSet rs = pStmt.executeQuery();
		if( rs.next() ){
			//Date regDate = new Date(rs.getDate("reg_date").getTime());
			//Date today = new Date(System.currentTimeMillis());
			System.out.println("regDate: "+ rs.getDate("reg_date").getTime());
			System.out.println("today: "+ System.currentTimeMillis());   //확인 후 지울 것.
			
			
			long subSec = (System.currentTimeMillis()) - rs.getDate("reg_date").getTime();
			int subDay = ( (int)subSec/1000 ) / (60*60*24) ;
			
			System.out.println("subDay: "+subDay);
			
			if(subDay<=3) { //가입한지 3일이 안 되었다면 쿠폰 줌.
				coupons.put("newbieCoupon", "10"); //뉴비쿠폰: 10%할인
			}
		}
		
		pStmt.close();
		rs.close();
		con.close();
		
		return coupons;
	}
	
	/*
	private Map checkTalentCoupon() {
		//연예인 테이블에 존재하면 map.put("talentCoupon", "30"); //연예인쿠폰: 30%할인
	}
	*/

	
}