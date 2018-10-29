package ching.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionHMAC {

	/***
	 * 获取HMAC加密后的16进制字符串(Hash Message Authentication Code，散列消息鉴别码)
	 * 
	 * @param data
	 *            加密前数据
	 * @param key
	 *            密钥
	 * @param algorithm
	 *            HmacMD5 HmacSHA1 HmacSHA256 HmacSHA384 HmacSHA512
	 * @param encoding
	 *            编码格式
	 * @return HMAC加密后16进制字符串
	 * @throws Exception
	 */
	public static String getHMACHexString(String data, String key, String algorithm, String encoding) throws Exception {
		return byte2hex(encryptHMACByAlgorithm(data, key, algorithm, encoding));
	}

	/***
	 * HMAC加密(Hash Message Authentication Code，散列消息鉴别码)
	 * 
	 * @param data
	 *            加密前数据
	 * @param key
	 *            密钥
	 * @param algorithm
	 *            HmacMD5 HmacSHA1 HmacSHA256 HmacSHA384 HmacSHA512
	 * @param encoding
	 *            编码格式
	 * @return HMAC加密后数据
	 * @throws Exception
	 */
	public static byte[] encryptHMACByAlgorithm(String data, String key, String algorithm, String encoding)
			throws Exception {
		Mac mac = Mac.getInstance(algorithm);
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(encoding), algorithm);
		mac.init(secretKey);
		mac.update(data.getBytes(encoding));
		return mac.doFinal();
	}

	/***
	 * 将byte[]转成16进制字符串
	 * 
	 * @param data
	 *            HMAC加密后数据
	 * @return 16进制字符串
	 */
	public static String byte2hex(byte[] data) {
		StringBuilder hash = new StringBuilder();
		String stmp;
		for (int n = 0; data != null && n < data.length; n++) {
			stmp = Integer.toHexString(data[n] & 0XFF);
			if (stmp.length() == 1)
				hash.append('0');
			hash.append(stmp);
		}
		return hash.toString();
	}

}
