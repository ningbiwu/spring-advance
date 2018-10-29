package eSign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timevale.esign.sdk.tech.bean.result.*;
import com.timevale.esign.sdk.tech.service.*;
import com.timevale.esign.sdk.tech.service.factory.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.timevale.esign.sdk.tech.bean.EventBean;
import com.timevale.esign.sdk.tech.bean.OrganizeBean;
import com.timevale.esign.sdk.tech.bean.PersonBean;
import com.timevale.esign.sdk.tech.bean.PosBean;
import com.timevale.esign.sdk.tech.bean.SignPDFFileBean;
import com.timevale.esign.sdk.tech.bean.SignPDFStreamBean;
import com.timevale.esign.sdk.tech.bean.SignParamBean;
import com.timevale.esign.sdk.tech.bean.UpdateOrganizeBean;
import com.timevale.esign.sdk.tech.bean.UpdatePersonBean;
import com.timevale.esign.sdk.tech.bean.EventBean.EventTargetBean;
import com.timevale.esign.sdk.tech.bean.seal.OrganizeTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.PersonTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.SealColor;
import com.timevale.esign.sdk.tech.impl.constants.LicenseType;
import com.timevale.esign.sdk.tech.impl.constants.OrganRegType;
import com.timevale.esign.sdk.tech.impl.constants.SignType;
import com.timevale.esign.sdk.tech.v3.service.TemplateSealService;
import com.timevale.esign.sdk.tech.v3.service.TemplateSignService;
import com.timevale.esign.sdk.tech.v3.service.factory.TemplateSealServiceFactory;
import com.timevale.esign.sdk.tech.v3.service.factory.TemplateSignServiceFactory;
import com.timevale.esign.sdk.tech.v3.service.sign.bean.SignatureSettings;
import com.timevale.tech.sdk.bean.HttpConnectionConfig;
import com.timevale.tech.sdk.bean.ProjectConfig;
import com.timevale.tech.sdk.bean.SignatureConfig;
import com.timevale.tech.sdk.constants.AlgorithmType;
import com.timevale.tech.sdk.constants.HttpType;

import utils.FileHelper;

/***
 * e签宝快捷签SDK辅助类
 *
 * @author Ching
 *
 */
public class SignHelper {
	private static Logger LOG = LoggerFactory.getLogger(SignHelper.class);

	/***
	 * 项目初始化 使用到的接口：sdk.init(proCfg, httpConCfg, sCfg);
	 */
	public static void initProject() {

		ProjectConfig proCfg = new ProjectConfig();

		// 项目ID(应用ID)
		proCfg.setProjectId("1111563517");// 1111563517
		// 项目Secret(应用Secret)
		proCfg.setProjectSecret("95439b0863c241c63a861b87d1e647b7"); // 95439b0863c241c63a861b87d1e647b7
		// 开放平台地址
		proCfg.setItsmApiUrl("http://121.40.164.61:8080/tgmonitor/rest/app!getAPIInfo2");

		HttpConnectionConfig httpConCfg = new HttpConnectionConfig();
		// 协议类型，默认https
		httpConCfg.setHttpType(HttpType.HTTP);
		// 请求失败重试次数，默认5次
		httpConCfg.setRetry(5);
		// 代理服务IP地址
		// httpConCfg.setProxyIp(null);
		// 代理服务端口
		// httpConCfg.setProxyPort(0);
if(StringUtils.isNotEmpty("")){

}
		SignatureConfig sCfg = new SignatureConfig();
		// 算法类型，默认HMAC-SHA256
		sCfg.setAlgorithm(AlgorithmType.HMACSHA256); // 可选RSA，但推荐使用HMACSHA256
		// e签宝公钥，可以从开放平台获取。若算法类型为RSA，此项必填
		sCfg.setEsignPublicKey("");
		// 平台私钥，可以从开放平台下载密钥生成工具生成。若算法类型为RSA，此项必填
		sCfg.setPrivateKey("");
		System.out.println("--项目初始化...");
		EsignsdkService sdk = EsignsdkServiceFactory.instance();
		Result result = sdk.init(proCfg, httpConCfg, sCfg);
		if (0 != result.getErrCode()) {
			LOG.info("--项目初始化失败：errCode=" + result.getErrCode() + " msg=" + result.getMsg() );
		} else {
			System.out.println("--项目初始化成功！errCode=" + result.getErrCode() + " msg=" + result.getMsg());
		}
	}

	/***
	 * 坐标定位签署的PosBean
	 */
	public static PosBean setXYPosBean(String page,int x,int y) {
		PosBean posBean = new PosBean();
		// 定位类型，0-坐标定位，1-关键字定位，默认0，若选择关键字定位，签署类型(signType)必须指定为关键字签署才会生效。
		posBean.setPosType(0);
		// 签署页码，若为多页签章，支持页码格式“1-3,5,8“，若为坐标定位时，不可空
		posBean.setPosPage(page);
		// 签署位置X坐标，默认值为0，以pdf页面的左下角作为原点，控制横向移动距离，单位为px
		posBean.setPosX(x);
		// 签署位置Y坐标，默认值为0，以pdf页面的左下角作为原点，控制纵向移动距离，单位为px
		posBean.setPosY(y);
		// 印章展现宽度，将以此宽度对印章图片做同比缩放。详细查阅接口文档的15 PosBean描述
		posBean.setWidth(159);
		return posBean;
	}

