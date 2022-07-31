package top.vchar.wechat.util;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import top.vchar.wechat.config.BizException;
import top.vchar.wechat.enums.ApiCode;

/**
 * <p> xml 解析工具类 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/17
 */
public class XMLParse {
    /**
     * 提取出xml数据包中的加密消息
     * @param xmltext 待提取的xml字符串
     * @return 提取出的加密消息字符串
     */
    public static Object[] extract(String xmltext) {
        Object[] result = new Object[3];
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            dbf.setXIncludeAware(false);
            dbf.setExpandEntityReferences(false);

            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(xmltext);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList nodeList = root.getElementsByTagName("Encrypt");
            result[0] = 0;
            result[1] = nodeList.item(0).getTextContent();
            return result;
        } catch (Exception e) {
            throw new BizException(ApiCode.ENT_WX_PARSE_XML_ERROR, e);
        }
    }

    /**
     * 生成xml消息
     * @param encrypt 加密后的消息密文
     * @param signature 安全签名
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @return 生成的xml字符串
     */
    public static String generate(String encrypt, String signature, String timestamp, String nonce) {

        String format = "<xml>\n" + "<Encrypt><![CDATA[%1$s]]></Encrypt>\n"
                + "<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n"
                + "<TimeStamp>%3$s</TimeStamp>\n" + "<Nonce><![CDATA[%4$s]]></Nonce>\n" + "</xml>";
        return String.format(format, encrypt, signature, timestamp, nonce);
    }

    /**
     * 获取XML中的值
     * @param root 节点
     * @param key 要获取值得key
     * @return 返回值
     */
    public static String getXmlValue(Element root, String key){
        NodeList nodeList = root.getElementsByTagName(key);
        return nodeList.item(0).getTextContent();
    }
}
