package org.hswebframework.utils.time;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 日期格式化工具
 *
 * @author zhouhao
 */
public interface DateFormatter {

    List<DateFormatter> supportFormatter = new ArrayList<>(Arrays.asList(
            /*
            *常见格式
            * */
            // yyyyMMdd
            new DefaultDateFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2}"), "yyyyMMdd")
            // yyyy-MM-dd
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}"), "yyyy-MM-dd")
            // yyyy/MM/dd
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9]{2}"), "yyyy/MM/dd")
            //yyyy年MM月dd日
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]{2}日"), "yyyy年MM月dd日")
            //yyyy年M月d日
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]月[0-9]日"), "yyyy年M月d日")
            //yyyy年MM月d日
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]日"), "yyyy年MM月d日")
            //yyyy年M月dd日
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]月[0-9]{2}日"), "yyyy年M月dd日")

            // yyyy-M-d
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9]"), "yyyy-M-d")
            // yyyy-MM-d
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]"), "yyyy-MM-d")
            // yyyy-M-dd
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9]{2}"), "yyyy-M-dd")

            // yyyy/M/d
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9]"), "yyyy/M/d")
            // yyyy/MM/d
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9]"), "yyyy/MM/d")
            // yyyy/M/dd
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9]{2}"), "yyyy/M/dd")

            // yyyy-MM-dd HH:mm:ss
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-MM-dd HH:mm:ss")
            // yyyy-M-d HH:mm:ss
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9] [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-M-d HH:mm:ss")
            // yyyy-M-d H:m:s
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9] [0-9]:[0-9]:[0-9]"), "yyyy-M-d H:m:s")
            // yyyy-MM-d HH:mm:ss
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9] [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-MM-d HH:mm:ss")
            // yyyy-M-dd HH:mm:ss
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-M-dd HH:mm:ss")
            // yyyy-M-dd H:m:s
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]-[0-9]{2} [0-9]:[0-9]:[0-9]"), "yyyy-M-dd H:m:s")
            // yyyy-MM-d H:m:s
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9] [0-9]:[0-9]:[0-9]"), "yyyy-MM-d H:m:s")

            // yyyy/MM/dd HH:mm:ss
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy/MM/dd HH:mm:ss")
            // yyyy/M/d HH:mm:ss
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9] [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy/M/d HH:mm:ss")
            // yyyy/M/d H:m:s
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9] [0-9]:[0-9]:[0-9]"), "yyyy/M/d H:m:s")
            // yyyy/MM/d HH:mm:ss
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9] [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy/MM/d HH:mm:ss")
            // yyyy/M/dd HH:mm:ss
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy/M/dd HH:mm:ss")
            // yyyy/M/dd H:m:s
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]/[0-9]{2} [0-9]:[0-9]:[0-9]"), "yyyy/M/dd H:m:s")
            // yyyy/MM/d H:m:s
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9] [0-9]:[0-9]:[0-9]"), "yyyy/MM/d H:m:s")

            // yyyy-MM-dd HH:mm:ssZ
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\\+[0-9]{4}"), "yyyy-MM-dd HH:mm:ssZ")
            //yyyy-MM-dd'T'HH:mm:ss
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-MM-dd'T'HH:mm:ss")
            //yyyy-MM-dd'T'HH:mm:ssZ
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}\\+[0-9]{4}"), "yyyy-MM-dd'T'HH:mm:ssZ")
            //yyyy年MM月dd日HH时mm分ss秒
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]{2}日[0-9]{2}时[0-9]{2}分[0-9]{2}秒"), "yyyy年MM月dd日HH时mm分ss秒")
            //yyyy年MM月dd日 HH时mm分ss秒
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]{2}日 [0-9]{2}时[0-9]{2}分[0-9]{2}秒"), "yyyy年MM月dd日 HH时mm分ss秒")
            // HH:mm:ss
            , new DefaultDateFormatter(Pattern.compile("[0-9]{2}:[0-9]{2}:[0-9]{2}"), "HH:mm:ss")
            /*
            * 奇奇怪怪的格式
            * */
            // yyyyMMdd HH:mm:dd
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyyMMdd HH:mm:ss")
            // yyyyMMddHHmmdd
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}"), "yyyyMMddHHmmss")
            // yyyyMMdd HHmmdd
            , new DefaultDateFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2} [0-9]{2}[0-9]{2}[0-9]{2}"), "yyyyMMdd HHmmss")
            //yyyy年 MM月 dd日 EEE HH:mm:ss 'CST'
            , new SampleJDKDateFormatter(str -> str.contains("年") && str.contains("CST") && str.split("[ ]").length == 6, () -> new SimpleDateFormat("yyyy年 MM月 dd日 EEE HH:mm:ss 'CST'", Locale.CHINESE))
            //yyyy年 MM月 dd日 EEE HH:mm:ss 'GMT'
            , new SampleJDKDateFormatter(str -> str.contains("年") && str.contains("GMT") && str.split("[ ]").length == 6, () -> new SimpleDateFormat("yyyy年 MM月 dd日 EEE HH:mm:ss 'GMT'", Locale.CHINESE))
            //EEE MMM ddHH:mm:ss 'CST' yyyy
            , new SampleJDKDateFormatter(str -> str.contains("CST") && str.split("[ ]").length == 6, () -> new SimpleDateFormat("EEE MMM dd HH:mm:ss 'CST' yyyy", Locale.US))
            //EEE MMM ddHH:mm:ss 'GMT' yyyy
            , new SampleJDKDateFormatter(str -> str.contains("GMT") && str.split("[ ]").length == 6, () -> new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.US))
            //MMM d, yyyy K:m:s a
            , new SampleJDKDateFormatter(str -> str.endsWith("AM") || str.endsWith("PM") && str.split("[ ]").length == 5, () -> new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH))
    ));

    boolean support(String str);

    Date format(String str);

    String toString(Date date);

    String getPattern();

    static Date fromString(String dateString) {
        DateFormatter formatter = getFormatter(dateString);
        if (formatter != null)
            return formatter.format(dateString);
        return null;
    }

    static Date fromString(String dateString, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String toString(Date date, String format) {
        if (null == date) return null;
        for (DateFormatter formatter : supportFormatter) {
            if (formatter.getPattern().equals(format))
                return formatter.toString(date);
        }
        return new DateTime(date).toString(format);
    }

    static boolean isSupport(String dateString) {
        return !(dateString == null || dateString.length() < 4) && supportFormatter.parallelStream().anyMatch(formatter -> formatter.support(dateString));
    }

    static DateFormatter getFormatter(String dateString) {
        if (dateString == null || dateString.length() < 4) return null;
        for (DateFormatter formatter : supportFormatter) {
            if (formatter.support(dateString))
                return formatter;
        }
        return null;
    }
}