	/***
	 * 关键字定位签署的PosBean
	 */
	public static PosBean setKeyPosBean(String key,int x, int y,int width) {
		PosBean posBean = new PosBean();
		// 定位类型，0-坐标定位，1-关键字定位，默认0，若选择关键字定位，签署类型(signType)必须指定为关键字签署才会生效。
		posBean.setPosType(1);
		// 关键字签署时不可空 */
		posBean.setKey(key);
		// 关键字签署时会对整体pdf文档进行搜索，故设置签署页码无效
		// posBean.setPosPage("1");
		// 签署位置X坐标，以关键字所在位置为原点进行偏移，默认值为0，控制横向移动距离，单位为px
		posBean.setPosX(x);
		// 签署位置Y坐标，以关键字所在位置为原点进行偏移，默认值为0，控制纵向移动距离，单位为px
		posBean.setPosY(y);
		// 印章展现宽度，将以此宽度对印章图片做同比缩放。详细查阅接口文档的15 PosBean描述
		posBean.setWidth(width);
		return posBean;
	}

	/***
	 * 文件流签署的PDF文档信息
	 */
	public static SignPDFStreamBean setSignPDFStreamBean(byte[] pdfFileStream) {
		SignPDFStreamBean signPDFStreamBean = new SignPDFStreamBean();
		// 待签署文档本地二进制数据
		signPDFStreamBean.setStream(pdfFileStream);
		// 文档名称，e签宝签署日志对应的文档名，若为空则取文档路径中的名称
		// signPDFStreamBean.setFileName("pdf文件名");
		// 文档编辑密码，当目标PDF设置权限密码保护时必填 */
		// signPDFStreamBean.setOwnerPassword(null);
		return signPDFStreamBean;
	}

	/***
	 * 文件路径签署的PDF文档信息
	 */
	public static SignPDFFileBean setSignPDFFileBean(String srcPdfFile, String signedPdfFile) {
		SignPDFFileBean signPDFFileBean = new SignPDFFileBean();
		// 待签署文档本地路径
		signPDFFileBean.setSrcPdfFile(srcPdfFile);
		// 签署后文档本地路径
		signPDFFileBean.setDstPdfFile(signedPdfFile);
		// 文档名称，e签宝签署日志对应的文档名，若为空则取文档路径中的名称
		signPDFFileBean.setFileName("pdf文件名");
		// 文档编辑密码，当目标PDF设置权限密码保护时必填 */
		signPDFFileBean.setOwnerPassword(null);
		return signPDFFileBean;
	}

	/***
	 * 平台自身PDF摘要签署（文件二进制流）； 盖章位置通过坐标定位； 使用到接口：SelfSignServiceFactory.instance();
	 * selfSignService.localSignPdf(signPDFStreamBean, posBean, sealId,
	 * SignType.Single);
	 */
	public static FileDigestSignResult platformSignByStreamm(String srcPdfFile) {
		// 设置文件流签署的PDF文档信息
		SignPDFStreamBean signPDFStreamBean = setSignPDFStreamBean(FileHelper.getBytes(srcPdfFile));
		// 设置坐标定位签署的PosBean，坐标定位方式支持单页签章、多页签章和骑缝章，但对关键字签章指定页码无效；
		PosBean posBean = setXYPosBean("1",170,714);
		// 设置签署类型为 单页签章，坐标定位方式支持单页签章、多页签章和骑缝章
		SignType signType = SignType.Single;
		// 设置签署印章，www.tsign.cn官网设置的默认签名sealId = 0
		int sealId = 0;

		System.out.println("----开始平台自身PDF摘要签署...");
		SelfSignService selfSignService = SelfSignServiceFactory.instance();
		FileDigestSignResult fileDigestSignResult = selfSignService.localSignPdf(signPDFStreamBean, posBean, sealId,
				signType);
		if (0 != fileDigestSignResult.getErrCode()) {
			LOG.info("平台自身PDF摘要签署（文件流）失败，errCode=" + fileDigestSignResult.getErrCode() + " msg="
					+ fileDigestSignResult.getMsg());
		} else {
			System.out.println("----平台自身PDF摘要签署成功！签署记录SignServiceId = " + fileDigestSignResult.getSignServiceId() + ", 请保存该签署记录Id！");
		}
		return fileDigestSignResult;

	}

