package test.web;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/applicationContext.xml"})
public class ProjectDirWebTest {
	//@Autowired //2015.8.26,采用配置的bean有错误，不能转换Integer，因此注释
	private RestTemplate restTemplate;
	@BeforeClass
	public static void beforeclass(){
		
	}
	@Before
	public void before(){
		restTemplate = new RestTemplate(new CommonsClientHttpRequestFactory()); 
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("userName", "admin");
		map.add("userPwd", "1");
		//----登录
		String mv = restTemplate.postForObject("http://localhost:8080/MedicineMebmer/slogin", map , String.class);
	}
	@Test
	public void main(){
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("proid", 67);
		String mv = restTemplate.postForObject(
				"http://localhost:8080/MedicineMebmer/xmml/main", map , String.class);
		assertEquals(true,mv.contains("horizontalMenu"));
	}
	@Test 
	public void list() throws JsonParseException, JsonMappingException, IOException{
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("proid", 67);
		map.add("searchName", "a");
		String mv = restTemplate.postForObject(
				"http://localhost:8080/MedicineMebmer/xmml/list", map , String.class);
		ObjectMapper om = new ObjectMapper();
		Map<String,Object> result = om.readValue(mv, new TypeReference<Map<String,Object>>(){});
		Integer code = (Integer)result.get("code");
		assertEquals(200,code.intValue());
	}
}
