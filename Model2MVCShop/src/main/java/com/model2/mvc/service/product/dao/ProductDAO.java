package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.vo.ProductVO;

public class ProductDAO {
	
	public ProductDAO(){
	}

	public void insertProduct(ProductVO productVO) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "select MAX(PROD_NO) from PRODUCT";
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		int prodNo=0;
		if(rs.next()) {
			prodNo = rs.getInt("MAX(PROD_NO)");
		}
		
		sql = "insert into PRODUCT values (?,?,?,?,?,?,?)";
		stmt = con.prepareStatement(sql);
		stmt.setInt(1, ++prodNo);
		stmt.setString(2, productVO.getProdName());
		stmt.setString(3, productVO.getProdDetail());
		stmt.setString(4, productVO.getManuDate());
		stmt.setInt(5, productVO.getPrice());
		stmt.setString(6, productVO.getFileName());
		stmt.setDate(7, productVO.getRegDate());
		stmt.executeUpdate();
		
		con.close();
	}

	public ProductVO findProduct(int productNo) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "select * from PRODUCT where PROD_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, productNo);

		
		ResultSet rs = stmt.executeQuery();

		ProductVO productVO = null;
		while (rs.next()) {
			productVO = new ProductVO();
			productVO.setProdNo(rs.getInt("PROD_NO"));
			productVO.setProdName(rs.getString("PROD_NAME"));
			productVO.setProdDetail(rs.getString("PROD_DETAIL"));
			productVO.setManuDate(rs.getString("MANUFACTURE_DAY"));
			productVO.setPrice(rs.getInt("PRICE"));
			productVO.setFileName(rs.getString("IMAGE_FILE"));
			productVO.setRegDate(rs.getDate("REG_DATE"));
		}
		
		con.close();

		return productVO;
	}

	
	public Map<String , Object> getProductList(Search searchVO) throws Exception {
		
		Map<String , Object>  map = new HashMap<String, Object>();
		
		Connection con = DBUtil.getConnection();
		
		// Original Query 구성
		String sql = "SELECT product.prod_no, prod_name, price, reg_date, tran_status_code, tran_no FROM product, transaction WHERE product.prod_no = transaction.prod_no(+) ";
		
		if (searchVO.getSearchCondition() != null) {
			if ( searchVO.getSearchCondition().equals("0") &&  !searchVO.getSearchKeyword().equals("") ) {
				sql += "AND product.PROD_NO LIKE '%" + searchVO.getSearchKeyword()+"%'";
			}else if (searchVO.getSearchCondition().equals("1")) {
				sql += "AND PROD_NAME LIKE '%" + searchVO.getSearchKeyword()+"%'";
			}else if (searchVO.getSearchCondition().equals("2")) {
				sql += "AND PRICE=" + searchVO.getSearchKeyword();
			}
		}
		sql += " ORDER BY product.PROD_NO";
		
		System.out.println("ProductDAO::Original SQL :: " + sql);
		
		//==> TotalCount GET
		int totalCount = this.getTotalCount(sql);
		System.out.println("ProductDAO :: totalCount  :: " + totalCount);
		
		//==> CurrentPage 게시물만 받도록 Query 다시구성
		sql = makeCurrentPageSql(sql, searchVO);
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
	
		System.out.println(searchVO);

		List<ProductVO> list = new ArrayList<ProductVO>();
		String[] tranCodeList = new String[searchVO.getPageSize()];
		int[] tranNoList = new int[searchVO.getPageSize()];
		int i=0;
		while(rs.next()){
			ProductVO productVO = new ProductVO();
			productVO.setProdNo(rs.getInt("PROD_NO"));
			productVO.setProdName(rs.getString("PROD_NAME"));
			productVO.setPrice(rs.getInt("PRICE"));
			productVO.setRegDate(rs.getDate("REG_DATE"));
			
			list.add(productVO);
			tranCodeList[i] = (rs.getString("TRAN_STATUS_CODE")==null)?"-1":rs.getString("TRAN_STATUS_CODE").trim();
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
		
		//==> tranNo 정보 갖는 List 저장
		System.out.println("tranNoList.size() : "+ tranNoList.length);
		map.put("tranNoList", tranNoList);

		rs.close();
		pStmt.close();
		con.close();

		return map;
	}
	
	
	public void updateProduct(ProductVO productVO) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "update PRODUCT set PROD_NAME=?,PROD_DETAIL=?,PRICE=?,IMAGE_FILE=? where PROD_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, productVO.getProdName());
		stmt.setString(2, productVO.getProdDetail());
		stmt.setInt(3, productVO.getPrice());
		stmt.setString(4, productVO.getFileName());
		stmt.setInt(5, productVO.getProdNo());
		stmt.executeUpdate();
		
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
}