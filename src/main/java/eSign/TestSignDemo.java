package eSign;

import com.timevale.esign.sdk.tech.bean.result.AddSealResult;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
import java.io.File;
import utils.FileHelper;

/***
 * e签宝快捷签-电子签名Demo
 *
 * @author Ching
 *
 */
public class TestSignDemo {

	public static void main(String[] args) {

		// 待签署的PDF文件路径
		String srcPdfFile = "src/main/resources/pdf" + File.separator + "test.pdf";
		// 最终签署后的PDF文件路径
		String signedFolder = "src/main/resources/pdf" + File.separator;
		// 最终签署后PDF文件名称
		String signedFileName = "test16.pdf";

		// 初始化项目，做全局使用，只初始化一次即可
		SignHelper.initProject();

		// 应用场景演示
		int scene = 0;
		switch (scene) {
		case 0:
			System.out.println("----<场景演示：个人和企业 使用标准的模板印章签署，签署人之间用文件二进制流传递>----");
			// 使用标准的模板印章签署
			doSignWithTemplateSealByStream(srcPdfFile, signedFolder, signedFileName);
			break;
		case 1:
			System.out.println("----<场景演示：使用上传的印章图片签署，签署人之间用文件二进制流传递>----");
			// 使用上传的印章图片签署
			doSignWithImageSealByStream(srcPdfFile, signedFolder, signedFileName);
			break;
		case 2:
			System.out.println("----<场景演示：修改个人手机号>----");
			// 更新个人账户信息，如修改手机号,需要accountId
			SignHelper.updatePersonAccount("0781C4E21BEB473DB36463F3D0C1A175", "18181991872");
			break;
		case 3:
			System.out.println("----<场景演示：修改企业手机号>----");
			// 更新企业账户信息，如修改手机号,需要accountId
			SignHelper.updateOrganizeAccount("328E2B6B41834F019AF2956CC6A30E9E", "13024779865");
			break;
		case 4:
			System.out.println("----<场景演示：使用标准的模板印章签署，签署人之间用文件传递>----");
			// 原始待签署文档
			String srcPdf = "src/main/resources/pdf" + File.separator + "test.pdf";
			// 贵公司签署后文档
			String platformSignedPdf = "src/main/resources/pdf" + File.separator + "SignedByPlatform.pdf";
			// 贵公司个人用户签署后文档
			String personSignedPdf = "src/main/resources/pdf" + File.separator + "SignedByPerson.pdf";
			// 使用标准的模板印章签署，签署人之间用文件传递
			doSignWithTemplateSealByFile(srcPdf,srcPdf,srcPdf);
			break;
		case 5:
			System.out.println("----<场景演示：企业与企业之间使用标准的模板印章签署，签署人之间用文件二进制流传递>----");
			doSignWithTemplateSealByStreamCompanyies(srcPdfFile, signedFolder, signedFileName);
			break;
		case 6:
			System.out.println("----<电子验签开始>----");
			doVerifyPdf(srcPdfFile);
			break;
		case 7:
			System.out.println("----<本地签署开始>----");
			signLocalPdfByFile(srcPdfFile, signedFolder, signedFileName);
			break;
		default:
			System.out.println("---- 提示！请选择应用场景...");
			break;
		}

	}
	/***
	 * 本地签署文件
	 *
	 * @param srcPdfFile
	 * @param signedFolder
	 * @param signedFileName
	 */
	public static void signLocalPdfByFile(String srcPdfFile, String signedFolder, String signedFileName) {
		// 创建个人客户账户
		String userPersonAccountId = SignHelper.addPersonAccount();
		// 创建企业客户账号
		String userOrganizeAccountId = SignHelper.addOrganizeAccount();
		// 创建个人印章
		AddSealResult userPersonSealData = SignHelper.addPersonTemplateSeal(userPersonAccountId);
		// 创建企业印章
		AddSealResult userOrganizeSealData = SignHelper.addOrganizeTemplateSeal(userOrganizeAccountId);

		// 个人客户签署，签署方式：关键字定位,以文件流的方式传递pdf文档
		FileDigestSignResult userPersonSignResult = SignHelper.userPersonSignByFile(srcPdfFile,
				signedFileName, userPersonAccountId,userPersonSealData.getSealData());

//		// 企业客户签署,坐标定位,以文件流的方式传递pdf文档
//		FileDigestSignResult userOrganizeSignResult = SignHelper.userOrganizeSignByStream(
//				userPersonSignResult.getStream(), userOrganizeAccountId, userOrganizeSealData.getSealData(),240,120);
//
//		// 所有签署完成,将最终签署后的文件流保存到本地
//		if (0 == userOrganizeSignResult.getErrCode()) {
//			SignHelper.saveSignedByStream(userOrganizeSignResult.getStream(), signedFolder, signedFileName);
//		}
	}

