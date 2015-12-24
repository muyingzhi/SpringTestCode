package test.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.searainbow.membercenter.service.IRegionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class RegionServiceTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private IRegionService service;
	@Test
	public void testGetDatas() throws Exception{
		
		System.out.println(service.getTreeDatas("FR20T0000010000000050000"));
		assertTrue(true);
	}
	
}