	/***
	 * 平台自身PDF摘要签署（文件）； 盖章位置通过坐标定位； 使用到接口：SelfSignServiceFactory.instance();
	 * selfSignService.localSignPdf(signPDFStreamBean, posBean, sealId,
	 * SignType.Single);
	 */
	public static FileDigestSignResult platformSignByFile(String srcPdfFile,String signedPdfFile) {
		// 设置文件签署的PDF文档信息
		SignPDFFileBean signPDFFileBean = setSignPDFFileBean(srcPdfFile,signedPdfFile);
		// 设置坐标定位签署的PosBean，坐标定位方式支持单页签章、多页签章和骑缝章，但对关键字签章指定页码无效；
		PosBean posBean = setXYPosBean("1",170,714);
		// 设置签署类型为 单页签章，坐标定位方式支持单页签章、多页签章和骑缝章
		SignType signType = SignType.Single;
		// 设置签署印章，www.tsign.cn官网设置的默认签名sealId = 0
		int sealId = 0;

		System.out.println("----开始平台自身PDF摘要签署...");
		SelfSignService selfSignService = SelfSignServiceFactory.instance();
		FileDigestSignResult fileDigestSignResult = selfSignService.localSignPdf(signPDFFileBean, posBean, sealId,
				signType);
		if (0 != fileDigestSignResult.getErrCode()) {
			LOG.info("平台自身PDF摘要签署（文件）失败，errCode=" + fileDigestSignResult.getErrCode() + " msg="
					+ fileDigestSignResult.getMsg());
		} else {
			System.out.println("----平台自身PDF摘要签署成功！SignServiceId = " + fileDigestSignResult.getSignServiceId());
		}
		return fileDigestSignResult;
	}


	/***
	 *批量签署
	 */
	public static CodeMultiSignResult SafeMultiSignPDF(String srcPdfFile,String signedPdf,String code) {
		// 设置文件签署的PDF文档信息
		SignPDFFileBean fileBean = setSignPDFFileBean(srcPdfFile,signedPdf);
		String accountId = SignHelper.addPersonAccount();
		AddSealResult sealData = SignHelper.addPersonTemplateSeal(accountId);

		List<SignParamBean> signParams =new ArrayList();
		SignParamBean signParamBean  = new SignParamBean();

		signParamBean.setFileBean(fileBean);
		signParamBean.setSignPos(setXYPosBean("1",170,714));
		signParamBean.setSignType(SignType.Single);

		System.out.println("----开始批量PDF摘要签署...");
		UserSignService userSignService = UserSignServiceFactory.instance();
		CodeMultiSignResult codeMultiSignResult = userSignService.localSafeMultiSignPDF(accountId, signParams, sealData.getSealData(),code);
		if (0 != codeMultiSignResult.getErrCode()) {
			LOG.info("平台自身PDF摘要签署（文件）失败，errCode=" + codeMultiSignResult.getErrCode() + " msg="
					+ codeMultiSignResult.getMsg());
		} else {
			System.out.println("----平台自身PDF摘要签署成功！SignServiceId = " + codeMultiSignResult.getSuccessList());
		}
		return codeMultiSignResult;
	}

	/***
	 * 平台下个人用户PDF摘要签署（文件二进制流）；盖章位置通过关键字定位； 使用到接口：UserSignServiceFactory.instance();
	 * userSignService.localSignPDF(accountId,addSealResult.getSealData(),
	 * signPDFStreamBean, posBean, SignType.Single);
	 */
	public static FileDigestSignResult userPersonSignByStream(byte[] pdfFileStream, String accountId,
			String sealData,int x,int y) {

		// 设置文件流签署的PDF文档信息
		SignPDFStreamBean signPDFStreamBean = setSignPDFStreamBean(pdfFileStream);
		// 设置坐标定位签署的PosBean，坐标定位方式支持单页签章、多页签章和骑缝章，但对关键字签章指定页码无效；
		//PosBean posBean = setKeyPosBean("甲方代表",110,0,90);
		PosBean posBean = setXYPosBean("1,2,3",x,y);
		// 设置签署类型为 关键字签章
		SignType signType = SignType.Multi;

		System.out.println("----开始平台个人客户的PDF摘要签署...");
		UserSignService userSignService = UserSignServiceFactory.instance();
			FileDigestSignResult fileDigestSignResult = userSignService.localSignPDF(accountId, sealData, signPDFStreamBean,
				posBean, signType);
		if (0 != fileDigestSignResult.getErrCode()) {
			LOG.info("平台个人客户的PDF摘要签署失败，errCode=" + fileDigestSignResult.getErrCode() + " msg="
					+ fileDigestSignResult.getMsg());
		} else {
			System.out.println("平台个人客户的PDF摘要签署成功！签署记录SignServiceId = " + fileDigestSignResult.getSignServiceId() + ", 请保存该签署记录Id！");
		}
		return fileDigestSignResult;
	}

	/***
	 * 平台下个人用户PDF摘要签署（文件）；盖章位置通过关键字定位； 使用到接口：UserSignServiceFactory.instance();
	 * userSignService.localSignPDF(accountId,addSealResult.getSealData(),
	 * signPDFStreamBean, posBean, SignType.Single);
	 */
	public static FileDigestSignResult userPersonSignByFile(String srcPdf,String signedPdf,String accountId,
			String sealData) {

		// 设置文件流签署的PDF文档信息
		SignPDFFileBean signPDFFileBean = setSignPDFFileBean(srcPdf,signedPdf);
		// 设置坐标定位签署的PosBean，坐标定位方式支持单页签章、多页签章和骑缝章，但对关键字签章指定页码无效；
		PosBean posBean = setKeyPosBean("乙方签名",0,0,159);
		// 设置签署类型为 关键字签章
		SignType signType = SignType.Key;

		System.out.println("----开始平台个人客户的PDF摘要签署...");
		UserSignService userSignService = UserSignServiceFactory.instance();
		FileDigestSignResult fileDigestSignResult = userSignService.localSignPDF(accountId, sealData, signPDFFileBean,
				posBean, signType);
		if (0 != fileDigestSignResult.getErrCode()) {
			LOG.info("平台个人客户的PDF摘要签署失败，errCode=" + fileDigestSignResult.getErrCode() + " msg="
					+ fileDigestSignResult.getMsg());
		} else {
			System.out.println("平台个人客户的PDF摘要签署成功！SignServiceId = " + fileDigestSignResult.getSignServiceId());
		}
		return fileDigestSignResult;
	}

