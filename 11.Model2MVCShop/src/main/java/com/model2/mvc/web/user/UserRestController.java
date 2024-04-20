package com.model2.mvc.web.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserService;




//==> 회원관리 RestController
@RestController
@RequestMapping("/user/*")
public class UserRestController {
	
	///Field
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	//setter Method 구현 않음
		
	public UserRestController(){
		System.out.println(this.getClass());
	}
	
	@RequestMapping( value="json/getUser/{userId}", method=RequestMethod.GET )
	public User getUser( @PathVariable String userId ) throws Exception{
		
		System.out.println("/user/json/getUser : GET");
		
		//Business Logic
		return userService.getUser(userId);
	}

//	@RequestMapping( value="json/login", method=RequestMethod.POST )
//	public User login(	@RequestBody User user,
//									HttpSession session ) throws Exception{
//	
//		System.out.println("/user/json/login : POST");
//		//Business Logic
//		System.out.println("::"+user);
//		User dbUser=userService.getUser(user.getUserId());
//		
//		if( user.getPassword().equals(dbUser.getPassword())){
//			session.setAttribute("user", dbUser);
//		}
//		
//		return dbUser;
//	}
	
//////////////////////////////////////////////////////////////////////////////////////
	
	//React
	
	//일반 로그인
	@RequestMapping( value="json/login", method=RequestMethod.POST )
	public User login(@RequestBody User user) throws Exception{
		
		System.out.println("/user/login : POST");
		
		//Business Logic
		User dbUser=userService.getUser(user.getUserId());
		
		if( user.getPassword().equals(dbUser.getPassword())){
			//session.setAttribute("user", dbUser);
			user.setActive(true);
		}
		
		return user;
	}
	
	
	
	/////////////////////////////////////////
	//카카오 로그인

	//인가코드 받기 (카카오 로그인시 / 카카오 로그인 세션 있을시)
	@RequestMapping( value="login/kakao/authorization", method=RequestMethod.GET )
	public Map loginWithKakaoAuthorization(@RequestParam(required = false) String code, HttpSession session, Model model) throws Exception{
		System.out.println("인가코드: "+code);
		Map<String, String> map = getUserInfo( getToken(code) );
		
		User dbUser = userService.getUser(map.get("id"));
		
		if(dbUser!=null) { //이 유저가 카카오 id로 가입한 적이 있는 유저라면 바로 로그인
			session.setAttribute("user", dbUser);
			map.put("active", "true");
			return map;
		}else {
			//model.addAttribute("userInfo", map);
			//model.addAttribute("id", map.get("id"));
			System.out.println("userInfo: "+model.getAttribute("userInfo"));
			map.put("active", "false");
			return  map;//"forward:/user/addUserView";
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
		
		map.put("id", jsonobj.get("id").toString());
		map.put("nickname", profile.get("nickname").toString());
		
		return map;
	}
	
	
}