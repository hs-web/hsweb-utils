package org.hswebframework.utils.time;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class SmartDateFormatter implements DateFormatter {
    static Part year_cn = str("年");
    static Part month_cn = str("月");
    static Part day_cn = str("日");
    static Part hour_cn = str("时");
    static Part minute_cn = str("分");
    static Part second_cn = str("秒");
    static Part weeks3 = new Week("EEE", 3);
    static Part month3 = new Month("MMM", 3);

    static Part zzz = new TimezonePart("zzz", 3);

    static Part strike = str("-");
    static Part slash = str("/");
    static Part plus = str("+");

    static Part blankSpace = str(" ");

    static Part colon = str(":");

    static Part yyyy = new NumberPart("yyyy", 1900, 2999);
    static Part MM = new NumberPart("MM", 1, 12);
    static Part M = new NumberPart("M", 1, 12);

    static Part dd = new NumberPart("dd", 1, 31);
    static Part d = new NumberPart("d", 1, 31);
    static Part HH = new NumberPart("HH", 0, 23);
    static Part k = new NumberPart("k", 1, 24);
    static Part K = new NumberPart("K", 0, 11);

    static Part a = new MatchPart("a", 2, new HashSet<>(Arrays.asList("AM", "PM")));

    static Part h = new NumberPart("h", 1, 12);
    static Part H = new NumberPart("H", 0, 24);
    static Part mm = new NumberPart("mm", 0, 60);
    static Part m = new NumberPart("m", 0, 60);
    static Part ss = new NumberPart("ss", 0, 60);
    static Part SSS = new NumberPart("SSS", 3, 0, 999);

    static Part s = new NumberPart("s", 0, 60);
    static Part Z = new NumberPart("Z", 4, 0, 9999);
    static Part XXX = new ZoneOffsetPart();

    static Part T = str("T");


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
            return Date.from(dateTime.atOffset(offset).toInstant());
        }
        if (zoneId != null) {
            return Date.from(dateTime
                                     .atZone(zoneId)
                                     .withZoneSameInstant(ZoneId.systemDefault())
                                     .toInstant());
        }

        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

    }

    @Override
    public String toString(Date date) {

        return pattern.format(
                ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
        );
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


    interface Part {
        int length();

        String pattern();

        boolean test(String str, int offset);
    }

}
