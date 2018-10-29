package Scene;

import constant.SceneConfig;
import java.io.File;
import net.sf.json.JSONObject;
import utils.DemoMessage;
import utils.SceneDataDictionaryHelper;
import utils.SceneHelper;

/***
 * @Description: 场景式存证_Demo
 * @Version: Ver_1.1
 * @Team: 公有云技术支持小组
 * @Author: 天云小生
 * @Date: 2018年04月15日
 */

public class SceneEviDemo {

	// 所属行业类型ID
	public static String BusinessTempletId = null;
	// 业务凭证（名称）ID
	public static String SceneTempletId = null;
	// 业务凭证中某一证据点名称ID
	public static String SegmentTempletId = null;
	// 场景式存证编号（证据链编号）
	public static String C_Evid = null;
	// 原文存证（基础版）证据点ID（编号）
	public static String H_Standard_Evid = null;
	// 原文存证（高级版）证据点ID（编号）
	public static String H_Advanced_Evid = null;
	// 摘要存证证据点ID（编号）
	public static String H_Digest_Evid = null;
	// 用于上传待存证文档的Url
	public static String UploadUrl = null;
	// 存证证明页面查看完整Url
	public static String ViewInfoUrl = null;
	// 是否使用e签宝电子签名,如果使用请主动传递相关的全部签署记录ID(SignServiceId)
	public static boolean isHaveSignServiceId  = true;