	/***
	 * 平台下企业用户PDF摘要签署（文件二进制流）；盖章位置通过关键字定位； 使用到接口：UserSignServiceFactory.instance();
	 * userSignService.localSignPDF(accountId,addSealResult.getSealData(),
	 * signPDFStreamBean, posBean, SignType.Single);
	 */
	public static FileDigestSignResult userOrganizeSignByStream(byte[] pdfFileStream, String accountId,
			String sealData,int x,int y) {

		// 设置文件流签署的PDF文档信息
		SignPDFStreamBean signPDFStreamBean = setSignPDFStreamBean(pdfFileStream);
		// 设置坐标定位签署的PosBean，坐标定位方式支持单页签章、多页签章和骑缝章，但对关键字签章指定页码无效；
		PosBean posBean = setXYPosBean("1,2,3",x,y);
		// 设置签署类型为 关键字签章
		SignType signType = SignType.Multi;

		System.out.println("----开始平台企业客户的PDF摘要签署...");
		UserSignService userSignService = UserSignServiceFactory.instance();
		FileDigestSignResult fileDigestSignResult = userSignService.localSignPDF(accountId, sealData, signPDFStreamBean,
				posBean, signType);
		if (0 != fileDigestSignResult.getErrCode()) {
			LOG.info("平台企业客户的PDF摘要签署失败，errCode=" + fileDigestSignResult.getErrCode() + " msg="
					+ fileDigestSignResult.getMsg());
		} else {
			System.out.println("平台企业客户的PDF摘要签署成功！签署记录SignServiceId = " + fileDigestSignResult.getSignServiceId() + ", 请保存该签署记录Id！");
		}
		return fileDigestSignResult;
	}

	/***
	 * 创建个人账户 使用到接口：accountService.addAccount(organizeBean);
	 */
	public static String addPersonAccount() {
		PersonBean personBean = new PersonBean();
		// 邮箱地址,可空
		// personBean.setEmail(null);
		// 手机号码,可空
		personBean.setMobile("18518295734");
		// 姓名
		personBean.setName("宁必武");
		// 身份证号/护照号
		personBean.setIdNo("342626198910032697");
		// 个人归属地：0-大陆，1-香港，2-澳门，3-台湾，4-外籍，默认0
		personBean.setPersonArea(0);
		// 所属公司,可空
		// personBean.setOrgan("客户的企业");
		// 职位,可空
		// personBean.setTitle("部门经理");
		// 常用地址,可空
		// personBean.setAddress("XXX街道");

		System.out.println("----开始创建个人账户...");
		AccountService accountService = AccountServiceFactory.instance();
		AddAccountResult addAccountResult = accountService.addAccount(personBean);
		if (0 != addAccountResult.getErrCode()) {
			LOG.info("创建个人账户失败，errCode=" + addAccountResult.getErrCode() + " msg=" + addAccountResult.getMsg());
		} else {
			System.out.println("创建个人账户成功！accountId = " + addAccountResult.getAccountId());
		}
		return addAccountResult.getAccountId();

	}

	/***
	 * 创建企业账户,该企业账户是一个相对概念。可以理解成是你们公司的客户，这个客户是一个企业；
	 * 使用到接口：accountService.addAccount(organizeBean);
	 */
	public static String addOrganizeAccount() {
		OrganizeBean organizeBean = new OrganizeBean();
		// 邮箱地址,可空
		// organizeBean.setEmail(null);
		// 手机号码,可空
//		organizeBean.setMobile("188****0787");
		// 企业名称
		organizeBean.setName("成都省心宝科技有限公司");//天之云信息科技有限公司为公共使用，请不要修改
		// 单位类型，0-普通企业，1-社会团体，2-事业单位，3-民办非企业单位，4-党政及国家机构，默认0
		organizeBean.setOrganType(0);
		// 企业注册类型，NORMAL:组织机构代码号，MERGE：多证合一，传递社会信用代码号,REGCODE:企业工商注册码,默认NORMAL
		organizeBean.setRegType(OrganRegType.MERGE);
		// 组织机构代码号、社会信用代码号或工商注册号
		organizeBean.setOrganCode("52227058XT51M4AL62");//52227058XT51M4AL62为公共使用，请不要修改
		// 公司地址,可空
		organizeBean.setAddress("杭州城落霞峰7号");
		// 经营范围,可空
		// organizeBean.setScope("");

		// 注册类型，1-代理人注册，2-法人注册，默认1
		organizeBean.setUserType(1);

		// 代理人姓名，当注册类型为1时必填
		organizeBean.setAgentName("艾利");
		// 代理人身份证号，当注册类型为1时必填
		organizeBean.setAgentIdNo("220301198705170035");

		// 法定代表姓名，当注册类型为2时必填
		// organizeBean.setLegalName("天云");
		// 法定代表人归属地,0-大陆，1-香港，2-澳门，3-台湾，4-外籍，默认0
		// organizeBean.setLegalArea(0);
		// 法定代表身份证号/护照号，当注册类型为2时必填
		// organizeBean.setLegalIdNo("220301198705170019");

		System.out.println("----开始创建企业账户...");
		AccountService accountService = AccountServiceFactory.instance();
		AddAccountResult addAccountResult = accountService.addAccount(organizeBean);

		if (0 != addAccountResult.getErrCode()) {
			LOG.info("创建企业账户失败，errCode=" + addAccountResult.getErrCode() + " msg=" + addAccountResult.getMsg());
		} else {
			System.out.println("创建企业账户成功！accountId = " + addAccountResult.getAccountId());
		}
		return addAccountResult.getAccountId();

	}
	/***
	 * 通过accountId注销账户
	 * @param accountId
	 */
	public static void deleteAccount(String accountId){
		AccountService accountService = AccountServiceFactory.instance();
		Result result = accountService.deleteAccount(accountId);
		if (0 != result.getErrCode()) {
			LOG.info("注销个人账户失败，errCode=" + result.getErrCode() + " msg=" + result.getMsg());
		} else {
			System.out.println("注销个人账户成功！accountId = " + accountId + " 已被注销");
		}
	}

