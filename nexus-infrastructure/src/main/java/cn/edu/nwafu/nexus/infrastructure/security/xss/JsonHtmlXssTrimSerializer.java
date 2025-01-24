package cn.edu.nwafu.nexus.infrastructure.security.xss;

import cn.hutool.http.HtmlUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * JsonHtmlXssTrimSerializer 类用于对 JSON 字符串进行反序列化处理。
 * 该类继承自 JsonDeserializer<String>，主要目的是对反序列化的字符串进行一些特殊处理，以防止 XSS 攻击并去除 HTML 标签。
 *
 * @author Huang Z.Y.
 */
public class JsonHtmlXssTrimSerializer extends JsonDeserializer<String> {
    public JsonHtmlXssTrimSerializer() {
        super();
    }

    /**
     * 实现反序列化方法
     * 从 JsonParser 中获取字符串值，若不为空，则使用 HtmlUtil 工具类的 cleanHtmlTag 方法去除 HTML 标签，
     * 最后将处理后的字符串作为结果返回。
     *
     * @param p       JsonParser 对象，用于解析 JSON 数据
     * @param context 反序列化上下文
     * @return 处理后的字符串，若输入为 null 则返回 null
     * @throws IOException 当反序列化过程中出现 I/O 异常时抛出
     */
    @Override
    public String deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String value = p.getValueAsString();
        if (value != null) {
            // 去除掉 html 标签
            return HtmlUtil.cleanHtmlTag(value);
        }
        return null;
    }
    
    /**
     * 指明该反序列化器所处理的类型为 String 类型
     *
     * @return 所处理的类型，即 String.class
     */
    @Override
    public Class<String> handledType() {
        return String.class;
    }
}