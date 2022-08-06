package top.vchar.wechat.util;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
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

    public static Element coverXml(String xmlText){
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            dbf.setXIncludeAware(false);
            dbf.setExpandEntityReferences(false);

            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(xmlText);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);
            return document.getDocumentElement();
        }catch (ParserConfigurationException | IOException | SAXException e) {
            throw new BizException(ApiCode.ENT_WX_PARSE_XML_ERROR, e);
        }
    }

    /**
     * 提取出xml数据包中的加密消息
     * @param xmlText 待提取的xml字符串
     * @param key xml的key
     * @return 提取出的加密消息字符串
     */
    public static String extract(String xmlText, String key) {
        Element root = coverXml(xmlText);
        return getXmlValue(root, key);
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
