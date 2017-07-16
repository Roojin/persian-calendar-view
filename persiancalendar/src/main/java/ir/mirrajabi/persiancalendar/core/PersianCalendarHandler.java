package ir.mirrajabi.persiancalendar.core;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RawRes;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

import ir.mirrajabi.persiancalendar.R;
import ir.mirrajabi.persiancalendar.core.exceptions.DayOutOfRangeException;
import ir.mirrajabi.persiancalendar.core.interfaces.OnDayClickedListener;
import ir.mirrajabi.persiancalendar.core.interfaces.OnDayLongClickedListener;
import ir.mirrajabi.persiancalendar.core.interfaces.OnEventUpdateListener;
import ir.mirrajabi.persiancalendar.core.interfaces.OnMonthChangedListener;
import ir.mirrajabi.persiancalendar.core.models.AbstractDate;
import ir.mirrajabi.persiancalendar.core.models.CalendarEvent;
import ir.mirrajabi.persiancalendar.core.models.CivilDate;
import ir.mirrajabi.persiancalendar.core.models.Day;
import ir.mirrajabi.persiancalendar.core.models.IslamicDate;
import ir.mirrajabi.persiancalendar.core.models.PersianDate;
import ir.mirrajabi.persiancalendar.helpers.ArabicShaping;
import ir.mirrajabi.persiancalendar.helpers.DateConverter;

public class PersianCalendarHandler {
    private final String TAG = PersianCalendarHandler.class.getName();
    private Context mContext;
    private Typeface mTypeface;
    private Typeface mHeadersTypeface;

    private int mColorBackground = Color.BLACK;
    private int mColorHoliday = Color.RED;
    private int mColorHolidaySelected = Color.RED;
    private int mColorNormalDay = Color.WHITE;
    private int mColorNormalDaySelected = Color.BLUE;
    private int mColorDayName = Color.LTGRAY;
    private int mColorEventUnderline = Color.RED;

    @DrawableRes
    private int mSelectedDayBackground = R.drawable.circle_select;
    @DrawableRes
    private int mTodayBackground = R.drawable.circle_current_day;

    private float mDaysFontSize = 25;
    private float mHeadersFontSize = 20;

    private boolean mHighlightLocalEvents = true;
    private boolean mHighlightOfficialEvents = true;

    private List<CalendarEvent> mOfficialEvents;
    private List<CalendarEvent> mLocalEvents;

    private OnDayClickedListener mOnDayClickedListener;
    private OnDayLongClickedListener mOnDayLongClickedListener;
    private OnMonthChangedListener mOnMonthChangedListener;
    private OnEventUpdateListener mOnEventUpdateListener;

    private String[] mMonthNames = {
            "فروردین",
            "اردیبهشت",
            "خرداد",
            "تیر",
            "مرداد",
            "شهریور",
            "مهر",
            "آبان",
            "آذر",
            "دی",
            "بهمن",
            "اسفند"
    };
    private String[] mWeekDaysNames = {
            "شنبه",
            "یک‌شنبه",
            "دوشنبه",
            "سه‌شنبه",
            "چهارشنبه",
            "پنج‌شنبه",
            "جمعه"
    };

    private PersianCalendarHandler(Context context) {
        this.mContext = context;
        mLocalEvents = new ArrayList<>();
    }

    private static WeakReference<PersianCalendarHandler> myWeakInstance;

    public static PersianCalendarHandler getInstance(Context context) {
        if (myWeakInstance == null || myWeakInstance.get() == null) {
            myWeakInstance = new WeakReference<>(new PersianCalendarHandler(context.getApplicationContext()));
        }
        return myWeakInstance.get();
    }

    /**
     * Text shaping is a essential thing on supporting Arabic script text on older Android versions.
     * It converts normal Arabic character to their presentation forms according to their position
     * on the text.
     *
     * @param text Arabic string
     * @return Shaped text
     */
    public String shape(String text) {
        return (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN)
                ? ArabicShaping.shape(text)
                : text;
    }

