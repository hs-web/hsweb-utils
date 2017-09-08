package org.hsweb.commons;

import org.hsweb.commons.time.DateFormatter;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.util.Comparator;
import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * TODO 完成注释
 *
 * @author zhouhao
 */
public class TimeTests {
    @Test
    public void testFormat() {
        String[] tests = {
                "20151101",
                "2015-11-01",
                "2015/11/02",
                "2015年11月01日",
                "2015年5月12日",
                "2015年12月1日",
                "Thu Oct 16 07:13:48 GMT 2015",
                "Fri Oct 16 07:13:48 CST 2015",
                "2017年 04月 27日 星期四 17:18:20 CST",
                "2017年 04月 21日 星期五 17:18:20 GMT",
                "2015年11月01日 13时12分01秒",
                "20151101131201",
                "20151101 13:12:01",
                "20151101 131201",
                "2015-11-01 13:12:01",

                "2015-1-1 13:12:01",
                "2015-9-1 1:2:1",

                "2015/1/1 13:12:01",
                "2015/9/1 1:2:1",


                "2015-11-01 13:12:01+0100",
                "2015-11-01T13:12:01",
                "2015-11-01T13:12:01+0100",
                "Sep 29, 2012 1:00:01 AM",
                "Sep 29, 2012 1:00:01 PM",
                "07:13:12",
                "2017-4-5",
                "2017/4/5",
                "2017/10/5",
                "2017/4/12"
        };
        for (String test : tests) {
            DateFormatter formatter = DateFormatter.getFormatter(test);
            Date date = formatter.format(test);

            System.out.println(formatter.getPattern() + "=>:\t\t" + test + " = " + DateFormatter.toString(date, formatter.getPattern()));
        }


        BiFunction<String, String, Predicate<String>> compareBuilder = (str1, str2) -> {
            long from = DateFormatter.fromString(str1).getTime();
            long to = DateFormatter.fromString(str2).getTime();
            return (compare) -> {
                long target = DateFormatter.fromString(compare).getTime();
                return from >= to ? target >= from || target <= to : target >= from && target <= to;
            };
        };

        boolean timeInScope = compareBuilder.apply("20:12:00", "06:00:00")
                .and(compareBuilder.apply("18:00:00", "22:00:00"))
                .and(compareBuilder.apply("02:00:00", "22:00:00"))
                .test("21:59:00");

        boolean timeNotInScope = !compareBuilder.apply("08:12:02", "21:12:21")
                .test("21:59:00");

        boolean dateInScope = compareBuilder.apply("2012/12/12 11:11:11", "2012/12/15 23:11:11")
                .test("2012/12/13 00:00:00");

        boolean dateNotInScope = !compareBuilder.apply("2012/12/12 11:11:11", "2012/12/15 23:11:11")
                .test("2012/12/12 10:00:00");

        Assert.assertTrue(timeInScope && timeNotInScope && dateInScope && dateNotInScope);
    }
}
