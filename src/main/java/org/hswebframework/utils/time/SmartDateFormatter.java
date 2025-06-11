package org.hswebframework.utils.time;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class SmartDateFormatter implements DateFormatter {
    public static final Part year_cn = str("年");
    public static final Part month_cn = str("月");
    public static final Part day_cn = str("日");
    public static final Part hour_cn = str("时");
    public static final Part minute_cn = str("分");
    public static final Part second_cn = str("秒");
    public static final Part weeks3 = new Week("EEE", 3);
    public static final Part month3 = new Month("MMM", 3);

    public static final Part zzz = new TimezonePart("zzz", 3);

    public static final Part strike = str("-");
    public static final Part slash = str("/");
    public static final Part plus = str("+");

    public static final Part blankSpace = str(" ");

    public static final Part colon = str(":");

    public static final Part yyyy = new NumberPart("yyyy", 1900, 2999);
    public static final Part MM = new NumberPart("MM", 1, 12);
    public static final Part M = new NumberPart("M", 1, 12);

    public static final Part dd = new NumberPart("dd", 1, 31);
    public static final Part d = new NumberPart("d", 1, 31);
    public static final Part HH = new NumberPart("HH", 0, 23);
    public static final Part k = new NumberPart("k", 1, 24);
    public static final Part K = new NumberPart("K", 0, 11);

    public static final Part a = new MatchPart("a", 2, new HashSet<>(Arrays.asList("AM", "PM")));

    public static final Part h = new NumberPart("h", 1, 12);
    public static final Part H = new NumberPart("H", 0, 24);
    public static final Part mm = new NumberPart("mm", 0, 60);
    public static final Part m = new NumberPart("m", 0, 60);
    public static final Part ss = new NumberPart("ss", 0, 60);
    public static final Part SSS = new NumberPart("SSS", 3, 0, 999);

    public static final Part s = new NumberPart("s", 0, 60);
    public static final Part Z = new NumberPart("Z", 4, 0, 9999);
    public static final Part XXX = new ZoneOffsetPart();

    public static final Part T = str("T");


    DateTimeFormatter pattern;
    final String patternString;

    private final Part[] parts;

    private final int max, min;

    public SmartDateFormatter withLocal(Locale locale) {
        pattern = pattern.withLocale(locale);
        return this;
    }

    SmartDateFormatter(String pattern, int max, int min, Part[] parts) {
        this.parts = parts;
        this.max = max;
        this.min = min;
        this.patternString = pattern;
        this.pattern = DateTimeFormatter.ofPattern(patternString);
    }

    SmartDateFormatter(String pattern, Part[] parts) {
        this.parts = parts;
        this.max = Stream.of(this.parts).mapToInt(Part::length).sum();
        this.min = max;
        this.patternString = pattern;
        this.pattern = DateTimeFormatter.ofPattern(patternString);
    }

    SmartDateFormatter(Part[] parts) {
        this.parts = parts;
        this.max = Stream.of(this.parts).mapToInt(Part::length).sum();
        this.min = max;
        this.patternString = Stream.of(this.parts).map(Part::pattern).collect(Collectors.joining());
        this.pattern = DateTimeFormatter.ofPattern(patternString);
    }

    public static SmartDateFormatter of(String pattern, Part... parts) {
        return new SmartDateFormatter(pattern, parts);
    }

    public static SmartDateFormatter of(String pattern, int min, int max, Part... parts) {
        return new SmartDateFormatter(pattern, max, min, parts);
    }

    public static SmartDateFormatter of(Part... parts) {
        return new SmartDateFormatter(parts);
    }

    @Override
    public boolean support(String str) {
        int len = str.length();
        if (len < min || len > max) {
            return false;
        }
        int temp = 0;
        for (Part part : parts) {
            if (!part.test(str, temp)) {
                return false;
            }
            temp += part.length();
        }
        return true;
    }

    @Override
    public Date format(String str) {
        return Date.from(parse(str).atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public LocalDateTime parse(String str) {
        TemporalAccessor accessor = pattern.parse(str);

        LocalDate date = accessor.query(TemporalQueries.localDate());
        LocalTime time = accessor.query(TemporalQueries.localTime());
        ZoneId zoneId = accessor.query(TemporalQueries.zone());
        ZoneOffset offset = accessor.query(TemporalQueries.offset());
        LocalDateTime dateTime;

        if (date != null) {
            if (time != null) {
                dateTime = LocalDateTime.of(date, time);
            } else {
                dateTime = LocalDateTime.of(date, LocalTime.of(0, 0, 0, 0));
            }
        } else {
            dateTime = LocalDateTime.of(LocalDate.now(), time);
        }
        if (offset != null) {
            return dateTime.atOffset(offset).toLocalDateTime();
        }
        if (zoneId != null) {
            return dateTime
                    .atZone(zoneId)
                    .withZoneSameInstant(ZoneId.systemDefault())
                    .toLocalDateTime();
        }
        return dateTime.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    @Override
    public String toString(Date date) {

        return pattern.format(
                ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
        );
    }

    @Override
    public String toString(LocalDateTime dateTime) {
        return pattern.format(dateTime.atZone(ZoneId.systemDefault()));
    }

    @Override
    public String getPattern() {
        return patternString;
    }


    public static SmartDateFormatter.Part str(String str) {
        return new StringPart(str);
    }

    static class StringPart implements Part {
        private final String pattern;
        private final int length;

        StringPart(String pattern) {
            this(pattern, pattern.length());
        }

        StringPart(String pattern, int length) {
            this.pattern = pattern;
            this.length = length;
        }

        @Override
        public int length() {
            return length;
        }

        @Override
        public String pattern() {
            return pattern;
        }

        @Override
        public boolean test(String str, int offset) {
            for (int i = 0; i < pattern.length(); i++) {
                if (str.charAt(offset + i) != pattern.charAt(i)) {
                    return false;
                }
            }
            return true;
        }
    }

    static class TimezonePart extends MatchPart {
        static Set<String> TZ = new HashSet<>(Arrays.asList(
                "CST", "GMT", "UTC"
        ));

        TimezonePart(String pattern, int length) {
            super(pattern, length, TZ);
        }
    }

    static class ZoneOffsetPart implements Part {

        @Override
        public int length() {
            return 6;
        }

        @Override
        public String pattern() {
            return "Z";
        }

        @Override
        public boolean test(String str, int offset) {
            if (str.charAt(offset) != '+' ||
                    str.charAt(offset + 3) != ':') {
                return false;
            }
            int hour = Utils.toNumber(str, offset + 1, 2);

            if (hour < 0 || hour > 12) {
                return false;
            }
            int minute = Utils.toNumber(str, offset + 4, 2);

            return minute >= 0;
        }
    }

    static class NumberPart implements Part {
        private final String pattern;
        private final int length;
        private final int min;
        private final int max;

        NumberPart(String pattern, int length, int min, int max) {
            this.pattern = pattern;
            this.min = min;
            this.max = max;
            this.length = length;
        }

        NumberPart(String pattern, int min, int max) {
            this(pattern, pattern.length(), min, max);
        }

        @Override
        public int length() {
            return length;
        }

        @Override
        public String pattern() {
            return pattern;
        }

        @Override
        public boolean test(String str, int offset) {
            int val = Utils.toNumber(str, offset, length);
            return val >= min && val <= max;
        }
    }

    static class Month extends MatchPart {
        static Set<String> MONTHS = new HashSet<>(Arrays.asList(
                "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月",
                "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
                "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
        ));

        Month(String pattern, int length) {
            super(pattern, length, MONTHS);
        }

    }

    static class Week extends MatchPart {
        static Set<String> WEEKS = new HashSet<>(Arrays.asList(
                "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日",
                "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun",
                "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday",
                "周一", "周二", "周三", "周四", "周五", "周六", "周日"
        ));

        Week(String pattern, int length) {
            super(pattern, length, WEEKS);
        }

    }

    static class MatchPart implements Part {
        private final Set<String> matched;
        private final String pattern;
        private final int length;

        MatchPart(String pattern, int length, Set<String> matched) {
            this.pattern = pattern;
            this.length = length;
            this.matched = matched;
        }

        @Override
        public int length() {
            return length;
        }

        @Override
        public String pattern() {
            return pattern;
        }

        @Override
        public boolean test(String str, int offset) {
            return matched.contains(str.substring(offset, offset + length));
        }
    }


    public interface Part {
        int length();

        String pattern();

        boolean test(String str, int offset);
    }

}
