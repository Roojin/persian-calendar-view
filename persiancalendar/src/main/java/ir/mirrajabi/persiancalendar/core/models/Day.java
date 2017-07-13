package ir.mirrajabi.persiancalendar.core.models;

public class Day {
    private String mNum;
    private boolean mHoliday;
    private boolean mToday;
    private int mDayOfWeek;
    private PersianDate mPersianDate;
    private boolean mEvent;
    private boolean mLocalEvent;

    public boolean isEvent() {
        return mEvent;
    }

    public void setEvent(boolean event) {
        this.mEvent = event;
    }


    public void setEvent(boolean event, boolean isLocal) {
        this.mEvent = event;
        this.mLocalEvent = isLocal;
    }

    public boolean isLocalEvent() {
        return mLocalEvent;
    }

    public Day setLocalEvent(boolean localEvent) {
        mLocalEvent = localEvent;
        return this;
    }

    public String getNum() {
        return mNum;
    }

    public void setNum(String num) {
        this.mNum = num;
    }

    public boolean isHoliday() {
        return mHoliday;
    }

    public void setHoliday(boolean holiday) {
        this.mHoliday = holiday;
    }

    public boolean isToday() {
        return mToday;
    }

    public void setToday(boolean today) {
        this.mToday = today;
    }

    public int getDayOfWeek() {
        return mDayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.mDayOfWeek = dayOfWeek;
    }

    public PersianDate getPersianDate() {
        return mPersianDate;
    }

    public void setPersianDate(PersianDate persianDate) {
        this.mPersianDate = persianDate;
    }
}
