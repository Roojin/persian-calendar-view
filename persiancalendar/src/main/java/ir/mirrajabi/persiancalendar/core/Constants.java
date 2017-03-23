package ir.mirrajabi.persiancalendar.core;

public class Constants {
    public final static String DAY = "day";
    public final static String IS_OUT_OF_RANGE = "is out of range!";
    public final static String NOT_IMPLEMENTED_YET = "not implemented yet!";
    public final static String MONTH = "month";
    public final static String YEAR_0_IS_INVALID = "Year 0 is invalid!";
    public static final int MONTHS_LIMIT = 5000; // this should be an even number
    public static final String OFFSET_ARGUMENT = "OFFSET_ARGUMENT";
    public static final String BROADCAST_INTENT_TO_MONTH_FRAGMENT = "BROADCAST_INTENT_TO_MONTH_FRAGMENT";
    public static final String BROADCAST_FIELD_TO_MONTH_FRAGMENT = "BROADCAST_FIELD_TO_MONTH_FRAGMENT";
    public static final String BROADCAST_FIELD_SELECT_DAY = "BROADCAST_FIELD_SELECT_DAY";
    public static final int BROADCAST_TO_MONTH_FRAGMENT_RESET_DAY = Integer.MAX_VALUE;
    public static final String FONT_PATH = "fonts/NotoNaskhArabic-Regular.ttf";

    public static final char PERSIAN_COMMA = '،';
    public static final String[] FIRST_CHAR_OF_DAYS_OF_WEEK_NAME = {"ش", "ی", "د", "س",
            "چ", "پ", "ج"};
    public static final char[] ARABIC_DIGITS = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9'};
    public static final char[] PERSIAN_DIGITS = {'۰', '۱', '۲', '۳', '۴', '۵', '۶',
            '۷', '۸', '۹'};
}
