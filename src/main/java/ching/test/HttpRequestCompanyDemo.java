package ching.test;

import ching.utils.EncryptionHMAC;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpRequestCompanyDemo {


    private static String encoding = "UTF-8";
    private static String algorithm = "HmacSHA256";
    private static String projectId = "1111563517";
    private static String projectSecret = "95439b0863c241c63a861b87d1e647b7";

    public static void main(String[] args) {

        try {
            //企业信息校验-请求
            requestOrganiza();
            //企业打款
            //Remittance();

            String cash = "0.13";// 接收到的金额
            String serviceId = "29a8a7bc-a0cf-4e02-8c5e-78e359dc1d3a";
            //企业打款认证-验证
			      //verifyRemittance(cash,serviceId);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /***
     * 企业信息认证-请求
     * @throws Exception
     */
    public static void requestOrganiza() throws Exception {
        String signature = null;
        String resultJSON = null;
        String urlPath = null;
        // 设置企业信息认证请求参数
        String organizaApply = setOrganizaJSONStr();
        signature = EncryptionHMAC.getHMACHexString(organizaApply, projectSecret, algorithm, encoding);
        System.out.println("企业信息校验-请求开始...");
        // 企业信息校验请求地址（测试环境）
        urlPath = "http://smlrealname.tsign.cn:8080/realname/rest/external/organ/infoAuth";
        //模拟请求
        resultJSON = sendPost(urlPath, signature, organizaApply);
        System.out.println("企业信息校验-请求完成：" + resultJSON);

        /* 至此，已完成企业信息校验请求 */
    }

    /***
     * 企业打款
     * @throws Exception
     */
    public static void Remittance() throws Exception {
        String signature = null;
        String resultJSON = null;
        String urlPath = null;
        // 设置企业打款请求参数
        String remittanceApply = setRemittanceJSONStr();
        signature = EncryptionHMAC.getHMACHexString(remittanceApply, projectSecret, algorithm, encoding);
        System.out.println("企业打款开始...");
        // 企业打款请求地址（测试环境）
        urlPath = "http://smlrealname.tsign.cn:8080/realname/rest/external/organ/toPay";
        //模拟请求
        resultJSON = sendPost(urlPath, signature, remittanceApply);
        System.out.println("企业打款-打款完成：" + resultJSON);

    }

    public static void verifyRemittance(String cash, String serviceId) throws Exception {
        String signature = null;
        String resultJSON = null;
        String urlPath = null;
        System.out.println("企业打款认证-验证开始...");
        // 企业打款认证验证请求地址（测试环境）
        urlPath = "http://smlrealname.tsign.cn:8080/realname/rest/external/organ/payAuth";
        // 企业信息校验请求服务ID,请求成功时返回
        String remittanceVerify = setVerifyRemittanceJSONStr(cash, serviceId);
        signature = EncryptionHMAC.getHMACHexString(remittanceVerify, projectSecret, algorithm, encoding);
        resultJSON = sendPost(urlPath, signature, remittanceVerify);
        System.out.println("企业打款认证-验证完成：" + resultJSON);
        /* 至此，已完成企业打款认证验证*/
    }

    /***
     * 设置企业信息认证请求参数
     *
     * @return
     */
    public static String setOrganizaJSONStr() {
        JSONObject obj = new JSONObject();
        obj.put("name", "杭州端点网络科技有限公司");
        obj.put("codeUSC", "913301080536928210");
        obj.put("legalName", "赵沣伟");
        obj.put("legalIdno", "");

        return obj.toString();
    }
    /**
     * 设置企业打款请求参数
     * @return
     */
    public static String setRemittanceJSONStr() {
        JSONObject obj = new JSONObject();
        obj.put("name", "杭州端点网络科技有限公司");
        obj.put("cardno", "913301080536928210");
        obj.put("subbranch", "招商银行杭州滨江支行");
        obj.put("bank", "招商银行");
        obj.put("provice", "浙江省");
        obj.put("city", "杭州市");
        //接受申请打款成功的异步
        //obj.put("notify", "");
        obj.put("serviceId", "29a8a7bc-a0cf-4e02-8c5e-78e359dc1d3a");
        //列表内银行不必传入大额行号，其他银行需要传入大额行号，备用渠道prcptcd 为必填。
        //	obj.put("prcptcd", "102331000217");
        //自定义参数 会在异步通知中返回
        //obj.put("pizId", "杭州天谷");
        return obj.toString();
    }

    /***
     * 设置企业打款验证参数
     *
     * @param cash
     *            打款金融
     * @param serviceId
     *            企业信息校验服务ID
     * @return
     */
    public static String setVerifyRemittanceJSONStr(String cash, String serviceId) {
        JSONObject obj = new JSONObject();
        obj.put("cash", cash);
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
        for (Map.Entry<String, String> entry : getHeaders(signature, jsonStr).entrySet()) {
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
        }else{
            result = String.valueOf(resultCode);
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
        for (Map.Entry<String, String> entry : getHeaders(signature, jsonStr).entrySet()) {
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