	/***
	 * 更新个人账户信息
	 * @param accountId
	 */
	public static void updatePersonAccount(String accountId,String mobile){

		UpdatePersonBean updatePersonBean = new UpdatePersonBean();
		updatePersonBean.setMobile(mobile);

		AccountService accountService = AccountServiceFactory.instance();
		Result result = accountService.updateAccount(accountId, updatePersonBean, null);
		if (0 != result.getErrCode()) {
			LOG.info("更新个人账户失败，errCode=" + result.getErrCode() + " msg=" + result.getMsg());
		} else {
			System.out.println("更新个人账户成功！accountId = " + accountId + " 已被更新");
		}
	}

	/***
	 * 更新企业账户信息
	 * @param accountId
	 */
	public static void updateOrganizeAccount(String accountId,String mobile){

		UpdateOrganizeBean updateOrganizeBean = new UpdateOrganizeBean();
		updateOrganizeBean.setMobile(mobile);

		AccountService accountService = AccountServiceFactory.instance();
		Result result = accountService.updateAccount(accountId, updateOrganizeBean, null);
		if (0 != result.getErrCode()) {
			LOG.info("更新企业账户失败，errCode=" + result.getErrCode() + " msg=" + result.getMsg());
		} else {
			System.out.println("更新企业账户成功！accountId = " + accountId + " 已被更新");
		}
	}

	/***
	 * 创建个人账户的印章； 使用到接口：sealService.addTemplateSeal(accountId,
	 * PersonTemplateType.SQUARE, SealColor.RED);
	 */
	public static AddSealResult addPersonTemplateSeal(String accountId) {
		// 印章模板类型：矩形印章
		PersonTemplateType personTemplateType = PersonTemplateType.RECTANGLE;
		// 印章颜色：红色
		SealColor sealColor = SealColor.RED;

		System.out.println("----开始创建个人账户的印章...");
		SealService sealService = SealServiceFactory.instance();
		AddSealResult addSealResult = sealService.addTemplateSeal(accountId, personTemplateType, sealColor);
		if (0 != addSealResult.getErrCode()) {
			LOG.info("创建个人模板印章失败，errCode=" + addSealResult.getErrCode() + " msg=" + addSealResult.getMsg());
		} else {
			System.out.println("创建个人模板印章成功！SealData = " + addSealResult.getSealData());
		}
		return addSealResult;

	}

	/***
	 * 创建企业账户的印章,该企业账户印章是一个相对概念。可以理解成是你们公司的客户企业印章；
	 * 使用到接口：sealService.addTemplateSeal(accountId, OrganizeTemplateType.STAR,
	 * SealColor.RED, "合同专用章", "下弦文");
	 */
	public static AddSealResult addOrganizeTemplateSeal(String accountId) {
		/*
		 * hText 生成印章中的横向文内容 如“合同专用章、财务专用章” qText 生成印章中的下弦文内容 公章防伪码（一串13位数字）
		 * 如91010086135601
		 */

		// 印章模板类型：标准公章
		OrganizeTemplateType organizeTemplateType = OrganizeTemplateType.STAR;
		// 印章颜色：红色
		SealColor sealColor = SealColor.RED;
		// 横向文字
		String hText = "合同专用章";
		// 下弦文字
		String qText = "91010086135601";
		System.out.println("----开始创建企业账户的印章...");
		SealService sealService = SealServiceFactory.instance();
		AddSealResult addSealResult = sealService.addTemplateSeal(accountId, organizeTemplateType, sealColor, hText,
				qText);
		if (0 != addSealResult.getErrCode()) {
			LOG.info("创建企业模板印章失败，errCode=" + addSealResult.getErrCode() + " msg=" + addSealResult.getMsg());
		} else {
			System.out.println("创建企业模板印章成功！SealData = " + addSealResult.getSealData());
		}
		return addSealResult;

	}

