package test.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.searainbow.membercenter.dao.ProjectDirMapper;
import com.searainbow.membercenter.model.ProjectDir;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
//@SpringApplicationContext("classpath:applicationContext*.xml")
public class ProjectDirMapperTest {
	@Autowired
	private ProjectDirMapper mapper;
	
	private List<ProjectDir> list;
	@Before
	public void before(){
		
		this.list = new ArrayList<ProjectDir>();
		for(int i=0;i<10;i++){
			ProjectDir obj2 = new ProjectDir();
			obj2.setDrugName("阿莫西林分三片"+String.valueOf(i));
			obj2.setDosage("片剂");
			obj2.setDrugCode("x01"+String.valueOf(i));
			obj2.setDrugPy("amxl");
			obj2.setSpec("0.5mg");
			obj2.setType("西药");
			obj2.setProId(67);
			this.list.add(obj2);
		}
		System.out.println("----------------------list size = "+list.size());
	}
	@Test
	@Transactional
	public void testInsertBatch() throws Exception{

		System.out.println("----------------------");
		System.out.println("ok".hashCode());
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", this.list);
		int row = mapper.insertBatch(map);
		boolean flag = row == list.size();
		assertEquals(true,flag);
	}
	@Test
	public void testFindDirAndProduct() throws Exception{
		List list = mapper.findDirAndProduct(67);
		
		assertEquals(true,list.size()>0);
	}
}
