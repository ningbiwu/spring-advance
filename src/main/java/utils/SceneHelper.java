package utils;

import bean.CertificateBean;
import bean.IdsBean;
import constant.SceneConfig;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/***
 * @Description: 场景式存证_辅助类
 * @Team: 公有云技术支持小组
 * @Author: 天云小生
 * @Date: 2018年01月11日
 */
public class SceneHelper {

	/***
	 * 创建证据链:将已创建的存证环节串联起来,形成证据链 (此处以原文存证和签署记录ID为例,实际对接时请替换成动态传值)
	 *
	 * @param sceneTemplateId
	 *            业务凭证（名称）ID
	 */
	public static String createChainOfEvidence(String apiUrl, String sceneTemplateId) {
		System.out.println("请求API接口地址:" + apiUrl);
		// 请求参数-JSON字符串
		JSONObject param_json = new JSONObject();
		param_json.put("sceneName", "想要显示的场景式存证名称");// 如 XXX房屋租赁合同签署、XXX借款合同签署
		param_json.put("sceneTemplateId", sceneTemplateId);
		param_json.put("linkIds", null);
		System.out.println("请求参数:" + param_json);
		// 请求签名值
		String signature = DigestHelper.getSignature(param_json.toString(), SceneConfig.PROJECT_SECRET,
				SceneConfig.ALGORITHM, SceneConfig.ENCODING);
				// System.out.println("请求签署值 = " + signature);

		// HTTP请求内容类型
		String ContentType = "application/json";

		// 设置HTTP请求头信息
		LinkedHashMap<String, String> headers = HttpHelper.getPOSTHeaders(SceneConfig.PROJECT_ID, signature,
				SceneConfig.ALGORITHM, ContentType, SceneConfig.ENCODING);
		// 向指定URL发送POST方法的请求
		JSONObject result = HttpHelper.sendPOST(apiUrl, param_json.toString(), headers, SceneConfig.ENCODING);
		// 场景式存证编号（证据链编号）
		String C_Evid = null;
		int errCode = result.getInt("errCode");
		if (0 == errCode) {
			// 场景式存证编号（证据链编号）
			C_Evid = result.getString("evid");
			System.out.println("场景式存证编号（证据链编号） = " + C_Evid);
		} else {
			throw new RuntimeException(MessageFormat.format("创建证据链,获取场景式存证编号（证据链编号）失败,失败信息：errCode = {0}, msg = {1}",
					errCode, result.get("msg")));
		}
		return C_Evid;
	}

