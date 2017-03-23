package ir.mirrajabi.persiancalendar.core.models;

import java.util.Calendar;

import ir.mirrajabi.persiancalendar.core.Constants;
import ir.mirrajabi.persiancalendar.core.exceptions.DayOutOfRangeException;
import ir.mirrajabi.persiancalendar.core.exceptions.MonthOutOfRangeException;
import ir.mirrajabi.persiancalendar.core.exceptions.YearOutOfRangeException;

/**
 * @author Amir
 * @author ebraminio (implementing isLeapYear)
 */

public class PersianDate extends AbstractDate {
    private int mYear;
    private int mMonth;
    private int mDay;

    public PersianDate(int year, int month, int day) {
        setYear(year);
        // Initialize mDay, so that we get no exceptions when setting mMonth
        this.mDay = 1;
        setMonth(month);
        setDayOfMonth(day);
    }

    public PersianDate clone() {
        return new PersianDate(getYear(), getMonth(), getDayOfMonth());
    }

    public int getDayOfMonth() {
        return mDay;
    }

    public void setDayOfMonth(int day) {
        if (day < 1)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " " + Constants.IS_OUT_OF_RANGE);

        if (mMonth <= 6 && day > 31)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " " + Constants.IS_OUT_OF_RANGE);

        if (mMonth > 6 && mMonth <= 12 && day > 30)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " " + Constants.IS_OUT_OF_RANGE);

        if (isLeapYear() && mMonth == 12 && day > 30)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " " + Constants.IS_OUT_OF_RANGE);

        if ((!isLeapYear()) && mMonth == 12 && day > 29)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " " + Constants.IS_OUT_OF_RANGE);

        this.mDay = day;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        if (month < 1 || month > 12)
            throw new MonthOutOfRangeException(
                    Constants.MONTH + " " + month + " " + Constants.IS_OUT_OF_RANGE);

        // Set the mDay again, so that exceptions are thrown if the
        // mDay is out of range
        setDayOfMonth(mDay);

        this.mMonth = month;
    }

    public int getWeekOfYear() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        if (year == 0)
            throw new YearOutOfRangeException(Constants.YEAR_0_IS_INVALID);

        this.mYear = year;
    }

    public PersianDate rollDay(int amount, boolean up) {
        mDay += amount * (up ? 1 : -1);
        return this;
    }

    public PersianDate rollMonth(int amount, boolean up) {
        mMonth += amount * (up ? 1 : -1);
        return this;
    }

    public PersianDate rollYear(int amount, boolean up) {
        mYear += amount * (up ? 1 : -1);
        return this;
    }

    public String getEvent() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public int getDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, mYear);
        cal.set(Calendar.MONTH, mMonth - 1);
        cal.set(Calendar.DAY_OF_MONTH, mDay);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public int getDayOfYear() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public int getWeekOfMonth() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public boolean isLeapYear() {
        int y;
        if (mYear > 0)
            y = mYear - 474;
        else
            y = 473;
        return (((((y % 2820) + 474) + 38) * 682) % 2816) < 682;
    }

    public boolean equals(PersianDate persianDate) {
        return this.getDayOfMonth() == persianDate.getDayOfMonth()
                && this.getMonth() == persianDate.getMonth()
                && (this.getYear() == persianDate.getYear() || this.getYear() == -1);
    }
}
