package utils;

import bean.DisplayLinkParam;
import constant.SceneConfig;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/***
 * @Description: 场景式存证_数据字典创建辅助类
 * @Team: 公有云技术支持小组
 * @Author: 天云小生
 * @Date: 2018年01月16日
 */
public class SceneDataDictionaryHelper {

	/***
	 * 定义所属行业类型
	 */
	public static String createIndustryType(String apiUrl) {
		System.out.println("请求API接口地址:" + apiUrl);
		// 数据字典模板ID
		String templetId = null;
		// 行业名称
		String industyName = "房屋租赁行业";
		// 行业名称列表(根据实际情况进行增减或修改,此处仅以"房屋租赁行业"行业为例)
		ArrayList<String> industries = new ArrayList<String>();
		industries.add(industyName);// 如:金融行业-P2P信贷,医药卫生

		// 请求参数-JSON字符串
		JSONObject param_json = new JSONObject();
		param_json.put("name", JSONArray.fromObject(industries));
		System.out.println("请求参数:" + param_json);

		// 请求签名值
		String signature = DigestHelper.getSignature(param_json.toString(), SceneConfig.PROJECT_SECRET,
				SceneConfig.ALGORITHM, SceneConfig.ENCODING);
				// System.out.println("请求签名值 = " + signature);

		// HTTP请求内容类型
		String ContentType = "application/json";

		// 设置HTTP请求头信息
		LinkedHashMap<String, String> headers = HttpHelper.getPOSTHeaders(SceneConfig.PROJECT_ID, signature,
				SceneConfig.ALGORITHM, ContentType, SceneConfig.ENCODING);

		// 向指定URL发送POST方法的请求
		JSONObject RequestResult = HttpHelper.sendPOST(apiUrl, param_json.toString(), headers, SceneConfig.ENCODING);
		System.out.println("[定义所属行业类型]接口返回json数据:" + RequestResult);
		if (0 == RequestResult.getInt("errCode")) {
			String result = RequestResult.getString("result");
			templetId = JSONHelper.getTempletId(result, industyName);
		} else {
			throw new RuntimeException(MessageFormat.format("发生错误,错误信息 = {0}", RequestResult));
		}
		return templetId;
	}

	/***
	 * 定义业务凭证（名称）(如：房屋租赁合同签署)
	 *
	 * @param businessTempletId
	 *            所属行业类型ID
	 */
	public static String createSceneType(String apiUrl, String businessTempletId) {
		System.out.println("请求API接口地址:" + apiUrl);
		// 数据字典模板ID
		String templetId = null;
		// 业务凭证（名称）
		String sceneBusinessName = "钢板材料合同签署";
		// 业务凭证（名称）列表(根据实际情况进行增减或修改,此处仅以"房屋租赁合同签署"为例)
		ArrayList<String> scenes = new ArrayList<String>();
		scenes.add(sceneBusinessName);

		// 请求参数-JSON字符串
		JSONObject param_json = new JSONObject();
		// businessTempletId对应的是[定义所属行业类型]时获取的[所属行业类型ID]
		param_json.put("businessTempletId", businessTempletId);
		param_json.put("name", JSONArray.fromObject(scenes));
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
		JSONObject RequestResult = HttpHelper.sendPOST(apiUrl, param_json.toString(), headers, SceneConfig.ENCODING);
		System.out.println("[定义业务凭证（名称）]接口返回json数据:" + RequestResult);
		if (0 == RequestResult.getInt("errCode")) {
			String result = RequestResult.getString("result");
			templetId = JSONHelper.getTempletId(result, sceneBusinessName);
		} else {
			throw new RuntimeException(MessageFormat.format("发生错误,错误信息 = {0}", RequestResult));
		}
		return templetId;
	}

	/***
	 * 定义业务凭证中某一证据点名称(如：合同签署人信息)
	 *
	 * @param sceneTempletId
	 *            业务凭证（名称）ID
	 */
	public static String createSegmentType(String apiUrl, String sceneTempletId) {
		System.out.println("请求API接口地址:" + apiUrl);
		// 数据字典模板ID
		String templetId = null;
		//
		String segmentName = "甲方合同签署人信息";
		// 存证场景环节类型列表(根据实际情况进行增减或修改,此处仅以"合同签署人信息"为例)
		ArrayList<String> segments = new ArrayList<String>();
		segments.add(segmentName);

		// 请求参数-JSON字符串
		JSONObject param_json = new JSONObject();
		// sceneTempletId对应的是[定义业务凭证（名称）]时获取的[业务凭证（名称）ID]
		param_json.put("sceneTempletId", sceneTempletId);
		param_json.put("name", JSONArray.fromObject(segments));
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
		JSONObject RequestResult = HttpHelper.sendPOST(apiUrl, param_json.toString(), headers, SceneConfig.ENCODING);
		System.out.println("[定义业务凭证中某一证据点名称]接口返回json数据:" + RequestResult);
		if (0 == RequestResult.getInt("errCode")) {
			String result = RequestResult.getString("result");
			templetId = JSONHelper.getTempletId(result, segmentName);
		} else {
			throw new RuntimeException(MessageFormat.format("发生错误,错误信息 = {0}", RequestResult));
		}
		return templetId;
	}

	/***
	 * 定义业务凭证中某一证据点的字段属性(如：姓名-name) 设置显示名称与参数名称的对应关系
	 *
	 * @param segmentTempletId
	 *            业务凭证中某一证据点名称ID
	 */
	public static void createSegmentPropType(String apiUrl, String segmentTempletId) {
		System.out.println("请求API接口地址:" + apiUrl);
		// 业务凭证中某一证据点的字段属性列表(根据实际情况进行增减或修改,此处仅以"姓名-realName|用户名-userName"为例)
		DisplayLinkParam displayLinkParamRealName1 = new DisplayLinkParam();
		displayLinkParamRealName1.setDisplayName("甲方签署人");
		displayLinkParamRealName1.setParamName("realName_1");

		DisplayLinkParam displayLinkParamUserName1 = new DisplayLinkParam();
		// 用户在平台系统注册时的用户名
		displayLinkParamUserName1.setDisplayName("甲方签署人的用户名");
		displayLinkParamUserName1.setParamName("userName_1");

		DisplayLinkParam displayLinkParamRealName2 = new DisplayLinkParam();
		displayLinkParamRealName2.setDisplayName("乙方签署人");
		displayLinkParamRealName2.setParamName("realName_2");

		DisplayLinkParam displayLinkParamUserName2 = new DisplayLinkParam();
		// 用户在平台系统注册时的用户名
		displayLinkParamUserName2.setDisplayName("乙方签署人的用户名");
		displayLinkParamUserName2.setParamName("userName_2");

		ArrayList<String> segmentProp = new ArrayList<String>();
		segmentProp.add(JSONObject.fromObject(displayLinkParamRealName1).toString());
		segmentProp.add(JSONObject.fromObject(displayLinkParamUserName1).toString());
		segmentProp.add(JSONObject.fromObject(displayLinkParamRealName2).toString());
		segmentProp.add(JSONObject.fromObject(displayLinkParamUserName2).toString());

		// 请求参数-JSON字符串
		JSONObject param_json = new JSONObject();
		// segmentTempletId对应的是[定义业务凭证中某一证据点名称]时获取的[定义业务凭证中某一证据点名称ID]
		param_json.put("segmentTempletId", segmentTempletId);
		// 业务凭证中某一证据点字段属性列表
		param_json.put("properties", JSONArray.fromObject(segmentProp));
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

		System.out.println("[定义业务凭证中某一证据点的字段属性]接口返回json数据:" + result);
	}
}