	/***
	 * 创建企业账户的印章,该企业账户印章是一个相对概念。可以理解成是你们公司的客户企业印章；
	 * 使用到接口：sealService.addTemplateSeal(accountId, OrganizeTemplateType.STAR,
	 * SealColor.RED, "合同专用章", "下弦文");
	 */
	public static AddSealResult addOrganizeOtherTemplateSeal(String accountId) {
		/*
		 * hText 生成印章中的横向文内容 如“合同专用章、财务专用章” qText 生成印章中的下弦文内容 公章防伪码（一串13位数字）
		 * 如91010086135601
		 */

		// 印章模板类型：标准公章
		OrganizeTemplateType organizeTemplateType = OrganizeTemplateType.OVAL;
		// 印章颜色：红色
		SealColor sealColor = SealColor.RED;
		// 横向文字
		String hText = "合同专用章";
		// 下弦文字
		String qText = "6666666666666";
		System.out.println("----开始创建企业账户2的印章...");
		SealService sealService = SealServiceFactory.instance();
		AddSealResult addSealResult = sealService.addTemplateSeal(accountId, organizeTemplateType, sealColor, hText,
				qText);
		if (0 != addSealResult.getErrCode()) {
			LOG.info("创建企业模板印章失败，errCode=" + addSealResult.getErrCode() + " msg=" + addSealResult.getMsg());
		} else {
			System.out.println("创建企业模板印章成功！SealData = " + addSealResult.getSealData());
		}
		return addSealResult;

	}

	/***
	 * 上传印章图片制作SealData；
	 * 使用到接口：Apache Commons Codec的org.apache.commons.codec.binary.Base64
	 * 该方法属于 第三方jar实现，并非快捷签SDK提供；
	 */
	public static String getSealDataByImage(String imgFilePath) {

		System.out.println("----开始将上传的印章图片转成SealData数据...");
		/* commons-codec-1.10.jar 第三方技术实现 */
		String sealData = FileHelper.GetImageStr(imgFilePath);
		System.out.println("----上传的印章图片转成SealData数据完成！sealData:" + sealData);
		return sealData;
	}

	/***
	 * 保存签署后的文件流
	 */
	public static boolean saveSignedByStream(byte[] signedStream,String signedFolder,String signedFileName) {
		System.out.println("----开始保存签署后文件...");
		boolean isSuccess = false;
		Map<String,String> fileResult = FileHelper.saveFileByStream(signedStream, signedFolder,signedFileName);
		if (0 != Integer.parseInt(fileResult.get("errCode"))) {
			LOG.info("保存签署后文件失败，errCode=" + fileResult.get("errCode") + " msg=" + fileResult.get("msg"));
		} else {
			isSuccess = true;
			LOG.info("保存签署后文件成功！errCode=" + fileResult.get("errCode") + " msg=" + fileResult.get("msg"));
		}
		return isSuccess;

	}



	/**
	 * 创建事件证书
	 * 说明：事件证书是将签署者与行为绑定后并固化至数字证书中而形成的一种数字证书，事件证书证明了事件本身，不仅简化了电子数据取证过程的复杂度，
	 * 还提升了电子数据的证据效力。同时，事件证书具有单次有效性，即使用证书完成签署后，此证书立即失效，不可重复使用。事件证书可以关联多个签署者。
	 */
	public static String addEvent() {
		EventBean eventBean = new EventBean();
		List<EventTargetBean> eventTargetBean = new ArrayList<EventTargetBean>();
		//创建事件证书的个人或者企业的信息
		EventTargetBean person1 = new EventBean().createTargetInstance("静珊", LicenseType.NORMALIDNO, "120223199901014682");
		EventTargetBean person2 = new EventBean().createTargetInstance("浩宇", LicenseType.NORMALIDNO, "120224199901016895");
		eventTargetBean.add(person1);
		eventTargetBean.add(person2);
		// 事件描述
		eventBean.setContent("事件描述");
		eventBean.setObjects(eventTargetBean);


		System.out.println("----开始创建事件证书...");
		EventCertService eventCertService = EventCertServiceFactory.instance();
		EventCertResult eventCertResult = eventCertService.addEventCert(eventBean);
		if (0 != eventCertResult.getErrCode()) {
			LOG.info("创建事件证书失败，errCode=" + eventCertResult.getErrCode() + " msg=" + eventCertResult.getMsg());
		} else {
			System.out.println("创建事件证书成功！accountId = " + eventCertResult.getCertId());
		}
		return eventCertResult.getCertId();
	}


	/**
	 * 创建个人印章（本地）没有使用创建个人账户接口创建accountid的时候 是这接口创建印章
	 * 输入个人模板印章相关信息创建的电子印章图片数据，作为个人用户签署印章使用。
	 * 注意：使用本地模板印章接口时需要依赖ext-sdk-fonts.jar
	 */

