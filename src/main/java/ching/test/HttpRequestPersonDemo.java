package ching.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import ching.utils.EncryptionHMAC;
import net.sf.json.JSONObject;

public class HttpRequestPersonDemo {

	private static String encoding = "UTF-8";
	private static String algorithm = "HmacSHA256";
	private static String projectId = "1111563517";
	private static String projectSecret = "95439b0863c241c63a861b87d1e647b7";

	public static void main(String[] args) {
		try {
			//个人实名认证-请求
			requestPerson();

			 String code = "368751";// 接收到的短信验证码
			 String serviceId = "263b872d-c485-4ec2-af93-bcdc291e9ca2";
			//个人实名认证-验证
//			verifyPerson(code,serviceId);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/***
	 * 个人实名认证-请求
	 * @throws Exception
	 */
	public static void requestPerson() throws Exception {
		String signature = null;
		String resultJSON = null;
		String urlPath = null;
		// 设置个人银行四要素请求参数
		String personApply = setPersonJSONStr();
		signature = EncryptionHMAC.getHMACHexString(personApply, projectSecret, algorithm, encoding);
		System.out.println("个人实名认证-请求开始...");
		// 银行四要素认证请求地址（测试环境）
		urlPath = "http://smlrealname.tsign.cn:8080/realname/rest/external/person/bankauth/infoValid";
		//模拟请求
		resultJSON = sendPost(urlPath, signature, personApply);
		System.out.println("个人实名认证-请求完成：" + resultJSON);
		/* 至此，已完成个人银行四要素实名认证请求 */
	}

	/***
	 * 个人实名认证-验证
	 * @param code
	 * @param serviceId
	 * @throws Exception
	 */
	public static void verifyPerson(String code, String serviceId) throws Exception {
		String signature = null;
		String resultJSON = null;
		String urlPath = null;
		System.out.println("个人实名认证-验证开始...");
		// 银行四要素认证请求地址（测试环境）
		urlPath = "http://smlrealname.tsign.cn:8080/realname/rest/external/person/bankauth/codeValid";
		// 实名认证请求服务ID,请求成功时返回
		String personVerify = setVerifyPersonJSONStr(code, serviceId);
		signature = EncryptionHMAC.getHMACHexString(personVerify, projectSecret, algorithm, encoding);
		resultJSON = sendPost(urlPath, signature, personVerify);
		System.out.println("个人实名认证-验证完成：" + resultJSON);
		/* 至此，已完成个人银行四要素实名认证的验证 */
	}

	/***
	 * 设置个人银行四要素请求参数
	 *
	 * @return
	 */
	public static String setPersonJSONStr() {
		JSONObject obj = new JSONObject();
		obj.put("mobile", "18518295734");
		obj.put("name", "宁必武");
//		obj.put("cardno", "6259061178554793");
//		obj.put("idno", "342626198910032697");
		return obj.toString();
	}

	/***
	 * 设置个人银行四要素验证参数
	 *
	 * @param code
	 *            短信验证码
	 * @param serviceId
	 *            实名认证请求服务ID
	 * @return
	 */
	public static String setVerifyPersonJSONStr(String code, String serviceId) {
		JSONObject obj = new JSONObject();
		obj.put("code", code);
		obj.put("serviceId", serviceId);
		return obj.toString();
	}

	/***
	 * 设置Headers报头信息
	 *
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getHeaders(String signature, String jsonStr) throws Exception {
		Map<String, String> headersMap = new LinkedHashMap<String, String>();
		headersMap.put("X-timevale-project-id", projectId);
		headersMap.put("X-timevale-signature", signature); // 请求参数和projectSecret参数通过HmacSHA256加密的16进制字符串
		headersMap.put("signature-algorithm", algorithm);
		headersMap.put("Content-Type", "application/json");
		headersMap.put("Charset", encoding);
		return headersMap;
	}

	/***
	 * 模拟发送POST请求
	 *
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public static String sendPost(String urlPath, String signature, String jsonStr) throws Exception {
		String result = null;
		// 建立连接
		URL url = new URL(urlPath);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		// 设置Headers参数
		httpConn.setDoOutput(true); // 需要输出
		httpConn.setDoInput(true); // 需要输入
		httpConn.setUseCaches(false); // 不允许缓存
		httpConn.setRequestMethod("POST"); // 设置POST方式连接
		// 设置Headers属性
		for (Entry<String, String> entry : getHeaders(signature, jsonStr).entrySet()) {
			httpConn.setRequestProperty(entry.getKey(), entry.getValue());
		}

		// 连接会话
		httpConn.connect();

		// 建立输入流，向指向的URL传入参数
		DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
		// 设置请求参数
		dos.write(jsonStr.toString().getBytes("UTF-8"));
		System.out.println(jsonStr);
		dos.flush();
		dos.close();
		// 获得响应状态
		int resultCode = httpConn.getResponseCode();
		if (HttpURLConnection.HTTP_OK == resultCode) {
			StringBuffer sb = new StringBuffer();
			String readLine = new String();
			BufferedReader responseReader = new BufferedReader(
					new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			while ((readLine = responseReader.readLine()) != null) {
				sb.append(readLine);
			}
			// System.out.println(httpConn.get.getRequestURI());
			responseReader.close();
			result = sb.toString();
		}
		return result;
	}

	/***
	 * 模拟发送POST请求-验证个人实名信息
	 *
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public static String sendVPost(String urlPath, String signature, String jsonStr) throws Exception {
		String result = null;
		// 建立连接
		URL url = new URL(urlPath);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		// 设置Headers参数
		httpConn.setDoOutput(true); // 需要输出
		httpConn.setDoInput(true); // 需要输入
		httpConn.setUseCaches(false); // 不允许缓存
		httpConn.setRequestMethod("POST"); // 设置POST方式连接
		// 设置Headers属性
		for (Entry<String, String> entry : getHeaders(signature, jsonStr).entrySet()) {
			httpConn.setRequestProperty(entry.getKey(), entry.getValue());
		}

		// 连接会话
		httpConn.connect();

		// 建立输入流，向指向的URL传入参数
		DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
		// 设置请求参数
		dos.write(jsonStr.toString().getBytes("UTF-8"));
		System.out.println(jsonStr);
		dos.flush();
		dos.close();
		// 获得响应状态
		int resultCode = httpConn.getResponseCode();
		if (HttpURLConnection.HTTP_OK == resultCode) {
			StringBuffer sb = new StringBuffer();
			String readLine = new String();
			BufferedReader responseReader = new BufferedReader(
					new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			while ((readLine = responseReader.readLine()) != null) {
				sb.append(readLine);
			}
			// System.out.println(httpConn.get.getRequestURI());
			responseReader.close();
			result = sb.toString();
		}
		return result;
	}
}