	/***
	 * 电子验签
	 * @param srcPdfFile
	 */
	public static void doVerifyPdf (String srcPdfFile) {

		byte[] stream = FileHelper.getBytes(srcPdfFile);
		SignHelper.verifyPdfResult(stream);

	}
	/***
	 * 签署人之间用文件二进制流传递,标准模板印章签署，所用印章SealData为addTemplateSeal接口创建的模板印章返回的SealData
	 * 企业与企业之间进行签署
	 * @param srcPdfFile
	 * @param signedFolder
	 * @param signedFileName
	 */
	public static void doSignWithTemplateSealByStreamCompanyies(String srcPdfFile, String signedFolder, String signedFileName) {

		// 创建企业客户账号1
		String company1 = SignHelper.addOrganizeAccount();

		// 创建企业客户账号2
		String  company2 =SignHelper.addOrganizeAccount();
		// 创建企业印章1
		AddSealResult userOrganizeSealData1 = SignHelper.addOrganizeTemplateSeal(company1);

		// 创建企业印章2
		AddSealResult userOrganizeSealData2 = SignHelper.addOrganizeOtherTemplateSeal(company2);


		byte[] stream = FileHelper.getBytes(srcPdfFile);

		// 企业客户1签署,坐标定位,以文件流的方式传递pdf文档
		FileDigestSignResult userOrganizeSignResult1 = SignHelper.userOrganizeSignByStream(
				stream, company1, userOrganizeSealData1.getSealData(),120,120);

		// 企业客户2签署,坐标定位,以文件流的方式传递pdf文档
		FileDigestSignResult userOrganizeSignResult2 = SignHelper.userOrganizeSignByStream(
				userOrganizeSignResult1.getStream(), company2, userOrganizeSealData2.getSealData(),240,120);

		// 所有签署完成,将最终签署后的文件流保存到本地
		if (0 == userOrganizeSignResult2.getErrCode()) {
			SignHelper.saveSignedByStream(userOrganizeSignResult2.getStream(), signedFolder, signedFileName);
		}
	}

	/***
	 * 签署人之间用文件二进制流传递,标准模板印章签署，所用印章SealData为addTemplateSeal接口创建的模板印章返回的SealData
	 *
	 * @param srcPdfFile
	 * @param signedFolder
	 * @param signedFileName
	 */
	public static void doSignWithTemplateSealByStream(String srcPdfFile, String signedFolder, String signedFileName) {
		// 创建个人客户账户
		String userPersonAccountId = SignHelper.addPersonAccount();
		// 创建企业客户账号
		String userOrganizeAccountId = SignHelper.addOrganizeAccount();
		// 创建个人印章
		AddSealResult userPersonSealData = SignHelper.addPersonTemplateSeal(userPersonAccountId);
		// 创建企业印章
		AddSealResult userOrganizeSealData = SignHelper.addOrganizeTemplateSeal(userOrganizeAccountId);
	//	String SealData ="pdf" + File.separator + "sealdata.txt";
		//FileHelper.readSealData(SealData, "utf-8")
		//userPersonSealData.getSealData()

		// 贵公司签署，签署方式：坐标定位,以文件流的方式传递pdf文档
		//FileDigestSignResult platformSignResult = SignHelper.platformSignByStreamm(srcPdfFile);
		//得文件字节流
		byte[] stream = FileHelper.getBytes(srcPdfFile);

		// 个人客户签署，签署方式：关键字定位,以文件流的方式传递pdf文档
		FileDigestSignResult userPersonSignResult = SignHelper.userPersonSignByStream(stream,
				userPersonAccountId, userPersonSealData.getSealData(),120,120);

		// 企业客户签署,坐标定位,以文件流的方式传递pdf文档
		FileDigestSignResult userOrganizeSignResult = SignHelper.userOrganizeSignByStream(
				userPersonSignResult.getStream(), userOrganizeAccountId, userOrganizeSealData.getSealData(),240,120);

		// 所有签署完成,将最终签署后的文件流保存到本地
		if (0 == userOrganizeSignResult.getErrCode()) {
			SignHelper.saveSignedByStream(userOrganizeSignResult.getStream(), signedFolder, signedFileName);
		}
	}

