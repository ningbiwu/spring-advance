package constant;

/***
 * @Description: 场景式存证_常用配置信息类
 * @Team: 公有云技术支持小组
 * @Author: 天云小生
 * @Date: 2018年04月15日
 */
public class SceneConfig {

	// 项目ID(公共应用ID)-模拟环境,正式环境下贵司将拥有独立的应用ID
	public static final String PROJECT_ID = "1111563517";
	// 项目Secret(公共应用Secret)-模拟环境,正式环境下贵司将拥有独立的应用Secret
	public static final String PROJECT_SECRET = "95439b0863c241c63a861b87d1e647b7";
	// 编码格式
	public static final String ENCODING = "UTF-8";
	// 哈希算法
	public static final String ALGORITHM = "HmacSHA256";

	/***
	 * 模拟环境_HTTP调用地址
	 */
	// API接口调用地址_模拟环境_HTTP
    public static final String API_HOST = "http://smlcunzheng.tsign.cn:8083";
    // 存证证明查看页面Url（模拟环境）,仅有Https一种
    public static final String VIEWPAGE_HOST = "https://smlcunzheng.tsign.cn";

    /***
	 * 模拟环境_HTTPS调用地址
	 */
    /*
	// API接口调用地址_模拟环境_HTTPS
    public static final String API_HOST = "https://smlcunzheng.tsign.cn:9443";
    // 存证证明查看页面Url（模拟环境）,仅有Https一种
    public static final String VIEWPAGE_HOST = "https://smlcunzheng.tsign.cn";
    */

	/***
	 * 正式环境_HTTP调用地址
	 */
    /*
    // API接口调用地址_正式环境_HTTP
    public static final String API_HOST = "http://evislb.tsign.cn:8080";
    // 存证证明查看页面Url（正式环境）,仅有Https一种
    public static final String VIEWPAGE_HOST = "https://eviweb.tsign.cn";
    */

	/***
	 * 正式环境_HTTPS调用地址
	 */
    /*
	// API接口调用地址_正式环境_HTTPS
    public static final String API_HOST = "https://evislb.tsign.cn:443";
    // 存证证明查看页面Url（正式环境）,仅有Https一种
    public static final String VIEWPAGE_HOST = "https://eviweb.tsign.cn";
    */

    // 定义所属行业类型API接口
    public static final String BUS_ADD_API = API_HOST + "/evi-service/evidence/v1/sp/temp/bus/add";
    // 定义业务凭证（名称）API接口
    public static final String SCENE_ADD_API = API_HOST + "/evi-service/evidence/v1/sp/temp/scene/add";
    // 定义业务凭证中某一证据点名称API接口
    public static final String SEG_ADD_API = API_HOST + "/evi-service/evidence/v1/sp/temp/seg/add";
    // 定义业务凭证中某一证据点的字段属性API接口
    public static final String SEGPROP_ADD_API = API_HOST + "/evi-service/evidence/v1/sp/temp/seg-prop/add";
    // 构建证据链API接口
    public static final String VOUCHER_API = API_HOST + "/evi-service/evidence/v1/sp/scene/voucher";
    // 创建原文存证（基础版）证据点API接口
    public static final String ORIGINAL_STANDARD_API = API_HOST + "/evi-service/evidence/v1/sp/segment/original-std/url";
    // 创建原文存证（高级版）证据点API接口
    public static final String ORIGINAL_ADVANCED_API = API_HOST + "/evi-service/evidence/v1/sp/segment/original-adv/url";
    // 创建摘要存证证据点API接口
    public static final String ORIGINAL_DIGEST_API = API_HOST + "/evi-service/evidence/v1/sp/segment/abstract/url";
    // 关联证据点到证据链上API接口
    public static final String VOUCHER_APPEND_API = API_HOST + "/evi-service/evidence/v1/sp/scene/append";
    // 场景式存证编号(证据链编号)关联到指定用户API接口
    public static final String RELATE_API = API_HOST + "/evi-service/evidence/v1/sp/scene/relate";

    // 存证证明查看页面Url
    public static final String VIEWPAGE_URL = VIEWPAGE_HOST + "/evi-web/static/certificate-info.html";

}
