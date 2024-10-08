package org.hswebframework.utils.time;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hswebframework.utils.time.SmartDateFormatter.*;

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
            of(yyyy, MM, dd)
            // yyyy-MM-dd
            , of(yyyy, strike, MM, strike, dd)
            // yyyy/MM/dd
            , of(yyyy, slash, MM, slash, dd)
            //yyyy年MM月dd日
            , of(yyyy, year_cn, MM, month_cn, dd, day_cn)
            // yyyy-MM-dd HH:mm:ss
            , of(yyyy, strike, MM, strike, dd, blankSpace, HH, colon, mm, colon, ss)
            , of(yyyy, strike, MM, strike, dd, blankSpace, HH, colon, mm, colon, ss, str("."), SSS)
            // yyyy/MM/dd HH:mm:ss
            , of(yyyy, slash, MM, slash, dd, blankSpace, HH, colon, mm, colon, ss)
            // yyyyMMddHHmmss
            , of(yyyy, MM, dd, HH, mm, ss)
            //yyyy年M月d日
            , of(yyyy, year_cn, M, month_cn, d, day_cn)
            //yyyy年MM月d日
            , of(yyyy, year_cn, MM, month_cn, d, day_cn)
            //yyyy年M月dd日
            , of(yyyy, year_cn, M, month_cn, dd, day_cn)

            // yyyy-M-d
            , of(yyyy, strike, M, strike, d)
            // yyyy-MM-d
            , of(yyyy, strike, MM, strike, d)
            // yyyy-M-dd
            , of(yyyy, strike, M, strike, dd)

            // yyyy/M/d
            , of(yyyy, slash, M, slash, d)
            // yyyy/MM/d
            , of(yyyy, slash, MM, slash, d)
            // yyyy/M/dd
            , of(yyyy, slash, M, slash, dd)


            // yyyy-MM-dd HH:mm:ssZ
            , of("yyyy-MM-dd HH:mm:ssZ", yyyy, strike, MM, strike, dd, blankSpace, HH, colon, mm, colon, ss, plus, Z)
            , of("yyyy-MM-dd HH:mm:ss.SSSZ", yyyy, strike, MM, strike, dd, blankSpace, HH, colon, mm, colon, ss, str("."), SSS, plus, Z)
            , of("yyyy-MM-dd HH:mm:ss.SSSXXX", yyyy, strike, MM, strike, dd, blankSpace, HH, colon, mm, colon, ss, str("."), SSS, XXX)
            //yyyy-MM-dd'T'HH:mm:ss
            , of("yyyy-MM-dd'T'HH:mm:ss", yyyy, strike, MM, strike, dd, T, HH, colon, mm, colon, ss)
            //yyyy-MM-dd'T'HH:mm:ssZ
            , of("yyyy-MM-dd'T'HH:mm:ssZ", yyyy, strike, MM, strike, dd, T, HH, colon, mm, colon, ss, plus, Z)
            , of("yyyy-MM-dd'T'HH:mm:ssXXX", yyyy, strike, MM, strike, dd, T, HH, colon, mm, colon, ss, plus, XXX)
            //yyyy-MM-dd'T'HH:mm:ss.SSS
            , of("yyyy-MM-dd'T'HH:mm:ss.SSS", yyyy, strike, MM, strike, dd, T, HH, colon, mm, colon, ss, str("."), SSS)
            , of("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", yyyy, strike, MM, strike, dd, T, HH, colon, mm, colon, ss, str("."), SSS, SSS)
            , of("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS", yyyy, strike, MM, strike, dd, T, HH, colon, mm, colon, ss, str("."), SSS, SSS, SSS)

            //yyyy-MM-dd'T'HH:mm:ss.SSSz
            , of("yyyy-MM-dd'T'HH:mm:ss.SSSZ", yyyy, strike, MM, strike, dd, T, HH, colon, mm, colon, ss, str("."), SSS, plus, Z)
            , of("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", yyyy, strike, MM, strike, dd, T, HH, colon, mm, colon, ss, str("."), SSS, XXX)
            //yyyy年MM月dd日HH时mm分ss秒
            , of(yyyy, year_cn, MM, month_cn, dd, day_cn, HH, hour_cn, mm, minute_cn, ss, second_cn)
            //yyyy年MM月dd日 HH时mm分ss秒
            , of(yyyy, year_cn, MM, month_cn, dd, day_cn, blankSpace, HH, hour_cn, mm, minute_cn, ss, second_cn)
            // HH:mm:ss
            , of(HH, colon, mm, colon, ss)

            // yyyy-M-dd HH:mm:ss
            , of(yyyy, strike, M, strike, dd, blankSpace, HH, colon, mm, colon, ss)
            // yyyy-M-d HH:mm:ss
            , of(yyyy, strike, M, strike, d, blankSpace, HH, colon, mm, colon, ss)
            // yyyy-MM-d HH:mm:ss
            , of(yyyy, strike, MM, strike, d, blankSpace, HH, colon, mm, colon, ss)
            // yyyy-M-dd H:m:s
            , of(yyyy, strike, M, strike, dd, blankSpace, H, colon, m, colon, s)
            // yyyy-M-d H:m:s
            , of(yyyy, strike, M, strike, d, blankSpace, H, colon, m, colon, s)
            // yyyy-MM-d H:m:s
            , of(yyyy, strike, MM, strike, d, blankSpace, H, colon, m, colon, s)

            // yyyy/M/dd HH:mm:ss
            , of(yyyy, slash, M, slash, dd, blankSpace, HH, colon, mm, colon, ss)
            // yyyy/M/d HH:mm:ss
            , of(yyyy, slash, M, slash, d, blankSpace, HH, colon, mm, colon, ss)
            // yyyy/MM/d HH:mm:ss
            , of(yyyy, slash, MM, slash, d, blankSpace, HH, colon, mm, colon, ss)
            // yyyy/M/dd H:m:s
            , of(yyyy, slash, M, slash, dd, blankSpace, H, colon, m, colon, s)
            // yyyy/M/d H:m:s
            , of(yyyy, slash, M, slash, d, blankSpace, H, colon, m, colon, s)
            // yyyy/MM/d H:m:s
            , of(yyyy, slash, MM, slash, d, blankSpace, H, colon, m, colon, s)

            /*
             * 奇奇怪怪的格式
             * */
            // yyyyMMdd HH:mm:ss
            ,
            of(yyyy, MM, dd, blankSpace, HH, colon, mm, colon, ss)
            // yyyyMMdd HHmmss
            ,
            of(yyyy, MM, dd, blankSpace, HH, mm, ss)
            //yyyy年 MM月 dd日 EEE HH:mm:ss 'CST'
            ,
            of("yyyy年 MM月 dd日 EEE HH:mm:ss zzz",
               yyyy, year_cn, blankSpace,
               MM, month_cn, blankSpace,
               dd, day_cn, blankSpace,
               weeks3, blankSpace,
               HH, colon, mm, colon, ss, blankSpace,
               zzz).withLocal(Locale.CHINA)

            //EEE MMM dd HH:mm:ss 'CST' yyyy
            , of("EEE MMM dd HH:mm:ss zzz yyyy",
                 weeks3, blankSpace,
                 month3, blankSpace,
                 dd, blankSpace,
                 HH, colon, mm, colon, ss, blankSpace,
                 zzz, blankSpace, yyyy)
                    .withLocal(Locale.ENGLISH)
            //MMM dd, yyyy K:mm:ss a
            , of("MMM dd, yyyy K:mm:ss a",
                 month3, blankSpace,
                 dd, str(","), blankSpace,
                 yyyy, blankSpace,
                 K, colon, mm, colon, ss, blankSpace, a)
                    .withLocal(Locale.ENGLISH)
    ));

    boolean support(String str);

    Date format(String str);

    LocalDateTime parse(String str);

    String toString(Date date);

    String toString(LocalDateTime dateTime);

    String getPattern();

    static Date fromString(String dateString) {
        DateFormatter formatter = getFormatter(dateString);
        if (formatter != null) {
            return formatter.format(dateString);
        }

        return null;
    }

    static Date fromString(String dateString, String pattern) {
        DateFormatter formatter = getFormatter(dateString);
        if (formatter != null) {
            return formatter.format(dateString);
        }
        return DateTimeFormat
                .forPattern(pattern)
                .parseDateTime(dateString)
                .toDate();
    }

    static String toString(LocalDateTime date, String format) {
        if (null == date) return null;
        for (DateFormatter formatter : supportFormatter) {
            if (formatter.getPattern().equals(format))
                return formatter.toString(date);
        }
        return DateTimeFormatter.ofPattern(format).format(date);
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
        return getFormatter(dateString) != null;
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