	/***
	 * 签署人之间用文件传递,标准模板印章签署，所用印章SealData为addTemplateSeal接口创建的模板印章返回的SealData
	 *
	 * @param   srcPdf
	 * @param
	 * @param
	 */
	public static void doSignWithTemplateSealByFile(String srcPdf, String platformSignedPdf, String personSignedPdf) {
		// 创建个人客户账户
		String userPersonAccountId = SignHelper.addPersonAccount();

		// 创建个人印章
		AddSealResult userPersonSealData = SignHelper.addPersonTemplateSeal(userPersonAccountId);

		// 贵公司签署，签署方式：坐标定位,以文件的方式传递pdf文档
		SignHelper.platformSignByFile(srcPdf, platformSignedPdf);

		// 个人客户签署，签署方式：关键字定位,以文件的方式传递pdf文档
		SignHelper.userPersonSignByFile(platformSignedPdf, personSignedPdf, userPersonAccountId, userPersonSealData.getSealData());
	}

	/***
	 * 上传印章图片签署，所用印章SealData为印章图片的Base64数据
	 *
	 * @param srcPdfFile
	 * @param signedFolder
	 * @param signedFileName
	 */
	public static void doSignWithImageSealByStream(String srcPdfFile, String signedFolder, String signedFileName) {
		// 个人印章图片文件路径
		String personImgFilePath = "src/main/resources/images" + File.separator + "PersonSeal.png";
		// 企业印章图片文件路径
		String organizeImgFilePath = "src/main/resources/images" + File.separator + "OrganizeSeal.png";
		// 创建个人客户账户
		String userPersonAccountId = SignHelper.addPersonAccount();
		// 创建企业客户账号
		String userOrganizeAccountId = SignHelper.addOrganizeAccount();
		// 通过上传的印章图片获取个人印章数据
		String personSealData = SignHelper.getSealDataByImage(personImgFilePath);
		// 通过上传的印章图片获取企业印章数据
		String organizeSealData = SignHelper.getSealDataByImage(organizeImgFilePath);

		// 贵公司签署，签署方式：坐标定位,以文件流的方式传递pdf文档
		FileDigestSignResult platformSignResult = SignHelper.platformSignByStreamm(srcPdfFile);

		// 个人客户签署，签署方式：关键字定位,以文件流的方式传递pdf文档
		FileDigestSignResult userPersonSignResult = SignHelper.userPersonSignByStream(platformSignResult.getStream(),
				userPersonAccountId, personSealData,120,120);

		// 企业客户签署,坐标定位,以文件流的方式传递pdf文档
		FileDigestSignResult userOrganizeSignResult = SignHelper
				.userOrganizeSignByStream(userPersonSignResult.getStream(), userOrganizeAccountId, organizeSealData,240,120);

		// 所有签署完成,将最终签署后的文件流保存到本地
		if (0 == userOrganizeSignResult.getErrCode()) {
			SignHelper.saveSignedByStream(userOrganizeSignResult.getStream(), signedFolder, signedFileName);
		}
	}

	public static void toDoList() {
		// 注销个人或企业账户,需要accountId
		// SignHelper.deleteAccount("");
		// 更新个人账户信息，如修改手机号,需要accountId
		SignHelper.updatePersonAccount("", "");
	}

}