	public static String createPersonalTemplateSeal(String text) {
		// 印章模板类型：矩形印章
		PersonTemplateType type = PersonTemplateType.RECTANGLE;
		// 印章颜色：红色
		SealColor sealColor = SealColor.RED;

		System.out.println("----开始创建个人账户的印章（本地）...");
		TemplateSealService templateSealService = TemplateSealServiceFactory.instance();
		AddSealResult createPersonalTemplateSeal = templateSealService.createPersonalTemplateSeal(type, text, sealColor);
		if (0 != createPersonalTemplateSeal.getErrCode()) {
			LOG.info("创建个人模板印章失败，errCode=" + createPersonalTemplateSeal.getErrCode() + " msg=" + createPersonalTemplateSeal.getMsg());
		} else {
			System.out.println("创建个人模板印章成功！SealData = " + createPersonalTemplateSeal.getSealData());
		}
		return createPersonalTemplateSeal.getSealData();

	}
	/**
	 * 创建企业印章模板（本地）
	 * 说明：输入企业模板印章类型与印章文字创建的电子印章图片数据，作为企业用户签署印章使用。
	 * 注意：使用本地模板印章接口时需要依赖ext-sdk-fonts.jar
	 * @param
	 * @return
	 */
	public static String createOrganizeTemplateSeal(String roundText,String hText,String qText) {
		// 印章模板类型：矩形印章
		OrganizeTemplateType type = OrganizeTemplateType.STAR;
		// 印章颜色：红色
		SealColor sealColor = SealColor.RED;


		System.out.println("----开始创建企业账户的印章（本地）...");
		TemplateSealService templateSealService = TemplateSealServiceFactory.instance();
		AddSealResult createOfficialTemplateSeal = templateSealService.createOfficialTemplateSeal(type, roundText, hText, qText, sealColor);
		if (0 != createOfficialTemplateSeal.getErrCode()) {
			LOG.info("创建企业模板印章失败，errCode=" + createOfficialTemplateSeal.getErrCode() + " msg=" + createOfficialTemplateSeal.getMsg());
		} else {
			System.out.println("创建企业模板印章成功！SealData = " + createOfficialTemplateSeal.getSealData());
		}
		return createOfficialTemplateSeal.getSealData();

	}

	/**
	 * 事件证书pdf摘要签署
	 *
	 */
	public static FileDigestSignResult userEventSignByStream(String srcPdfFile, String certId,
			String sealData){

		//String srcPdfFile;
		// 设置文件流签署的PDF文档信息
		SignPDFStreamBean signPDFStreamBean = setSignPDFStreamBean(FileHelper.getBytes(srcPdfFile));
		// 设置坐标定位签署的PosBean，坐标定位方式支持单页签章、多页签章和骑缝章，但对关键字签章指定页码无效；
		PosBean posBean = setXYPosBean("1",110,250);
		// 设置签署类型为 关键字签章
		SignType signType = SignType.Single;

		System.out.println("----开始事件证书文件流的PDF摘要签署...");
		EventSignService eventSignService = EventSignServiceFactory.instance();
		FileDigestSignResult fileDigestSignResult = eventSignService.localSignPDFByEvent(certId, sealData, signPDFStreamBean,
				posBean, signType);
		if (0 != fileDigestSignResult.getErrCode()) {
			LOG.info("事件证书文件流的PDF摘要签署失败，errCode=" + fileDigestSignResult.getErrCode() + " msg="
					+ fileDigestSignResult.getMsg());
		} else {
			System.out.println("事件证书文件流的PDF摘要签署成功！签署记录SignServiceId = " + fileDigestSignResult.getSignServiceId() + ", 请保存该签署记录Id！");
		}
		return fileDigestSignResult;
	}
	/**
	 * 本地签署 平台自身签署
	 *
	 * @param srcPdfFile
	 * @param signedPdfFile
	 * @return
	 */

	public static FileDigestSignResult platformSignByFile1(String srcPdfFile,String signedPdfFile) {
		// 设置文件签署的PDF文档信息
		SignPDFFileBean signPDFFileBean = setSignPDFFileBean(srcPdfFile,signedPdfFile);
		// 设置坐标定位签署的PosBean，坐标定位方式支持单页签章、多页签章和骑缝章，但对关键字签章指定页码无效；
		PosBean posBean = setXYPosBean("1",170,714);
		// 设置签署类型为 单页签章，坐标定位方式支持单页签章、多页签章和骑缝章
		SignType signType = SignType.Single;
		// 设置签署印章，www.tsign.cn官网设置的默认签名sealId = 0
		int sealId = 0;

		System.out.println("----开始平台自身PDF摘要签署...");
		SelfSignService selfSignService = SelfSignServiceFactory.instance();
		FileDigestSignResult fileDigestSignResult = selfSignService.localSignPdf(signPDFFileBean, posBean, sealId,
				signType);
		if (0 != fileDigestSignResult.getErrCode()) {
			LOG.info("平台自身PDF摘要签署（文件）失败，errCode=" + fileDigestSignResult.getErrCode() + " msg="
					+ fileDigestSignResult.getMsg());
		} else {
			System.out.println("----平台自身PDF摘要签署成功！SignServiceId = " + fileDigestSignResult.getSignServiceId());
			System.out.println("----平台自身PDF摘要签署成功！SignServiceId = " + fileDigestSignResult.getStream());
		}
		return fileDigestSignResult;
	}

