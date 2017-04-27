package org.hsweb.commons;

import org.hsweb.commons.time.DateFormatter;
import org.junit.Test;

import java.util.Date;

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
                "Thu Oct 16 07:13:48 GMT 2015",
                "Fri Oct 16 07:13:48 CST 2015",
                "2017年 04月 27日 星期四 17:18:20 CST",
                "2017年 04月 21日 星期五 17:18:20 GMT",
                "2015年11月01日 13时12分01秒",
                "20151101131201",
                "20151101 13:12:01",
                "20151101 131201",
                "2015-11-01 13:12:01",
                "2015-11-01 13:12:01+0100",
                "2015-11-01T13:12:01",
                "2015-11-01T13:12:01+0100",
                "Sep 29, 2012 1:00:01 AM",
                "Sep 29, 2012 1:00:01 PM"
        };
        for (String test : tests) {
            DateFormatter formatter = DateFormatter.getFormatter(test);
            Date date = formatter.format(test);

            System.out.println(formatter.getPattern() + "=>:\t\t" + test + " = " + DateFormatter.toString(date, formatter.getPattern()));
        }

    }
}
