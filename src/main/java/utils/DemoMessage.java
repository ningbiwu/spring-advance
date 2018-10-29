package utils;

public class DemoMessage {

	/***
	 * 示例Demo重要提示
	 */
	public static void showImportantMessage() {
		System.out.println("1、本Demo是用来阐述API基本使用方法的,仅针对大众场景.供平台方接入参考,特殊情况还请平台方自行调整,确保符合平台方实际业务需求;");
		System.out.println("2、如使用e签宝的电子签名,请将与待存证合同相关的全部签署记录ID（SignServiceId）都传递给接口进行存证关联;");
		System.out.println("3、请将与本条存证有关的用户都进行\"证据链编号关联到指定用户\"操作，以供用户日后方便查询和出证;");
		System.out.println("4、请与e签宝工作人员确认贵司所购买的存证类型,以免对接过程中接口调用错误;");
		System.out.println("- - - - - - - - - - # # # # - - - - - - - - - - ");
	}

	/***
	 * 介绍两种常见的存证场景
	 */
	public static void showHowToUseHelp() {
		System.out.println("两种常见场景描述:");
		System.out.println("【常见场景：一条证据链中仅存证一份文档】");
		System.out.println("假设:合同A是张三和李四签署的租房合同,合同B是王五和天之云公司签署的租房合同");
		System.out.println("存证步骤如下:");
		System.out.println("1、创建一个证据链编号（如C123）;");
		System.out.println("2、获取一个证据点编号（如H123）并上传合同A;");
		System.out.println("3、把证据点H123关联到证据链C123上;");
		System.out.println("4、至此完成张三和李四签署的合同A存证;");
		System.out.println("5、创建一个证据链编号（如C456）;");
		System.out.println("6、上传合同B获取一个证据点编号（如H456）;");
		System.out.println("7、把证据点H456关联到证据链C456上;");
		System.out.println("8、至此完成王五和天之云公司签署的合同B存证;");
		System.out.println("这样就可以实现通过证据链编号C123把合同A显示到存证页面中,通过证据链编号C456把合同B显示到存证页面中;");
		System.out.println("根据实际业务需求关联存证编号到指定用户;");
		System.out.println(" * * * * ");
		System.out.println("【常见场景：一条证据链中存证多份文档】");
		System.out.println("假设:合同C、协议A是张XX和李XXX针对安检仪设备采购签订的两个合同,且贵司认为这两个合同需要和本次安检仪设备采购合同签署有关联,需要存证在一起");
		System.out.println("存证步骤如下:");
		System.out.println("1、创建一个证据链编号（如C789）;");
		System.out.println("2、获取一个证据点编号（如H123）并上传合同C;");
		System.out.println("3、获取一个证据点编号（如H456）并上传协议A;");
		System.out.println("4、把证据点H123和H456关联到证据链C789上;");
		System.out.println("至此完成张XX和李XXX针对安检仪设备采购签订的合同C和协议A的存证;这样就可以实现通过证据链编号C789把合同C和协议A都显示到存证页面中;");
		System.out.println("根据实际业务需求关联存证编号到指定用户;");
		System.out.println("- - - - - - - - - - # # # # - - - - - - - - - - ");
	}
}