	/**
	 * 	pdf模版生成pdf文件
	 * 说明：从开放平台获取模版ID,以及需要的模版参数。调用方法返回合成后的模版的下载地址。
	 */
	public static GeneratePdfResult generatePdfBy(String templateId){
		//模板id
		String Id = templateId;
		//模板中包含待填充文本域时，文本域Key-Value组合
		Map<String, Object> txtFields = getTxtFields();
		DocService ts = DocServiceFactory.instance();
		GeneratePdfResult fcftr = ts.generatePdfByTemplate(Id,txtFields);
		if(0!=fcftr.getErrCode()){
			System.out.println("pdf模版生成pdf文件失败，errCode=" + fcftr.getErrCode() + " msg=" + fcftr.getMsg());
		}else{
			System.out.println("pdf模版生成pdf文件成功！dstPdfFile="+fcftr.getDownUrl());
		}
		return fcftr;
	}

	/**
	 * Map<String,Object> txtFields  封装
	 * 模板中包含待填充文本域时，文本域Key-Value组合
	 * */
	public static Map<String,Object> getTxtFields(){
		Map<String, Object> txtFields = new HashMap<String, Object>();
		txtFields.put("name", "曾安怀");
		txtFields.put("address", "1991");
		txtFields.put("authName", "10");
		txtFields.put("authTime", "8");

		return txtFields;
	}

	/**
	 * SignatureSettings 封装
	 * */
	public static Map<String,SignatureSettings> getSignatureSettings(){
		String signatureFieldName1 = "aSgin";
		String signatureFieldName2 = "bSgin";
		// 创建个人客户账户
		String userPersonAccountId = addPersonAccount();
		// 创建企业客户账号
		String userOrganizeAccountId = addOrganizeAccount();
		// 创建个人印章
		AddSealResult userPersonSealData = addPersonTemplateSeal(userPersonAccountId);
		// 创建企业印章
		AddSealResult userOrganizeSealData = addOrganizeTemplateSeal(userOrganizeAccountId);
		Map<String,SignatureSettings> signatures = new HashMap<String,SignatureSettings>();
		//个人用户待签名信息
		SignatureSettings signatureSettings1 = new SignatureSettings();
		signatureSettings1.setAccountId(userPersonAccountId);
		signatureSettings1.setSealData(userPersonSealData.getSealData());
		signatureSettings1.setColor(SealColor.RED);//印章为红色
		//signatureSettings1.setTemplateType(OrganizeTemplateType.STAR);
		//企业用户待签名信息
		SignatureSettings signatureSettings2 = new SignatureSettings();
		signatureSettings2.setAccountId(userOrganizeAccountId);
		signatureSettings2.setSealData(userOrganizeSealData.getSealData());
		signatures.put(signatureFieldName1,signatureSettings1);
		signatures.put(signatureFieldName2,signatureSettings2);
	    return signatures;
	}

	public static SignPDFFileBean getSignPDFFileBean_sign(){
		// 待签署的PDF文件路径
		String srcPdfFile = "D:/下载/3325476101/FileRecv/A.123打开.pdf";
		// 最终签署后的PDF文件路径
		String dstPdfFile = "D:/456a.pdf";
		SignPDFFileBean signPDFFileBean = new SignPDFFileBean();
		String fileName = "";
		String ownerPassword = "";
		signPDFFileBean.setSrcPdfFile(srcPdfFile);
		signPDFFileBean.setDstPdfFile(dstPdfFile);
		signPDFFileBean.setFileName(fileName);
		signPDFFileBean.setOwnerPassword(ownerPassword);
		return signPDFFileBean;
	}


	/**
	 * 预置多个签名域Pdf模板批量签署-文件路径形式签署
	 * @param
	 * @param
	 * @param
	 **/
	public static void doSignatureSign_filePathMore(){
		//获取输入输出目标文件的对象封装-SignPDFFileBean
		SignPDFFileBean signPDFFileBean = getSignPDFFileBean_sign();
		//获取模板中包含待填充文本域,文本域Key-Value组合
		Map<String, Object> txtFields = getTxtFields();
		//获取指定每个预置签名域签署所使用的账号、印章等信息
		Map<String, SignatureSettings> signatures = getSignatureSettings();
		TemplateSignService tls = TemplateSignServiceFactory.instance();
		FileDigestMultiSignResult fms = tls.mulitiSign(signPDFFileBean, txtFields, signatures);
		System.out.println("----开始预置多个签名域Pdf模板批量签署（文件路径）----");
		if(0==fms.getErrCode()){
	    	Map<String,String> signResult = fms.getSignResult();
	    	System.out.println("预置多个签名域Pdf模板批量签署（文件路径）成功！签署信息="+signResult+";errCode="+fms.getErrCode()+";msg="+fms.getMsg());
	    }else{
	    	System.out.println("预置多个签名域Pdf模板批量签署（文件路径）失败！errCode="+fms.getErrCode()+";msg="+fms.getMsg());
	    }
	}


	/***
	 *
	 * stream pdf文件流
	 * pdf电子验签
	 */
	public static VerifyPdfResult verifyPdfResult(byte[] stream) {
		// 设置文件流签署的PDF文档信息
		System.out.println("----开始PDF电子验签...");
		SignService signService = SignServiceFactory.instance();
		VerifyPdfResult verifyPdfResult = signService.localVerifyPdf(stream);
		if (0 != verifyPdfResult.getErrCode()) {
			LOG.info("电子验签失败，errCode=" + verifyPdfResult.getErrCode() + " msg="+verifyPdfResult.getMsg());
		} else {
			System.out.println("----电子验签成功= " + verifyPdfResult.getMsg());
		}
		return verifyPdfResult;

	}

}