	/***
	 * 追加证据:向已存在的证据链中追加证据点
	 *
	 * @param C_Evid
	 *            场景式存证编号（证据链编号）
	 * @param H_Evid
	 *            证据点编号
	 * @param isHaveSignServiceId
	 *            是否使用e签宝电子签名
	 */
	public static void appendEvidence(String apiUrl, String C_Evid, String H_Evid, boolean isHaveSignServiceId) {
		System.out.println("请求API接口地址:" + apiUrl);
		// 创建原文存证（基础版或高级版）证据点时返回的evid(可关联多个证据点ID)
		IdsBean ids0 = new IdsBean();
		ids0.setType("0");
		// 创建存证环节时返回的证据点编号(请根据实际情况进行动态传值)
		ids0.setValue(H_Evid);

		// 电子签署SDK中签署接口返回的签署记录ID(可关联多个签署记录ID)
		// 请将与本签署文档有关的签署记录ID都进行关联,否则存证证明页面将无法完整显示签署记录
		IdsBean signServiceId_1 = new IdsBean();
		signServiceId_1.setType("1");
		// 电子签署SDK中签署接口返回的签署记录ID(SignServiceId)(请根据实际情况进行动态传值)
		signServiceId_1.setValue("953474322771513346");

		IdsBean signServiceId_2 = new IdsBean();
		signServiceId_2.setType("1");
		// 电子签署SDK中签署接口返回的签署记录ID(SignServiceId)(请根据实际情况进行动态传值)
		signServiceId_2.setValue("953474984796262406");

		// 时间戳服务接口中返回的时间戳数据记录ID(如果未使用e签宝的时间戳服务该参数可以不传递)
		// (可关联多个时间戳数据记录ID)
		// IdsBean ids2 = new IdsBean();
		// ids2.setType("2");
		// 时间戳服务接口返回的时间戳数据记录ID(请根据实际情况进行动态传值,如未使用该服务可以不传值)
		// ids2.setValue("129ab3d5-d4f9-4880-9f70-0c6767af6003");

		// 实名认证服务接口中返回的实名认证请求ID(如果未使用e签宝的实名认证服务该参数可以不传递)
		// (可关联多个实名认证请求ID)
		// IdsBean ids3 = new IdsBean();
		// ids3.setType("3");
		// 实名认证服务接口中返回的实名认证请求ID(请根据实际情况进行动态传值,如未使用该服务可以不传值)
		// ids3.setValue("cbf52ea8-8055-4fbd-b362-92949ca294cb");

		// 存证环节ID列表-证据链(根据实际情况进行填写)
		ArrayList<String> linkIds = new ArrayList<String>();
		linkIds.add(JSONObject.fromObject(ids0).toString());

		// 是否使用e签宝电子签名
		if(isHaveSignServiceId){
			linkIds.add(JSONObject.fromObject(signServiceId_1).toString());
			linkIds.add(JSONObject.fromObject(signServiceId_2).toString());
		}
		// linkIds.add(JSONObject.fromObject(ids2).toString());
		// linkIds.add(JSONObject.fromObject(ids3).toString());

		// 请求参数-JSON字符串
		JSONObject param_json = new JSONObject();
		param_json.put("evid", C_Evid);
		param_json.put("linkIds", JSONArray.fromObject(linkIds));
		System.out.println("请求参数:" + param_json);
		// 请求签名值
		String signature = DigestHelper.getSignature(param_json.toString(), SceneConfig.PROJECT_SECRET,
				SceneConfig.ALGORITHM, SceneConfig.ENCODING);
				// System.out.println("请求签署值 = " + signature);

		// HTTP请求内容类型
		String ContentType = "application/json";

		// 设置HTTP请求头信息
		LinkedHashMap<String, String> headers = HttpHelper.getPOSTHeaders(SceneConfig.PROJECT_ID, signature,
				SceneConfig.ALGORITHM, ContentType, SceneConfig.ENCODING);
		// 向指定URL发送POST方法的请求
		JSONObject result = HttpHelper.sendPOST(apiUrl, param_json.toString(), headers, SceneConfig.ENCODING);

		int errCode = result.getInt("errCode");
		if (0 == errCode) {
			// 追加证据是否成功标识
			boolean isAppendSuccess = result.getBoolean("success");
			System.out.println("向已存在的证据链中追加证据点是否成功: " + isAppendSuccess);
		} else {
			throw new RuntimeException(
					MessageFormat.format("向已存在的证据链中追加证据点失败,失败信息：errCode = {0}, msg = {1}", errCode, result.get("msg")));
		}
	}

	/***
	 * 创建原文存证基础版证据点, 原文基础版存证成功后将原文同时推送到e签宝服务端和司法鉴定中心,
	 * 不会推送到公证处(请询问e签宝对接人员确认贵司所购买的存证类型)
	 *
	 * @param filePath
	 * @param segmentTempletId
	 *            业务凭证中某一证据点名称ID
	 */
	public static JSONObject createSegmentOriginal_Standard(String apiUrl, String filePath, String segmentTempletId) {
		System.out.println("请求API接口地址:" + apiUrl);
		// 设置业务数据(与本文档有关的签署人信息)
		JSONObject segmentDataJSON = new JSONObject();
		segmentDataJSON.put("realName_1", "赵明丽");
		segmentDataJSON.put("userName_1", "zhaomingli@贵司系统内的登录账号");
		segmentDataJSON.put("realName_2", "金明丽");
		segmentDataJSON.put("userName_2", "jinmingli@贵司系统内的登录账号");
		String segmentData = segmentDataJSON.toString();

		Map<String, String> fileInfo = FileHelper.getFileInfo(filePath);

		JSONObject contentJSON = new JSONObject();
		// 待保全文档名称（文件名中不允许含有? * : " < > \ / | [ ] 【】）
		contentJSON.put("contentDescription", fileInfo.get("FileName"));
		// 待保全文档大小，单位：字节
		contentJSON.put("contentLength", fileInfo.get("FileLength"));
		// 待保全文档内容字节流MD5的Base64编码值
		contentJSON.put("contentBase64Md5", DigestHelper.getContentMD5(filePath));

		JSONObject original_Std_JSON = new JSONObject();
		original_Std_JSON.put("segmentTempletId", segmentTempletId);
		// segmentData为Json形式的字符串,并非Json格式数据,如:"segmentData":"{\"name\":\"张三\",\"address\":\"位于浙江省的采购方\"}"
		original_Std_JSON.put("segmentData", "\"" + segmentData + "\"");
		original_Std_JSON.put("content", contentJSON);

		System.out.println("请求参数:" + original_Std_JSON.toString());

		// 请求签名值
		String signature = DigestHelper.getSignature(original_Std_JSON.toString(), SceneConfig.PROJECT_SECRET,
				SceneConfig.ALGORITHM, SceneConfig.ENCODING);

		// HTTP请求内容类型
		String ContentType = "application/json";

		// 设置HTTP请求头信息
		LinkedHashMap<String, String> headers = HttpHelper.getPOSTHeaders(SceneConfig.PROJECT_ID, signature,
				SceneConfig.ALGORITHM, ContentType, SceneConfig.ENCODING);

		// 向指定URL发送POST方法的请求
		JSONObject result = HttpHelper.sendPOST(apiUrl, original_Std_JSON.toString(), headers, SceneConfig.ENCODING);

		int errCode = result.getInt("errCode");
		if (0 != errCode) {
			throw new RuntimeException(MessageFormat.format("创建原文存证（基础版）证据点ID（编号）失败,失败信息：errCode = {0}, msg = {1}",
					errCode, result.get("msg")));
		}
		return result;
	}

