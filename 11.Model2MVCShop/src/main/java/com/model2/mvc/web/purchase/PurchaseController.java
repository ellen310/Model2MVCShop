package com.model2.mvc.web.purchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;


//==> 구매관리 Controller
@Controller
@RequestMapping("/purchase/*")
public class PurchaseController {
	
	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;

	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	///Field
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
		
	public PurchaseController(){
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	int pageSize;

	
	
	//@RequestMapping("/addPurchase.do")
	@RequestMapping("addPurchase")
	public String addPurchase(@ModelAttribute("purchase")Purchase purchase, HttpServletRequest request) throws Exception {
		System.out.println("Controller:: /addPurchase");
		
		System.out.println(purchase);
		
		purchase.setPurchaseProd( productService.getProduct(Integer.parseInt(request.getParameter("prod_no"))) );
		
		purchase.setBuyer( userService.getUser( ((User)request.getSession().getAttribute("user")).getUserId() ) );
		purchase.setTranCode("1"); //1: 구매완료/재고없음, 2: 배송중, 3: 배송완료
		purchaseService.addPurchase(purchase);

		return "forward:/purchase/AddPurchase.jsp";
	}
	
	
	//@RequestMapping("/addPurchaseView.do")
	@RequestMapping("addPurchaseView")
	public ModelAndView addPurchaseView(HttpServletRequest request) throws Exception {

		Product product = productService.getProduct(Integer.parseInt(request.getParameter("prod_no")));
		
		Map<String, String> coupons = new HashMap<String, String>();
		coupons.put("userId", ((User)request.getSession().getAttribute("user")).getUserId() );
		coupons = purchaseService.checkCoupons(coupons); //쿠폰별 <쿠폰명, 할인율>이 담긴 coupons map. 조건에 맞는 쿠폰들을 모두 map에 담는다.
		
		//PurchaseView에서 쿠폰을 선택하여 view페이지를 reload한 경우
		if(request.getParameter("couponName")!=null) {
			
			String selectedCoupon = request.getParameter("couponName");
			System.out.println(selectedCoupon);
			
			int discount= Integer.parseInt( coupons.get(request.getParameter("couponName")) );
			int price = product.getPrice();
			product.setPrice( price *= ( (100-discount)/(float)100 )   );		
			
			System.out.println("할인율: "+discount+" | 할인상품가격:"+product.getPrice());
			
		}//컨트롤러는 비즈니스로직 수행하는 곳인데 여기서 처리하는게 맞는지 모르겠다(맞는거 같기도?)
		
		for (String strKey : coupons.keySet() ) {
			 System.out.println("Debugging:: 쿠폰정보");
			 System.out.println( strKey+ " : " + coupons.get(strKey));        
		 }
		
		ModelAndView modelAndview = new ModelAndView();
		modelAndview.addObject("product",product);
		modelAndview.addObject("coupons",coupons);	
		modelAndview.setViewName("forward:/purchase/AddPurchaseView.jsp");
		
		return modelAndview;
	}
	

	//@RequestMapping("/getPurchase.do")
	@RequestMapping("getPurchase")
	public String getPurchase(@RequestParam("tranNo") int tranNo, Model model) throws Exception{

		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		User user = userService.getUser(purchase.getBuyer().getUserId());
		purchase.setBuyer(user);
		
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/GetPurchase.jsp";
	}
	
	
	
	@RequestMapping("/listPurchase")
	public String listPurchase(@ModelAttribute("search") Search search , Model model , HttpSession session) throws Exception{

		System.out.println("/listPurchase");
		
		//페이지 설정
		if(search.getCurrentPage()==0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String,Object> map = new HashMap<>();
		map.put("search", search);
		map.put("userId", ((User)session.getAttribute("user")).getUserId());
		
		//비즈니스 로직 수행	
		map= purchaseService.getPurchaseList(map);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		
		//Model과 View연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/purchase/ListPurchase.jsp";
	}
	
	
	
	@RequestMapping("updatePurchase")
	public String updatePurchase(@ModelAttribute("purchase")Purchase purchase, @RequestParam("buyerId") String userId, Model model ) throws Exception{

		System.out.println("/updatePurchase");
		
		//purchase.setBuyer( userService.getUser(userId) );
		purchase = purchaseService.updatePurchase(purchase); 
		
		purchase = purchaseService.getPurchase(purchase.getTranNo()); //prod도 포함된 최종 db의 수정결과 가져오려고
		purchase.setBuyer( userService.getUser(userId) );
		System.out.println("업데이트 !!!!!!!!!!!!!!!!!!!!!!!!!!!"+purchase);
		
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/GetPurchase.jsp";
		
	}
	
	
	@RequestMapping("updatePurchaseView")
	public String updatePurchaseView( @ModelAttribute("purchase")Purchase purchase, @RequestParam int tranNo, Model model) throws Exception{

		System.out.println("/updatePurchaseView");
		
		purchase = purchaseService.getPurchase(tranNo);
		
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/UpdatePurchaseView.jsp";	
	}
	
	
	
	
	
	
	@RequestMapping("updateTranCode")
	public String updateTranCode( @ModelAttribute("purchase")Purchase purchase,  @RequestParam("menu")String menu) throws Exception{

		System.out.println("매핑돼써!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		purchaseService.updateTranCode(purchase);
		
		if(menu.equals("manage")) {
			return "forward:/product/listProduct"; 
		}else {
			return "forward:/purchase/listPurchase"; 
		}
	}
	
	
	
	
	
}