    private void initTypeface() {
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(mContext.getAssets(), Constants.FONT_PATH);
        }
        if (mHeadersTypeface == null) {
            mHeadersTypeface = Typeface.createFromAsset(mContext.getAssets(), Constants.FONT_PATH);
        }
    }

    public void setFont(TextView textView) {
        initTypeface();
        textView.setTypeface(mTypeface);
    }

    public void setFontAndShape(TextView textView) {
        setFont(textView);
        textView.setText(shape(textView.getText().toString()));
    }

    private char[] mPreferredDigits = Constants.PERSIAN_DIGITS;
    private boolean mIranTime;

    public PersianDate getToday() {
        return DateConverter.civilToPersian(new CivilDate(makeCalendarFromDate(new Date())));
    }

    public Calendar makeCalendarFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (mIranTime) {
            calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        }
        calendar.setTime(date);
        return calendar;
    }

    public String formatNumber(int number) {
        return formatNumber(Integer.toString(number));
    }

    public String formatNumber(String number) {
        if (mPreferredDigits == Constants.ARABIC_DIGITS)
            return number;

        StringBuilder sb = new StringBuilder();
        for (char i : number.toCharArray()) {
            if (Character.isDigit(i)) {
                sb.append(mPreferredDigits[Integer.parseInt(i + "")]);
            } else {
                sb.append(i);
            }
        }
        return sb.toString();
    }

    public String dateToString(AbstractDate date) {
        return formatNumber(date.getDayOfMonth()) + ' ' + getMonthName(date) + ' ' +
                formatNumber(date.getYear());
    }

    public String dayTitleSummary(PersianDate persianDate) {
        return getWeekDayName(persianDate) + Constants.PERSIAN_COMMA + " " + dateToString(persianDate);
    }

    public boolean isHighlightingLocalEvents() {
        return mHighlightLocalEvents;
    }

    public PersianCalendarHandler setHighlightLocalEvents(boolean highlightLocalEvents) {
        mHighlightLocalEvents = highlightLocalEvents;
        return this;
    }

    public boolean isHighlightingOfficialEvents() {
        return mHighlightOfficialEvents;
    }

    public PersianCalendarHandler setHighlightOfficialEvents(boolean highlightOfficialEvents) {
        mHighlightOfficialEvents = highlightOfficialEvents;
        return this;
    }

    public String[] monthsNamesOfCalendar(AbstractDate date) {
        return mMonthNames.clone();
    }

    public String getMonthName(AbstractDate date) {
        return monthsNamesOfCalendar(date)[date.getMonth() - 1];
    }

    public String getWeekDayName(AbstractDate date) {
        if (date instanceof IslamicDate)
            date = DateConverter.islamicToCivil((IslamicDate) date);
        else if (date instanceof PersianDate)
            date = DateConverter.persianToCivil((PersianDate) date);

        return mWeekDaysNames[date.getDayOfWeek() % 7];
    }

    private String readStream(InputStream is) {
        // http://stackoverflow.com/a/5445161
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private String readRawResource(@RawRes int res) {
        return readStream(mContext.getResources().openRawResource(res));
    }

    private List<CalendarEvent> readEventsFromJSON() {
        List<CalendarEvent> result = new ArrayList<>();
        try {
            JSONArray days = new JSONObject(readRawResource(R.raw.events)).getJSONArray("events");

            int length = days.length();
            for (int i = 0; i < length; ++i) {
                JSONObject event = days.getJSONObject(i);

                int year = event.getInt("year");
                int month = event.getInt("month");
                int day = event.getInt("day");
                String title = event.getString("title");
                boolean holiday = event.getBoolean("holiday");

                result.add(new CalendarEvent(new PersianDate(year, month, day), title, holiday));
            }

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return result;
    }

    public List<CalendarEvent> getOfficialEventsForDay(PersianDate day){
        if (mOfficialEvents == null)
            mOfficialEvents = readEventsFromJSON();

        return getEventsForDay(day, mOfficialEvents);
    }

    public List<CalendarEvent> getAllEventsForDay(PersianDate day) {
        List<CalendarEvent> events = getOfficialEventsForDay(day);
        events.addAll(getLocalEventsForDay(day));
        return events;
    }

    public String getEventsTitle(PersianDate day, boolean holiday) {
        String titles = "";
        boolean first = true;
        List<CalendarEvent> dayEvents = getAllEventsForDay(day);

        for (CalendarEvent event : dayEvents) {
            if (event.isHoliday() == holiday) {
                if (first) {
                    first = false;

                } else {
                    titles = titles + "\n";
                }
                titles = titles + event.getTitle();
            }
        }
        return titles;
    }

    public PersianCalendarHandler setMonthNames(String[] monthNames) {
        mMonthNames = monthNames;
        return this;
    }

    public PersianCalendarHandler setWeekDaysNames(String[] weekDaysNames) {
        mWeekDaysNames = weekDaysNames;
        return this;
    }

    public List<Day> getDays(int offset) {
        List<Day> days = new ArrayList<>();
        PersianDate persianDate = getToday();
        int month = persianDate.getMonth() - offset;
        month -= 1;
        int year = persianDate.getYear();

        year = year + (month / 12);
        month = month % 12;
        if (month < 0) {
            year -= 1;
            month += 12;
        }
        month += 1;
        persianDate.setMonth(month);
        persianDate.setYear(year);
        persianDate.setDayOfMonth(1);

        int dayOfWeek = DateConverter.persianToCivil(persianDate).getDayOfWeek() % 7;

        try {
            PersianDate today = getToday();
            for (int i = 1; i <= 31; i++) {
                persianDate.setDayOfMonth(i);

                Day day = new Day();
                day.setNum(formatNumber(i));
                day.setDayOfWeek(dayOfWeek);

                if (dayOfWeek == 6 || !TextUtils.isEmpty(getEventsTitle(persianDate, true))) {
                    day.setHoliday(true);
                }

                if (mHighlightLocalEvents)
                    if (getLocalEventsForDay(persianDate).size() > 0)
                        day.setEvent(true, true);
                if (mHighlightOfficialEvents)
                    if (getOfficialEventsForDay(persianDate).size() > 0)
                        day.setEvent(true, false);

                day.setPersianDate(persianDate.clone());

                if (persianDate.equals(today)) {
                    day.setToday(true);
                }

                days.add(day);
                dayOfWeek++;
                if (dayOfWeek == 7) {
                    dayOfWeek = 0;
                }
            }
        } catch (DayOutOfRangeException e) {
        }

        return days;
    }

    public boolean isIranTime() {
        return mIranTime;
    }

    public PersianCalendarHandler setIranTime(boolean iranTime) {
        mIranTime = iranTime;
        return this;
    }

    public Typeface getTypeface() {
        return mTypeface;
    }

    public PersianCalendarHandler setTypeface(Typeface typeface) {
        mTypeface = typeface;
        return this;
    }

    public int getColorBackground() {
        return mColorBackground;
    }

    public PersianCalendarHandler setColorBackground(int colorBackground) {
        mColorBackground = colorBackground;
        return this;
    }

    public int getColorDayName() {
        return mColorDayName;
    }

    public PersianCalendarHandler setColorDayName(int colorDayName) {
        mColorDayName = colorDayName;
        return this;
    }

    public int getColorHoliday() {
        return mColorHoliday;
    }

    public PersianCalendarHandler setColorHoliday(int colorHoliday) {
        mColorHoliday = colorHoliday;
        return this;
    }

    public int getColorHolidaySelected() {
        return mColorHolidaySelected;
    }

    public PersianCalendarHandler setColorHolidaySelected(int colorHolidaySelected) {
        mColorHolidaySelected = colorHolidaySelected;
        return this;
    }

    public PersianCalendarHandler setColorNormalDay(int colorNormalDay) {
        mColorNormalDay = colorNormalDay;
        return this;
    }

    public int getColorNormalDay() {
        return mColorNormalDay;
    }

    public int getColorNormalDaySelected() {
        return mColorNormalDaySelected;
    }

    public PersianCalendarHandler setColorNormalDaySelected(int colorNormalDaySelected) {
        mColorNormalDaySelected = colorNormalDaySelected;
        return this;
    }

    public int getColorEventUnderline() {
        return mColorEventUnderline;
    }

    public PersianCalendarHandler setColorEventUnderline(int colorEventUnderline) {
        mColorEventUnderline = colorEventUnderline;
        return this;
    }

    public float getDaysFontSize() {
        return mDaysFontSize;
    }

    public PersianCalendarHandler setDaysFontSize(float daysFontSize) {
        mDaysFontSize = daysFontSize;
        return this;
    }

    public float getHeadersFontSize() {
        return mHeadersFontSize;
    }

    public PersianCalendarHandler setHeadersFontSize(float headersFontSize) {
        mHeadersFontSize = headersFontSize;
        return this;
    }

    public List<CalendarEvent> getLocalEvents() {
        return mLocalEvents;
    }

    public List<CalendarEvent> getLocalEventsForDay(PersianDate day){
        return getEventsForDay(day,mLocalEvents);
    }

    private List<CalendarEvent> getEventsForDay(PersianDate day, List<CalendarEvent> events){
        List<CalendarEvent> result = new ArrayList<>();
        for (CalendarEvent calendarEvent : events)
            if (calendarEvent.getDate().equals(day))
                result.add(calendarEvent);
        return result;
    }

    public void addLocalEvent(CalendarEvent event) {
        mLocalEvents.add(event);
    }

    public PersianCalendarHandler setOnDayClickedListener(OnDayClickedListener onDayClickedListener) {
        mOnDayClickedListener = onDayClickedListener;
        return this;
    }

    public OnDayClickedListener getOnDayClickedListener() {
        return mOnDayClickedListener;
    }

    public PersianCalendarHandler setOnDayLongClickedListener(OnDayLongClickedListener onDayLongClickedListener) {
        mOnDayLongClickedListener = onDayLongClickedListener;
        return this;
    }

    public OnDayLongClickedListener getOnDayLongClickedListener() {
        return mOnDayLongClickedListener;
    }

    public PersianCalendarHandler setOnMonthChangedListener(OnMonthChangedListener onMonthChangedListener) {
        mOnMonthChangedListener = onMonthChangedListener;
        return this;
    }

    public OnEventUpdateListener getOnEventUpdateListener() {
        return mOnEventUpdateListener;
    }

    public PersianCalendarHandler setOnEventUpdateListener(OnEventUpdateListener onEventUpdateListener) {
        mOnEventUpdateListener = onEventUpdateListener;
        return this;
    }

    public OnMonthChangedListener getOnMonthChangedListener() {
        return mOnMonthChangedListener;
    }

    public int getTodayBackground() {
        return mTodayBackground;
    }

    public PersianCalendarHandler setTodayBackground(int todayBackground) {
        mTodayBackground = todayBackground;
        return this;
    }

    public int getSelectedDayBackground() {
        return mSelectedDayBackground;
    }
    public PersianCalendarHandler setSelectedDayBackground(int selectedDayBackground) {
        mSelectedDayBackground = selectedDayBackground;
        return this;
    }

    public Typeface getHeadersTypeface() {
        return mHeadersTypeface;
    }

    public PersianCalendarHandler setHeadersTypeface(Typeface headersTypeface) {
        mHeadersTypeface = headersTypeface;
        return this;
    }
}
