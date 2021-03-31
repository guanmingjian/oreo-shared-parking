package cn.oreo.server.order.configure;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * String转Date 转换器
 * @author GuanMingJian
 * @since 2020/12/14
 */
@Component
public class StringToDateConverter implements Converter<String, Date> {

    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String shortDateFormat = "yyyy-MM-dd";

    @Override
    public Date convert(String value) {

        if (StringUtils.isEmpty(value)) {
            return null;
        }
        value = value.trim();
        try {
            if (value.contains("-")) {
                SimpleDateFormat formatter;
                if (value.contains(":")) {
                    formatter = new SimpleDateFormat(dateFormat);
                }
                else {
                    formatter = new SimpleDateFormat(shortDateFormat);
                }
                Date dtDate = formatter.parse(value);
                return dtDate;
            } else if (value.matches("^\\d+$")) {
                Long lTime = new Long(value);
                return new Date(lTime);
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("parser %s to Date fail", value));
        }
        throw new RuntimeException(String.format("parser %s to Date fail", value));
    }
}
