package com.model2.mvc.view.purchase;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class ListPurchaseAction extends Action {
	
	@Override
	public String execute(	HttpServletRequest request,HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		
		
		Search searchVO = new Search();
		
		//페이지 설정
		int currentPage=1;
		if(request.getParameter("currentPage") != null)
			currentPage=Integer.parseInt(request.getParameter("currentPage"));
		searchVO.setCurrentPage(currentPage);
		
		//페이지 설정
		int pageSize=Integer.parseInt((getServletContext().getInitParameter("pageSize")));
		int pageUnit=Integer.parseInt((getServletContext().getInitParameter("pageUnit")));
		searchVO.setPageSize(pageSize);
		
		
		//DAO에게 일 넘기기
		PurchaseService service=new PurchaseServiceImpl();		
		Map<String,Object> map=service.getPurchaseList(searchVO, ((User)request.getSession().getAttribute("user")).getUserId());
		
		Page resultPage = new Page( currentPage, ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println("ListPurchaseAction ::"+resultPage);
		
		
		//Model과 View연결
		request.setAttribute("list", map.get("list"));
		request.setAttribute("tranCodeList", map.get("tranCodeList"));
		request.setAttribute("tranNoList", map.get("tranNoList"));
		request.setAttribute("resultPage", resultPage);
		request.setAttribute("search", searchVO);
		
		return "forward:/purchase/ListPurchase.jsp";
		
	}

}
