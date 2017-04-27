package org.hsweb.commons.time;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 日期格式化工具
 *
 * @author zhouhao
 */
public interface DateFormatter {

    List<DateFormatter> supportFormatter = Arrays.asList(
            /*
            *常见格式
            * */
            // yyyyMMdd
            new DefaultDateFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2}"), DateTimeFormat.forPattern("yyyyMMdd"))
            // yyyy-MM-dd
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}"), DateTimeFormat.forPattern("yyyy-MM-dd"))
            // yyyy/MM/dd
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9]{2}"), DateTimeFormat.forPattern("yyyy/MM/dd"))
            //yyyy年MM月dd日
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]{2}日"), DateTimeFormat.forPattern("yyyy年MM月dd日"))
            // yyyy-MM-dd HH:mm:ss
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))
            // yyyy-MM-dd HH:mm:ssZ
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\\+[0-9]{4}"), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ssZ"))
            //yyyy-MM-dd'T'HH:mm:ss
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}"), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss"))
            //yyyy-MM-dd'T'HH:mm:ssZ
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}\\+[0-9]{4}"), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ"))
            //yyyy年MM月dd日HH时mm分ss秒
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]{2}日[0-9]{2}时[0-9]{2}分[0-9]{2}秒"), DateTimeFormat.forPattern("yyyy年MM月dd日HH时mm分ss秒"))
            //yyyy年MM月dd日 HH时mm分ss秒
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]{2}日 [0-9]{2}时[0-9]{2}分[0-9]{2}秒"), DateTimeFormat.forPattern("yyyy年MM月dd日 HH时mm分ss秒"))

            /*
            * 奇奇怪怪的格式
            * */
            // yyyyMMdd HH:mm:dd
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss"))
            // yyyyMMddHHmmdd
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}"), DateTimeFormat.forPattern("yyyyMMddHHmmdd"))
            // yyyyMMdd HHmmdd
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2} [0-9]{2}[0-9]{2}[0-9]{2}"), DateTimeFormat.forPattern("yyyyMMdd HHmmdd"))
            //yyyy年 MM月 dd日 EEE HH:mm:ss 'CST'
            , new SampleJDKDateFormatter(str -> str.contains("年") && str.contains("CST") && str.split("[ ]").length == 6, () -> new SimpleDateFormat("yyyy年 MM月 dd日 EEE HH:mm:ss 'CST'", Locale.CHINESE))
            //yyyy年 MM月 dd日 EEE HH:mm:ss 'GMT'
            , new SampleJDKDateFormatter(str -> str.contains("年") && str.contains("GMT") && str.split("[ ]").length == 6, () -> new SimpleDateFormat("yyyy年 MM月 dd日 EEE HH:mm:ss 'GMT'", Locale.CHINESE))
            //EEE MMM ddHH:mm:ss 'CST' yyyy
            , new SampleJDKDateFormatter(str -> str.contains("CST") && str.split("[ ]").length == 6, () -> new SimpleDateFormat("EEE MMM ddHH:mm:ss 'CST' yyyy", Locale.US))
            //EEE MMM ddHH:mm:ss 'GMT' yyyy
            , new SampleJDKDateFormatter(str -> str.contains("GMT") && str.split("[ ]").length == 6, () -> new SimpleDateFormat("EEE MMM ddHH:mm:ss 'GMT' yyyy", Locale.US))
            //MMM d, yyyy K:m:s a
            , new SampleJDKDateFormatter(str -> str.endsWith("AM") || str.endsWith("PM") && str.split("[ ]").length == 5, () -> new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH))
    );

    boolean support(String str);

    Date format(String str);

    static Date fromString(String dateString) {
        if (dateString == null || dateString.length() < 4) return null;
        for (DateFormatter formatter : supportFormatter) {
            if (formatter.support(dateString))
                return formatter.format(dateString);
        }
        return null;
    }

    static String toString(Date date, String format) {
        if (null == date) return null;
        return new DateTime(date).toString(format);
    }

    static boolean isSupport(String dateString) {
        if (dateString == null || dateString.length() < 4) return false;
        return supportFormatter.parallelStream().anyMatch(formatter -> formatter.support(dateString));
    }

}
