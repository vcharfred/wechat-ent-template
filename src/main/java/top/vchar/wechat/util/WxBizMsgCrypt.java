package top.vchar.wechat.util;

import lombok.extern.slf4j.Slf4j;
import top.vchar.wechat.bean.CallbackMsg;
import top.vchar.wechat.bean.EntWxSuite;
import top.vchar.wechat.config.BizException;
import top.vchar.wechat.enums.ApiCode;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

/**
 * <p> 企业微信工具包 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/15
 */
@Slf4j
public class WxBizMsgCrypt {

    /**
     * aesKey
     */
    private byte[] aesKey;

    /**
     * 配置的应用token
     */
    private String token;

    /**
     * 接收者
     */
    private String receiveId;

    /**
     * 构造函数
     *
     * @param token 企业微信后台，开发者设置的token
     * @param encodingAesKey 企业微信后台，开发者设置的EncodingAESKey
     * @param receiveId, 不同场景含义不同，详见文档
     */
    public WxBizMsgCrypt(String token, String encodingAesKey, String receiveId){
        this.token = token;
        this.aesKey = Base64.getDecoder().decode(encodingAesKey+"=");
        this.receiveId = receiveId;
    }

    public WxBizMsgCrypt(EntWxSuite entWxSuite, String receiveId){
        this(entWxSuite.getToken(), entWxSuite.getEncodingAesKey(), receiveId);
    }

    /**
     * 生成4个字节的网络字节序
     */
    private byte[] getNetworkBytesOrder(int sourceNumber) {
        byte[] orderBytes = new byte[4];
        orderBytes[3] = (byte) (sourceNumber & 0xFF);
        orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
        orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
        orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
        return orderBytes;
    }

    /**
     * 还原4个字节的网络字节序
     */
    private int recoverNetworkBytesOrder(byte[] orderBytes) {
        int sourceNumber = 0;
        for (int i = 0; i < 4; i++) {
            sourceNumber <<= 8;
            sourceNumber |= orderBytes[i] & 0xff;
        }
        return sourceNumber;
    }

    /**
     * 随机生成16位字符串
     */
    public String getRandomStr() {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 对明文进行加密.
     *
     * @param text 需要加密的明文
     * @return 加密后base64编码的字符串
     */
    public String encrypt(String randomStr, String text) {
        ByteGroup byteCollector = new ByteGroup();
        byte[] randomStrBytes = randomStr.getBytes(StandardCharsets.UTF_8);
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        byte[] networkBytesOrder = getNetworkBytesOrder(textBytes.length);
        byte[] receiveidBytes = receiveId.getBytes(StandardCharsets.UTF_8);

        // randomStr + networkBytesOrder + text + receiveid
        byteCollector.addBytes(randomStrBytes);
        byteCollector.addBytes(networkBytesOrder);
        byteCollector.addBytes(textBytes);
        byteCollector.addBytes(receiveidBytes);

        // ... + pad: 使用自定义的填充方式对明文进行补位填充
        byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
        byteCollector.addBytes(padBytes);

        // 获得最终的字节流, 未加密
        byte[] unencrypted = byteCollector.toBytes();

        try {
            // 设置加密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

            // 加密
            byte[] encrypted = cipher.doFinal(unencrypted);

            // 使用BASE64对加密后的字符串进行编码
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new BizException(ApiCode.ENT_WX_ENCRYPT_AES_ERROR, e);
        }
    }

    /**
     * 对密文进行解密.
     *
     * @param text 需要解密的密文
     * @return 解密得到的明文
     */
    public String decrypt(String text){
        byte[] original;
        try {
            // 设置解密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

            // 使用BASE64对密文进行解码
            byte[] encrypted = Base64.getDecoder().decode(text);

            // 解密
            original = cipher.doFinal(encrypted);
        } catch (Exception e) {
            throw new BizException(ApiCode.ENT_WX_DECRYPT_AES_ERROR, e);
        }

        String xmlContent, fromReceiveId;
        try {
            // 去除补位字符
            byte[] bytes = PKCS7Encoder.decode(original);

            // 分离16位随机字符串,网络字节序和receiveid
            byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

            int xmlLength = recoverNetworkBytesOrder(networkOrder);

            xmlContent = new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), StandardCharsets.UTF_8);
            fromReceiveId = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new BizException(ApiCode.ENT_WX_ILLEGAL_BUFFER, e);
        }

