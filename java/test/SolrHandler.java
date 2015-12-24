package test;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class SolrHandler {

	public static void main(String[] args) {
		
		Date d = new Date(0);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sf.format(d));
//		String a = "http://member.emedchina.cn:80/MedicineMebmer/upload/image/1442995642418.jpg?token=20151012164746636-Z0002689833344844719";
//		a = a.substring(0, a.indexOf("?token"));
//		System.out.println(a.hashCode());
		// try {
		// HttpClient client = new DefaultHttpClient();
		// HttpGet get = new
		// HttpGet("http://localhost:8080/MedicineMebmer/app/login?userName=111&userPwd=111");
		// HttpResponse resp = client.execute(get);
		// InputStream is = resp.getEntity().getContent();
		// StringBuffer sb = new StringBuffer();
		// byte[] b = new byte[1024];
		// int count = 0;
		// while((count = is.read(b))!=-1){
		// sb.append(new String(b,0,count));
		// }
		// String result = sb.toString();
		// ObjectMapper objmapper = new ObjectMapper();
		// JsonNode resultNode = objmapper.readTree(result);
		// System.out.println("msg========>"+resultNode.get("msg").asText());;
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// 将数据库中数据写入solr服务器
//		SolrHandler sh = new SolrHandler();
//		try {
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//			String url = "jdbc:oracle:thin:@172.25.13.180:1521:drugmember";
//			String userName = "drugmember_dev";
//			String password = "123456";
//			Connection conn = DriverManager.getConnection(url, userName, password);
//
//			try {
//				if (conn == null) {
//					conn = DriverManager.getConnection(url, userName, password);
//				}
//				String sql = "select * from view_cat_product t where t.orgname like '%欧洲波兰波尔法%' ";
//				PreparedStatement pst = conn.prepareStatement(sql);
//				ResultSet rs = pst.executeQuery();
//				List<CatProduct> aa = new ArrayList<CatProduct>();
//				while (rs.next()) {
//
//					CatProduct cat = new CatProduct();
//					cat.setDoseageName(rs.getString("doseageName"));
//					cat.setDrugName(rs.getString("drugName"));
//					cat.setDrugSpec(rs.getString("drugSpec"));
//					cat.setDrugStand(rs.getString("drugStand"));
//					cat.setLastUpdateDate(rs.getDate("last_Update_Date"));
//					cat.setMetricName(rs.getString("metricName"));
//					cat.setOrgName(rs.getString("orgName"));
//					cat.setProductID(rs.getString("productID"));
//					cat.setTradeName(rs.getString("tradeName"));
//					cat.setWrapName(rs.getString("wrapName"));
//					aa.add(cat);
//				}
//				System.out.println("----------->共找到:"+aa.size());
//				sh.sendtoSolrEnterprise(aa, CatProduct.class, Constant.SOLR_PRODUCT_TABLE_NAME);
//			} catch (Exception e) {
//				e.printStackTrace();
//				try {
//					conn.close();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}
//			} finally {
//				try {
//					conn.close();
//				} catch (SQLException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	public String getSolrMessage(String q, Integer start, Integer rows) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet post = new HttpGet("http://172.25.3.179:7263/SolrSearch/product/select?q=" + q + "&start=" + start + "&rows=" + rows + "&wt=json&indent=true&_=1439368724162");
		HttpParams params = new BasicHttpParams();
		params.setParameter("q", "name:\"*\"");
		post.setParams(params);
		post.setHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
		HttpResponse resp = client.execute(post);
		StatusLine state = resp.getStatusLine();
		System.out.println(state.getReasonPhrase());
		InputStream is = resp.getEntity().getContent();
		byte[] b = new byte[1024];
		int count = 0;
		while (is.read(b) != -1) {
			System.out.println(new String(b, 0, count));
		}
		return null;

	}

	public void sendtoSolrEnterprise(List<? extends Object> params, Class clazz, String className) throws Exception {

		Field[] fs = clazz.getDeclaredFields();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		ObjectMapper om = new ObjectMapper();
		om.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED,true);
		for (Object param : params) {
			
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://localhost:8080/SolrSearch/" + className + "/update?wt=json");
			try {
				StringBuffer sb = new StringBuffer();
				sb.append("{\"add\":{ \"doc\":");
				sb.append(om.valueToTree(param));
//				for (Field field : fs) {
//					String fname = field.getName();
//					if ("state".equals(fname) || "token".equals(fname) || "roleType".equals(fname) || "productNum".equals(fname)) {
//						continue;
//					}
//					Method method = null;
//					try {
//						method = clazz.getMethod("get" + fname.substring(0, 1).toUpperCase() + fname.substring(1), null);
//						Object value = "";
//						if (field.getGenericType().toString().equals("class java.util.Date")) {
//							Date d = (Date) method.invoke(param, null);
//							value = sf.format(d);
//						} else {
//							value = method.invoke(param, null);
//						}
//						sb.append("\"" + fname + "\":\"" + value + "\",");
//					} catch (Exception e) {
//						e.printStackTrace();
//						System.out.println(clazz.getSimpleName() + "  字段 :  " + fname + " 没有合法的getter方法");
//					}
//
//				}
				sb.append(",\"boost\":1.0,\"overwrite\":true,\"commitWithin\":1000}}");

				String data = sb.toString();
				System.out.println(data);
//				HttpEntity entity = new StringEntity(data, ContentType.APPLICATION_JSON);
//				post.setEntity(entity);
//				HttpResponse resp = client.execute(post);
//				StatusLine state = resp.getStatusLine();
//				System.out.println(state.getStatusCode());
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void sendtoSolr(List<List<String>> params) throws Exception {

		for (List<String> param : params) {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://172.25.3.179:7263/SolrSearch/product/update?wt=json");
			try {
				String data = "{\"add\":{ \"doc\":{\"productId\":\"" + param.get(0) + "\",\"tradename\":\"" + param.get(1) + "\",\"drugname\":\"" + param.get(2)
						+ "\",\"drugspec\":\"" + param.get(3) + "\",\"drugstand\":\"" + param.get(4) + "\",\"doseagename\":\"" + param.get(5) + "\",\"metricname\":\""
						+ param.get(6) + "\",\"wrapname\":\"" + param.get(7) + "\",\"orgname\":\"" + param.get(8) + "\"},\"boost\":1.0,\"overwrite\":true,\"commitWithin\":1000}}";
				System.out.println(data);
				HttpEntity entity = new StringEntity(data, ContentType.APPLICATION_JSON);
				post.setEntity(entity);
				HttpResponse resp = client.execute(post);
				StatusLine state = resp.getStatusLine();
				System.out.println(state.getStatusCode());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