	/***
	 * 创建原文存证高级版证据点 原文高级版存证成功后将原文同时推送到e签宝服务端、司法鉴定中心和公证处(请询问e签宝对接人员确认贵司所购买的存证类型)
	 *
	 * @param filePath
	 * @param segmentTempletId
	 *            业务凭证中某一证据点名称ID
	 */
	public static JSONObject createSegmentOriginal_Advanced(String apiUrl, String filePath, String segmentTempletId) {
		System.out.println("请求API接口地址:" + apiUrl);
		// 设置业务数据(与本文档有关的签署人信息)
		JSONObject segmentDataJSON = new JSONObject();
		segmentDataJSON.put("realName_1", "赵明丽");
		segmentDataJSON.put("userName_1", "zhaomingli@贵司系统内的登录账号");
		segmentDataJSON.put("realName_2", "金明丽");
		segmentDataJSON.put("userName_2", "jinmingli@贵司系统内的登录账号");
		String segmentData = segmentDataJSON.toString();

		Map<String, String> fileInfo = FileHelper.getFileInfo(filePath);

		JSONObject contentJSON = new JSONObject();
		// 待保全文档名称（文件名中不允许含有? * : " < > \ / | [ ] 【】）
		contentJSON.put("contentDescription", fileInfo.get("FileName"));
		// 待保全文档大小，单位：字节
		contentJSON.put("contentLength", fileInfo.get("FileLength"));
		// 待保全文档内容字节流MD5的Base64编码值
		contentJSON.put("contentBase64Md5", DigestHelper.getContentMD5(filePath));

		JSONObject original_Std_JSON = new JSONObject();
		original_Std_JSON.put("segmentTempletId", segmentTempletId);
		// segmentData为Json形式的字符串,并非Json格式数据,如:"segmentData":"{\"name\":\"张三\",\"address\":\"位于浙江省的采购方\"}"
		original_Std_JSON.put("segmentData", "\"" + segmentData + "\"");
		original_Std_JSON.put("content", contentJSON);

		System.out.println("请求参数:" + original_Std_JSON.toString());

		// 请求签名值
		String signature = DigestHelper.getSignature(original_Std_JSON.toString(), SceneConfig.PROJECT_SECRET,
				SceneConfig.ALGORITHM, SceneConfig.ENCODING);

		// HTTP请求内容类型
		String ContentType = "application/json";

		// 设置HTTP请求头信息
		LinkedHashMap<String, String> headers = HttpHelper.getPOSTHeaders(SceneConfig.PROJECT_ID, signature,
				SceneConfig.ALGORITHM, ContentType, SceneConfig.ENCODING);

		// 向指定URL发送POST方法的请求
		JSONObject result = HttpHelper.sendPOST(apiUrl, original_Std_JSON.toString(), headers, SceneConfig.ENCODING);

		int errCode = result.getInt("errCode");
		if (0 != errCode) {
			throw new RuntimeException(MessageFormat.format("创建原文存证（高级版）证据点ID（编号）失败,失败信息：errCode = {0}, msg = {1}",
					errCode, result.get("msg")));
		}
		return result;
	}

