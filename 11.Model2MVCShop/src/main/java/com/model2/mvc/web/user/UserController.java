package com.model2.mvc.web.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserService;


//==> 회원관리 Controller
@Controller
@RequestMapping("/user/*")
public class UserController {
	
	///Field
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	//setter Method 구현 않음
		
	public UserController(){
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	
	@RequestMapping( value="addUser", method=RequestMethod.GET )
	public String addUser() throws Exception{
	
		System.out.println("/user/addUser : GET");
		
		return "redirect:/user/addUserView.jsp";
	}
	
	@RequestMapping( value="addUser", method=RequestMethod.POST )
	public String addUser( @ModelAttribute("user") User user ) throws Exception {

		System.out.println("/user/addUser : POST");
		//Business Logic
		userService.addUser(user);
		
		//return "redirect:/user/loginView.jsp";
		return "redirect:http://192.168.0.56:3000/Logon";
	}
	

	@RequestMapping( value="getUser", method=RequestMethod.GET )
	public String getUser( @RequestParam("userId") String userId , Model model ) throws Exception {
		
		System.out.println("/user/getUser : GET");
		//Business Logic
		User user = userService.getUser(userId);
		// Model 과 View 연결
		model.addAttribute("user", user);
		
		return "forward:/user/getUser.jsp";
	}
	

	@RequestMapping( value="updateUser", method=RequestMethod.GET )
	public String updateUser( @RequestParam("userId") String userId , Model model ) throws Exception{

		System.out.println("/user/updateUser : GET");
		//Business Logic
		User user = userService.getUser(userId);
		// Model 과 View 연결
		model.addAttribute("user", user);
		
		return "forward:/user/updateUser.jsp";
	}

	@RequestMapping( value="updateUser", method=RequestMethod.POST )
	public String updateUser( @ModelAttribute("user") User user , Model model , HttpSession session) throws Exception{

		System.out.println("/user/updateUser : POST");
		//Business Logic
		userService.updateUser(user);
		
		String sessionId=((User)session.getAttribute("user")).getUserId();
		if(sessionId.equals(user.getUserId())){
			session.setAttribute("user", user);
		}
		
		return "redirect:/user/getUser?userId="+user.getUserId();
	}
	
	
	@RequestMapping( value="login", method=RequestMethod.GET )
	public String login() throws Exception{
		
		System.out.println("/user/logon : GET");

		return "redirect:/user/loginView.jsp";
	}
	
	@RequestMapping( value="login", method=RequestMethod.POST )
	public String login(@ModelAttribute("user") User user , HttpSession session ) throws Exception{
		
		System.out.println("/user/login : POST");
		//Business Logic
		User dbUser=userService.getUser(user.getUserId());
		
		if( user.getPassword().equals(dbUser.getPassword())){
			session.setAttribute("user", dbUser);
		}
		
		return "redirect:/index.jsp";
	}
		
	
	@RequestMapping( value="logout", method=RequestMethod.GET )
	public String logout(HttpSession session ) throws Exception{
		
		System.out.println("/user/logout : POST");
		
		session.invalidate();
		
		return "redirect:/index.jsp";
	}
	
	
	@RequestMapping( value="checkDuplication", method=RequestMethod.POST )
	public String checkDuplication( @RequestParam("userId") String userId , Model model ) throws Exception{
		
		System.out.println("/user/checkDuplication : POST");
		//Business Logic
		boolean result=userService.checkDuplication(userId);
		// Model 과 View 연결
		model.addAttribute("result", new Boolean(result));
		model.addAttribute("userId", userId);

		return "forward:/user/checkDuplication.jsp";
	}

	
	@RequestMapping( value="listUser" )
	public String listUser( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/user/listUser : GET / POST");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=userService.getUserList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/user/listUser.jsp";
	}
	
	
	
	
	
	
	/////////////////////////////////////////
	
/*	//React 전.
	//인가코드 받기 (카카오 로그인시 / 카카오 로그인 세션 있을시)
	@RequestMapping( value="login/kakao/authorization", method=RequestMethod.GET )
	public String loginWithKakaoAuthorization(@RequestParam(required = false) String code, HttpSession session, Model model) throws Exception{
		System.out.println("인가코드: "+code);
		Map<String, String> map = getUserInfo( getToken(code) );
		
		User dbUser = userService.getUser(map.get("id"));
		
		if(dbUser!=null) { //이 유저가 카카오 id로 가입한 적이 있는 유저라면 바로 로그인
			session.setAttribute("user", dbUser);
			return "redirect:/index.jsp";
		}else {
			model.addAttribute("userInfo", map);
			model.addAttribute("id", map.get("id"));
			System.out.println("뭐야?"+model.getAttribute("userInfo"));
			return "forward:/user/addUserView.jsp";
		}
	}
*/
	
	//React용
	//인가코드 받기 (카카오 로그인시 / 카카오 로그인 세션 있을시)
	@RequestMapping( value="login/kakao/authorization", method=RequestMethod.GET )
	public String loginWithKakaoAuthorization(@RequestParam(required = false) String code, HttpSession session, Model model) throws Exception{
		System.out.println("인가코드: "+code);
		Map<String, String> map = getUserInfo( getToken(code) );
		
		User dbUser = userService.getUser(map.get("id"));
		
		if(dbUser!=null) { //이 유저가 카카오 id로 가입한 적이 있는 유저라면 바로 로그인
			session.setAttribute("user", dbUser);
			//return "redirect:http://localhost:3000/logon/"+map.get("id")+"/user"; //url파라미터로 user정보를 같이 내려보낸다. 카카오 가입 유저는 role이 무조건 user로 고정.
			return "redirect:http://192.168.0.56:3000/logon/"+map.get("id")+"/user"; //url파라미터로 user정보를 같이 내려보낸다. 카카오 가입 유저는 role이 무조건 user로 고정.
		}else {
			model.addAttribute("userInfo", map);
			model.addAttribute("id", map.get("id"));
			System.out.println("뭐야?"+model.getAttribute("userInfo"));
			//return "forward:http://192.168.0.56:8080/user/addUser";
			return "forward:/user/addUserView.jsp";
		}
		
		
		
	}
	
	
	
	//인가코드를 통해 토큰 받기
	public String getToken(String code) throws Exception {
		
		String url = "https://kauth.kakao.com/oauth/token";
		String grant_type = "authorization_code";
		String client_id = "e63e290b8b7712c5d19b6279f529cacf";
		String redirect_uri = "http://192.168.0.56:8080/user/login/kakao/authorization";
		
		
		/////////////////////////////////////////////////////////REQUEST
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
		//application/x-www-form-urlencoded는 http에서 아래와 같이 n=v형식으로 변환되어 보내짐. 그래서 raw로 바로 아래처럼 보냈음.
		HttpEntity httpEntity01 = new StringEntity("client_id="+client_id+"&redirect_uri="+redirect_uri+"&grant_type="+grant_type+"&code="+code,"utf-8");
		httpPost.setEntity(httpEntity01);
		
		HttpResponse httpResponse = httpClient.execute(httpPost);
		
		/////////////////////////////////////////////////////////RESPONSE
		//==> Response에서 entity데이터를 가져와서 InputStream 생성, jsonObj화
		String serverData = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).readLine();
		JSONObject jsonobj = (JSONObject)JSONValue.parse(serverData);
		
		return jsonobj.get("access_token").toString();
	}
	
	//토큰을 통해 사용자 정보 받아오기
	public Map<String,String> getUserInfo(String token) throws UnsupportedEncodingException, IllegalStateException, IOException{
		
		Map<String, String> map = new HashMap<String,String>();
		
		String url = "https://kapi.kakao.com/v2/user/me";
		
		/////////////////////////////////////////////////////////REQUEST
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		
		httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
		httpPost.setHeader("Authorization", "Bearer "+token);
		HttpResponse httpResponse = httpClient.execute(httpPost);
		
		/////////////////////////////////////////////////////////RESPONSE
		String serverData = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).readLine();
		JSONObject jsonobj = (JSONObject)JSONValue.parse(serverData);
		JSONObject kakao_account = (JSONObject)jsonobj.get("kakao_account");
		JSONObject profile = (JSONObject)kakao_account.get("profile");
		
//		System.out.println("내 닉네임은?!"+profile.get("nickname"));
//		System.out.println(jsonobj);
		
		map.put("id", jsonobj.get("id").toString());
		map.put("nickname", profile.get("nickname").toString());
		
		return map;
	}
		
	
	
	
	
	
}