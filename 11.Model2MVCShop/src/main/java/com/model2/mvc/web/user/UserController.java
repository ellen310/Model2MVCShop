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


//==> ȸ������ Controller
@Controller
@RequestMapping("/user/*")
public class UserController {
	
	///Field
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	//setter Method ���� ����
		
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
		// Model �� View ����
		model.addAttribute("user", user);
		
		return "forward:/user/getUser.jsp";
	}
	

	@RequestMapping( value="updateUser", method=RequestMethod.GET )
	public String updateUser( @RequestParam("userId") String userId , Model model ) throws Exception{

		System.out.println("/user/updateUser : GET");
		//Business Logic
		User user = userService.getUser(userId);
		// Model �� View ����
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
		// Model �� View ����
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
		
		// Business logic ����
		Map<String , Object> map=userService.getUserList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model �� View ����
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/user/listUser.jsp";
	}
	
	
	
	
	
	
	/////////////////////////////////////////
	
/*	//React ��.
	//�ΰ��ڵ� �ޱ� (īī�� �α��ν� / īī�� �α��� ���� ������)
	@RequestMapping( value="login/kakao/authorization", method=RequestMethod.GET )
	public String loginWithKakaoAuthorization(@RequestParam(required = false) String code, HttpSession session, Model model) throws Exception{
		System.out.println("�ΰ��ڵ�: "+code);
		Map<String, String> map = getUserInfo( getToken(code) );
		
		User dbUser = userService.getUser(map.get("id"));
		
		if(dbUser!=null) { //�� ������ īī�� id�� ������ ���� �ִ� ������� �ٷ� �α���
			session.setAttribute("user", dbUser);
			return "redirect:/index.jsp";
		}else {
			model.addAttribute("userInfo", map);
			model.addAttribute("id", map.get("id"));
			System.out.println("����?"+model.getAttribute("userInfo"));
			return "forward:/user/addUserView.jsp";
		}
	}
*/
	
	//React��
	//�ΰ��ڵ� �ޱ� (īī�� �α��ν� / īī�� �α��� ���� ������)
	@RequestMapping( value="login/kakao/authorization", method=RequestMethod.GET )
	public String loginWithKakaoAuthorization(@RequestParam(required = false) String code, HttpSession session, Model model) throws Exception{
		System.out.println("�ΰ��ڵ�: "+code);
		Map<String, String> map = getUserInfo( getToken(code) );
		
		User dbUser = userService.getUser(map.get("id"));
		
		if(dbUser!=null) { //�� ������ īī�� id�� ������ ���� �ִ� ������� �ٷ� �α���
			session.setAttribute("user", dbUser);
			//return "redirect:http://localhost:3000/logon/"+map.get("id")+"/user"; //url�Ķ���ͷ� user������ ���� ����������. īī�� ���� ������ role�� ������ user�� ����.
			return "redirect:http://192.168.0.56:3000/logon/"+map.get("id")+"/user"; //url�Ķ���ͷ� user������ ���� ����������. īī�� ���� ������ role�� ������ user�� ����.
		}else {
			model.addAttribute("userInfo", map);
			model.addAttribute("id", map.get("id"));
			System.out.println("����?"+model.getAttribute("userInfo"));
			//return "forward:http://192.168.0.56:8080/user/addUser";
			return "forward:/user/addUserView.jsp";
		}
		
		
		
	}
	
	
	
	//�ΰ��ڵ带 ���� ��ū �ޱ�
	public String getToken(String code) throws Exception {
		
		String url = "https://kauth.kakao.com/oauth/token";
		String grant_type = "authorization_code";
		String client_id = "e63e290b8b7712c5d19b6279f529cacf";
		String redirect_uri = "http://192.168.0.56:8080/user/login/kakao/authorization";
		
		
		/////////////////////////////////////////////////////////REQUEST
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
		//application/x-www-form-urlencoded�� http���� �Ʒ��� ���� n=v�������� ��ȯ�Ǿ� ������. �׷��� raw�� �ٷ� �Ʒ�ó�� ������.
		HttpEntity httpEntity01 = new StringEntity("client_id="+client_id+"&redirect_uri="+redirect_uri+"&grant_type="+grant_type+"&code="+code,"utf-8");
		httpPost.setEntity(httpEntity01);
		
		HttpResponse httpResponse = httpClient.execute(httpPost);
		
		/////////////////////////////////////////////////////////RESPONSE
		//==> Response���� entity�����͸� �����ͼ� InputStream ����, jsonObjȭ
		String serverData = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).readLine();
		JSONObject jsonobj = (JSONObject)JSONValue.parse(serverData);
		
		return jsonobj.get("access_token").toString();
	}
	
	//��ū�� ���� ����� ���� �޾ƿ���
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
		
//		System.out.println("�� �г�����?!"+profile.get("nickname"));
//		System.out.println(jsonobj);
		
		map.put("id", jsonobj.get("id").toString());
		map.put("nickname", profile.get("nickname").toString());
		
		return map;
	}
		
	
	
	
	
	
}