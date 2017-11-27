package org.hswebframework.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtils {
    /**
     * 获取汉字全拼
     *
     * @param str 汉字字符串
     * @return 汉字字符串全拼
     */
    public static String toPingYin(String str) {
        char[] t1 = str.toCharArray();
        String[] t2;
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断是否为汉字字符
                if (StringUtils.containsChineseChar(Character.toString(t1[i]))) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 += t2[0];
                } else {
                    t4 += Character.toString(t1[i]);
                }
            }
            return t4;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
        }
        return t4;
    }

    /**
     * 获取中文字符串首字母
     *
     * @param str 字符串
     * @return 字符串首字母
     */
    public static String toPinYinHeadChar(String str) {
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }
}