	/***
	 * 创建摘要存证证据点,
	 * 摘要版存证不会将原文进行推送,仅是将原文的摘要(SHA256)推送到e签宝服务端和司法鉴定中心,文件摘要(SHA256)不支持存放到公证处(
	 * 请询问e签宝对接人员确认贵司所购买的存证类型)
	 *
	 * @param filePath
	 * @param segmentTempletId
	 *            业务凭证中某一证据点名称ID
	 */
	public static JSONObject createSegmentOriginal_Digest(String apiUrl, String filePath, String segmentTempletId) {
		System.out.println("请求API接口地址:" + apiUrl);
		// 设置业务数据(与本文档有关的签署人信息)
		JSONObject segmentDataJSON = new JSONObject();
		segmentDataJSON.put("realName_1", "赵明丽");
		segmentDataJSON.put("userName_1", "zhaomingli@贵司系统内的登录账号");
		segmentDataJSON.put("realName_2", "金明丽");
		segmentDataJSON.put("userName_2", "jinmingli@贵司系统内的登录账号");
		String segmentData = segmentDataJSON.toString();

		Map<String, String> fileInfo = FileHelper.getFileInfo(filePath);
		// 原文SHA256摘要
		String fileDigestSHA256 = DigestHelper.getFileSHA256(filePath);

		// System.out.println("原文SHA256摘要 = " + fileDigestSHA256);

		JSONObject contentJSON = new JSONObject();
		// 待保全文档名称（文件名中不允许含有? * : " < > \ / | [ ] 【】）
		contentJSON.put("contentDescription", fileInfo.get("FileName"));
		// 原文SHA256摘要
		contentJSON.put("contentDigest", fileDigestSHA256);

		JSONObject original_Digest_JSON = new JSONObject();
		original_Digest_JSON.put("segmentTempletId", segmentTempletId);
		// segmentData为Json形式的字符串,并非Json格式数据,如:"segmentData":"{\"name\":\"张三\",\"address\":\"位于浙江省的采购方\"}"
		original_Digest_JSON.put("segmentData", "\"" + segmentData + "\"");
		original_Digest_JSON.put("content", contentJSON);

		System.out.println("请求参数:" + original_Digest_JSON.toString());

		// 请求签名值
		String signature = DigestHelper.getSignature(original_Digest_JSON.toString(), SceneConfig.PROJECT_SECRET,
				SceneConfig.ALGORITHM, SceneConfig.ENCODING);

		// HTTP请求内容类型
		String ContentType = "application/json";

		// 设置HTTP请求头信息
		LinkedHashMap<String, String> headers = HttpHelper.getPOSTHeaders(SceneConfig.PROJECT_ID, signature,
				SceneConfig.ALGORITHM, ContentType, SceneConfig.ENCODING);

		// 向指定URL发送POST方法的请求
		JSONObject result = HttpHelper.sendPOST(apiUrl, original_Digest_JSON.toString(), headers, SceneConfig.ENCODING);

		int errCode = result.getInt("errCode");
		if (0 != errCode) {
			throw new RuntimeException(MessageFormat.format("创建摘要版证据点ID（编号）失败,失败信息：errCode = {0}, msg = {1}",
					errCode, result.get("msg")));
		}
		return result;
	}

	/***
	 * 待存证文档上传
	 *
	 * @param H_Evid
	 *            证据点编号
	 * @param fileUploadUrl
	 * @param filePath
	 * @return
	 */
	public static void uploadOriginalDocumen(String H_Evid, String fileUploadUrl, String filePath) {

		// ContentMD5 内容字节流MD5的Base64编码值
		String ContentMD5 = DigestHelper.getContentMD5(filePath);
		// HTTP请求内容类型
		String ContentType = "application/octet-stream";
		// 设置HTTP请求头信息
		LinkedHashMap<String, String> headers = HttpHelper.getPUTHeaders(ContentMD5, ContentType, SceneConfig.ENCODING);
		// 向指定URL发送PUT方法的请求
		int uploadSuccessCode = HttpHelper.sendPUT(H_Evid, fileUploadUrl, filePath, headers);
		if (200 == uploadSuccessCode) {
			System.out.println("存证环节编号（证据点编号）= " + H_Evid + " 的待存证文档上传成功！");
		} else {
			throw new RuntimeException("存证环节编号（证据点编号）= " + H_Evid + " 的待存证文档上传失败！");
		}
	}