	/***
	 * 本demo中涉及的信息均为方便演示，实际开发中请根据贵司的实际业务进行调整
	 */
	public static void main(String[] args) {
		// 示例Demo重要提示
		DemoMessage.showImportantMessage();
		// 介绍两种常见的存证场景
		DemoMessage.showHowToUseHelp();

		// 根据实际情况创建场景式存证数据字典(全局范围) 【提示:】场景式存证数据字典只需要创建一次,存在于数据字典中的ID可永久使用
		doSceneDataDictionary();

		// 待存证的文档路径（ 默认路径在项目下的files文件夹下）
		String filePath = "src/main/resources/pdf" + File.separator + "signed.pdf";

		// 示例(如不知道该如何选择原文基础版、原文高级版或摘要版时请询问e签宝对接人员确认贵司所购买的存证类型)
		// 11:原文基础版 | 12:原文高级版 | 13:摘要版
		int example = 13;
		switch (example) {
		case 11:
			System.out.println("- - - - - - - [原文基础版存证演示] - - - - - - -");
			// 原文基础版存证成功后将原文同时推送到e签宝服务端和司法鉴定中心,不会推送到公证处
			// (请询问e签宝对接人员确认贵司所购买的存证类型)
			System.out.println("- - - - - - - [第一步:创建证据链,获取场景式存证编号（证据链编号）] - - - - - - -");
			// 场景式存证编号(证据链编号)(请妥善保管场景式存证编号,以便日后查询存证证明)
			C_Evid = SceneHelper.createChainOfEvidence(SceneConfig.VOUCHER_API, SceneTempletId);
			System.out.println("- - - - - - - [第二步:创建原文存证（基础版）证据点,获取存证环节编号（证据点编号）和待存证文档上传Url] - - - - - - -");
			// 原文存证（基础版）证据点ID（编号）
			JSONObject standard_Result = SceneHelper.createSegmentOriginal_Standard(SceneConfig.ORIGINAL_STANDARD_API, filePath,
					SegmentTempletId);
			// 原文存证（基础版）证据点ID（编号）
			H_Standard_Evid = standard_Result.getString("evid");
			System.out.println("原文存证（基础版）证据点ID（编号）:" + H_Standard_Evid);
			// 待保全文档上传Url
			UploadUrl = standard_Result.getString("url");
			System.out.println("待保全文档上传Url:" + UploadUrl);
			System.out.println("- - - - - - - [第三步:进行待存证文档的上传] - - - - - - -");
			// 待存证文档上传
			SceneHelper.uploadOriginalDocumen(H_Standard_Evid, UploadUrl, filePath);
			System.out.println("- - - - - - - [第四步:追加证据点,将证据点追加到已存在的证据链内形成证据链] - - - - - - -");
			// 是否使用e签宝电子签名,如果使用请主动传递相关的全部签署记录ID(SignServiceId)
			isHaveSignServiceId = true;
			// 向已存在的证据链中关联或追加证据(如追加补充协议的签署存证信息)
			SceneHelper.appendEvidence(SceneConfig.VOUCHER_APPEND_API, C_Evid, H_Standard_Evid,isHaveSignServiceId);
			System.out.println("- - - - - - - [第五步:场景式存证编号(证据链编号)关联到指定的用户,以便指定用户日后可以顺利出证] - - - - - - -");
			SceneHelper.relateSceneEvIdWithUser(SceneConfig.RELATE_API, C_Evid);
			System.out.println("- - - - - - - [第六步:通过贵司的系统跳转到存证证明查看页面,以便指定用户进行存证查看] - - - - - - -");
			// 存证证明页面查看完整Url
			ViewInfoUrl = SceneHelper.getViewCertificateInfoUrl(SceneConfig.VIEWPAGE_URL, C_Evid);
			System.out.println("存证证明页面查看完整Url = " + ViewInfoUrl);
			break;
		case 12:
			System.out.println("- - - - - - - [原文高级版存证演示] - - - - - - -");
			// 原文高级版存证成功后将原文同时推送到e签宝服务端、司法鉴定中心和公证处
			// (请询问e签宝对接人员确认贵司所购买的存证类型)
			System.out.println("- - - - - - - [第一步:创建证据链,获取场景式存证编号（证据链编号）] - - - - - - -");
			// 场景式存证编号(证据链编号)(请妥善保管场景式存证编号,以便日后查询存证证明)
			C_Evid = SceneHelper.createChainOfEvidence(SceneConfig.VOUCHER_API, SceneTempletId);
			System.out.println("- - - - - - - [第二步:创建原文存证（高级版）证据点,获取存证环节编号（证据点编号）和待存证文档上传Url] - - - - - - -");
			// 原文存证（高级版）证据点ID（编号）
			JSONObject advanced_Result = SceneHelper.createSegmentOriginal_Advanced(SceneConfig.ORIGINAL_ADVANCED_API, filePath,
					SegmentTempletId);
			// 原文存证（高级版）证据点ID（编号）
			H_Advanced_Evid = advanced_Result.getString("evid");
			System.out.println("原文存证（高级版）证据点ID（编号）:" + H_Advanced_Evid);
			// 待保全文档上传Url
			UploadUrl = advanced_Result.getString("url");
			System.out.println("待保全文档上传Url:" + UploadUrl);
			System.out.println("- - - - - - - [第三步:进行待存证文档的上传] - - - - - - -");
			// 待存证文档上传
			SceneHelper.uploadOriginalDocumen(H_Advanced_Evid, UploadUrl, filePath);
			System.out.println("- - - - - - - [第四步:追加证据点,将证据点追加到已存在的证据链内形成证据链] - - - - - - -");
			// 是否使用e签宝电子签名,如果使用请主动传递相关的全部签署记录ID(SignServiceId)
			isHaveSignServiceId  = true;
			// 向已存在的证据链中关联或追加证据(如追加补充协议的签署存证信息)
			SceneHelper.appendEvidence(SceneConfig.VOUCHER_APPEND_API, C_Evid, H_Advanced_Evid,isHaveSignServiceId);
			System.out.println("- - - - - - - [第五步:场景式存证编号(证据链编号)关联到指定的用户,以便指定用户日后可以顺利出证] - - - - - - -");
			SceneHelper.relateSceneEvIdWithUser(SceneConfig.RELATE_API, C_Evid);
			System.out.println("- - - - - - - [第六步:通过贵司的系统跳转到存证证明查看页面,以便指定用户进行存证查看] - - - - - - -");
			// 存证证明页面查看完整Url
			ViewInfoUrl = SceneHelper.getViewCertificateInfoUrl(SceneConfig.VIEWPAGE_URL, C_Evid);
			System.out.println("存证证明页面查看完整Url = " + ViewInfoUrl);
			break;
		case 13:
			System.out.println("- - - - - - - [摘要版存证演示] - - - - - - -");
			// 摘要版存证不会将原文进行推送,仅是将原文的摘要(SHA256)推送到e签宝服务端和司法鉴定中心,文件摘要(SHA256)不支持存放到公证处
			// (请询问e签宝对接人员确认贵司所购买的存证类型)
			System.out.println("- - - - - - - [第一步:创建证据链,获取场景式存证编号（证据链编号）] - - - - - - -");
			// 场景式存证编号(证据链编号)(请妥善保管场景式存证编号,以便日后查询存证证明)
			C_Evid = SceneHelper.createChainOfEvidence(SceneConfig.VOUCHER_API, SceneTempletId);
			System.out.println("- - - - - - - [第二步:创建摘要版存证环节,不想将待存证文档上传给第三方系统时可以选择该类存证环节] - - - - - - -");
			// 摘要版证据点ID（编号）
			JSONObject digest_Result = SceneHelper.createSegmentOriginal_Digest(SceneConfig.ORIGINAL_DIGEST_API, filePath,
					SegmentTempletId);
			// 摘要版证据点ID（编号）
			H_Digest_Evid = digest_Result.getString("evid");
			System.out.println("摘要版证据点ID（编号）:" + H_Digest_Evid);
			System.out.println("- - - - - - - [第三步:追加证据点,将证据点追加到已存在的证据链内形成证据链] - - - - - - -");
			// 是否使用e签宝电子签名,如果使用请主动传递相关的全部签署记录ID(SignServiceId)
			isHaveSignServiceId  = true;
			// 向已存在的证据链中关联或追加证据(如追加补充协议的签署存证信息)
			SceneHelper.appendEvidence(SceneConfig.VOUCHER_APPEND_API, C_Evid, H_Digest_Evid,isHaveSignServiceId);
			System.out.println("- - - - - - - [第四步:场景式存证编号(证据链编号)关联到指定的用户,以便指定用户日后可以顺利出证] - - - - - - -");
			SceneHelper.relateSceneEvIdWithUser(SceneConfig.RELATE_API, C_Evid);
			System.out.println("- - - - - - - [第五步:通过贵司的系统跳转到存证证明查看页面,以便指定用户进行存证查看] - - - - - - -");
			// 存证证明页面查看完整Url
			ViewInfoUrl = SceneHelper.getViewCertificateInfoUrl(SceneConfig.VIEWPAGE_URL, C_Evid);
			System.out.println("存证证明页面查看完整Url = " + ViewInfoUrl);
			break;
		default:
			System.out.println("示例演示时发生异常:请从11-13之间选择一种示例~");
			break;
		}
	}

	/***
	 * 根据实际情况创建场景式存证数据字典(全局范围) 【提示:】场景式存证数据字典只需要创建一次,存在于数据字典中的ID可永久使用
	 */
	public static void doSceneDataDictionary() {
		// 定义所属行业类型,获取所属行业类型ID
		BusinessTempletId = SceneDataDictionaryHelper.createIndustryType(SceneConfig.BUS_ADD_API);
		// 定义业务凭证（名称）, 获取业务凭证（名称）ID
		SceneTempletId = SceneDataDictionaryHelper.createSceneType(SceneConfig.SCENE_ADD_API, BusinessTempletId);
		// 定义业务凭证中某一证据点名称,获取业务凭证中某一证据点名称ID
		SegmentTempletId = SceneDataDictionaryHelper.createSegmentType(SceneConfig.SEG_ADD_API, SceneTempletId);

		System.out.println("行业模板id "+SegmentTempletId);
		// 定义业务凭证中某一证据点的字段属性
		SceneDataDictionaryHelper.createSegmentPropType(SceneConfig.SEGPROP_ADD_API, SegmentTempletId);
	}
}