        // receiveId不相同的情况
        if (!fromReceiveId.equals(receiveId)) {
            log.error("企业微信接收者corpId校验失败：需要的为：{}, 密文解密后的: {}，解密后报文:{}", receiveId, fromReceiveId, xmlContent);
            throw new BizException(ApiCode.ENT_WX_VALIDATE_CORPID_ERROR);
        }
        return xmlContent;

    }

    /**
     * 将企业微信回复用户的消息加密打包.
     * <ol>
     * 	<li>对要发送的消息进行AES-CBC加密</li>
     * 	<li>生成安全签名</li>
     * 	<li>将消息密文和安全签名打包成xml格式</li>
     * </ol>
     *
     * @param replyMsg 企业微信待回复用户的消息，xml格式的字符串
     * @param timeStamp 时间戳，可以自己生成，也可以用URL参数的timestamp
     * @param nonce 随机串，可以自己生成，也可以用URL参数的nonce
     *
     * @return 加密后的可以直接回复用户的密文，包括msg_signature, timestamp, nonce, encrypt的xml格式的字符串
     */
    public String encryptMsg(String replyMsg, String timeStamp, String nonce) {
        // 加密
        String encrypt = encrypt(getRandomStr(), replyMsg);

        // 生成安全签名
        if (Objects.equals(timeStamp, "")) {
            timeStamp = Long.toString(System.currentTimeMillis());
        }

        String signature = getSHA1(token, timeStamp, nonce, encrypt);
        // 生成发送的xml
        return XMLParse.generate(encrypt, signature, timeStamp, nonce);
    }

    /**
     * 检验消息的真实性，并且获取解密后的明文.
     * <ol>
     * 	<li>利用收到的密文生成安全签名，进行签名验证</li>
     * 	<li>若验证通过，则提取xml中的加密消息</li>
     * 	<li>对消息进行解密</li>
     * </ol>
     *
     * @param msgSignature 签名串，对应URL参数的msg_signature
     * @param timeStamp 时间戳，对应URL参数的timestamp
     * @param nonce 随机串，对应URL参数的nonce
     * @param postData 密文，对应POST请求的数据
     *
     * @return 解密后的原文
     */
    public String decryptMsg(String msgSignature, String timeStamp, String nonce, String postData) {

        // 密钥，公众账号的app secret
        // 提取密文
        Object[] encrypt = XMLParse.extract(postData);

        // 验证安全签名
        String signature = getSHA1(token, timeStamp, nonce, encrypt[1].toString());

        // 和URL中的签名比较是否相等
        if (!signature.equals(msgSignature)) {
            throw new BizException(ApiCode.ENT_WX_VALIDATE_SIGNATURE_ERROR);
        }

        // 解密
        return decrypt(encrypt[1].toString());
    }

    /**
     * 检验消息的真实性，并且获取解密后的明文.
     *
     * @param callbackMsg 密文信息
     * @return 返回明文
     */
    public String decryptMsg(CallbackMsg callbackMsg){
        return decryptMsg(callbackMsg.getMsgSignature(), callbackMsg.getTimestamp()
                , callbackMsg.getNonce(), callbackMsg.getBody());
    }

    /**
     * 验证URL
     *
     * @param msgSignature 签名串，对应URL参数的msg_signature
     * @param timeStamp 时间戳，对应URL参数的timestamp
     * @param nonce 随机串，对应URL参数的nonce
     * @param echoStr 随机串，对应URL参数的echostr
     * @return 解密之后的echostr
     */
    public String verifyUrl(String msgSignature, String timeStamp, String nonce, String echoStr) {
        String signature = getSHA1(token, timeStamp, nonce, echoStr);

        if (!signature.equals(msgSignature)) {
            throw new BizException(ApiCode.ENT_WX_VALIDATE_SIGNATURE_ERROR);
        }

        return decrypt(echoStr);
    }

    /**
     * 用SHA1算法生成安全签名
     * @param token 票据
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @param encrypt 密文
     * @return 安全签名
     */
    public static String getSHA1(String token, String timestamp, String nonce, String encrypt) {
        try {
            String[] array = new String[] { token, timestamp, nonce, encrypt };
            StringBuilder sb = new StringBuilder();
            // 字符串排序
            Arrays.sort(array);
            for (int i = 0; i < 4; i++) {
                sb.append(array[i]);
            }
            String str = sb.toString();
            // SHA1签名生成
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuilder hexStr = new StringBuilder();
            String shaHex = "";
            for (byte b : digest) {
                shaHex = Integer.toHexString(b & 0xFF);
                if (shaHex.length() < 2) {
                    hexStr.append(0);
                }
                hexStr.append(shaHex);
            }
            return hexStr.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(ApiCode.ENT_WX_COMPUTE_SIGNATURE_ERROR);
        }
    }

    static class ByteGroup {
        List<Byte> byteContainer = new ArrayList<>();
        public byte[] toBytes() {
            byte[] bytes = new byte[byteContainer.size()];
            for (int i = 0; i < byteContainer.size(); i++) {
                bytes[i] = byteContainer.get(i);
            }
            return bytes;
        }

        public ByteGroup addBytes(byte[] bytes) {
            for (byte b : bytes) {
                byteContainer.add(b);
            }
            return this;
        }

        public int size() {
            return byteContainer.size();
        }
    }
}