	/***
	 * 场景式存证编号关联到指定用户(以便指定用户日后可以顺利出证)
	 *
	 * @param C_Evid
	 *            场景式存证编号（证据链编号）
	 * @return
	 */
	public static void relateSceneEvIdWithUser(String apiUrl, String C_Evid) {
		System.out.println("请求API接口地址:" + apiUrl);
		// 用户证件信息
		List<CertificateBean> certificates = new ArrayList<CertificateBean>();

		// 接口调用方的个人类客户证件信息(请根据实际情况进行替换)
		// 与该场景式存证编号有关的客户证件信息
		CertificateBean personBean1 = new CertificateBean();
		personBean1.setName("赵明丽");
		personBean1.setType("ID_CARD");
		personBean1.setNumber("540101198709260015");

		// 接口调用方的个人类客户证件信息(请根据实际情况进行替换)
		// 与该场景式存证编号有关的客户证件信息
		CertificateBean personBean2 = new CertificateBean();
		personBean2.setName("金明丽");
		personBean2.setType("ID_CARD");
		personBean2.setNumber("540101198709260058");

		certificates.add(personBean1);
		certificates.add(personBean2);

		// 请求参数-JSON字符串
		JSONObject param_json = new JSONObject();
		param_json.put("evid", C_Evid);
		param_json.put("certificates", JSONArray.fromObject(certificates));
		System.out.println("请求参数:" + param_json);

		// 请求签名值
		String signature = DigestHelper.getSignature(param_json.toString(), SceneConfig.PROJECT_SECRET,
				SceneConfig.ALGORITHM, SceneConfig.ENCODING);

		// HTTP请求内容类型
		String ContentType = "application/json";

		// 设置HTTP请求头信息
		LinkedHashMap<String, String> headers = HttpHelper.getPOSTHeaders(SceneConfig.PROJECT_ID, signature,
				SceneConfig.ALGORITHM, ContentType, SceneConfig.ENCODING);

		// 向指定URL发送POST方法的请求
		JSONObject result = HttpHelper.sendPOST(apiUrl, param_json.toString(), headers, SceneConfig.ENCODING);

		int errCode = result.getInt("errCode");
		if (0 == errCode) {
			// 场景式存证编号关联到指定用户是否成功标识
			boolean isAppendSuccess = result.getBoolean("success");
			System.out.println("场景式存证编号(证据链编号)关联到指定用户是否成功: " + isAppendSuccess);
		} else {
			throw new RuntimeException(MessageFormat.format("场景式存证编号(证据链编号)关联到指定用户失败,失败信息：errCode = {0}, msg = {1}",
					errCode, result.get("msg")));
		}
	}

	/***
	 * 拼接存证证明查看页面完整Url,以便指定用户进行存证查看
	 *
	 * @param C_Evid
	 *            场景式存证编号(证据链编号)
	 * @return
	 */
	public static String getViewCertificateInfoUrl(String apiUrl, String C_Evid) {
		String timestampString = null;
		// 存证证明页面查看地址Url的有效期：
		String reverse = "true";
		if ("false".equals(reverse)) {
			// false表示timestamp字段为链接的生效时间，在生效30分钟后该链接失效
			long timestamp = System.currentTimeMillis();
			timestampString = ToolsHelper.stampToString(timestamp);// 当前系统的时间戳(毫秒级)
		} else {
			// true表示timestamp字段为链接的失效时间,假设2018年12月31日23点59分59秒链接失效
			timestampString = ToolsHelper.dateToStamp("2018-12-31 23:59:59");
			System.out.println("毫秒级时间戳 = " + timestampString);
		}
		// 证件类型
		String type = "ID_CARD";
		// 证件号码，指定这个用户查看这个存证证明时，页面中的证明持有人一栏显示的是该用户名称
		String number = "540101198709260015";

		StringBuffer param = new StringBuffer();
		param.append("id=" + C_Evid);
		param.append("&projectId=" + SceneConfig.PROJECT_ID);
		param.append("&timestamp=" + timestampString);
		param.append("&reverse=" + reverse);
		param.append("&type=" + type);
		param.append("&number=" + number);
		System.out.println("动态链接Url部分:" + param);
		// 请求签名值
		String signature = DigestHelper.getSignature(param.toString(), SceneConfig.PROJECT_SECRET, "HmacSHA256",
				"UTF-8");
		// 存证证明页面查看完整Url
		String viewCertificateInfoUrl = apiUrl + "?" + param.toString() + "&signature=" + signature;
		return viewCertificateInfoUrl;
	}
}
