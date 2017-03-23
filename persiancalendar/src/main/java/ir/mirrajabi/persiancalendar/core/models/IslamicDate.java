package ir.mirrajabi.persiancalendar.core.models;

import ir.mirrajabi.persiancalendar.core.Constants;
import ir.mirrajabi.persiancalendar.core.exceptions.DayOutOfRangeException;
import ir.mirrajabi.persiancalendar.core.exceptions.MonthOutOfRangeException;
import ir.mirrajabi.persiancalendar.core.exceptions.YearOutOfRangeException;

/**
 * @author Amir
 * @author ebraminio
 */

public class IslamicDate extends AbstractDate {
    private int mDay;
    private int mMonth;
    private int mYear;
    public IslamicDate(int year, int month, int day) {
        setYear(year);
        // Initialize mDay, so that we get no exceptions when setting mMonth
        this.mDay = 1;
        setMonth(month);
        setDayOfMonth(day);
    }

    public int getDayOfMonth() {
        return mDay;
    }

    public void setDayOfMonth(int day) {
        // TODO This check is not very exact! But it's not worth of it
        // to compute the number of days in this mMonth exactly
        if (day < 1 || day > 30)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " " + Constants.IS_OUT_OF_RANGE);

        this.mDay = day;
    }

    public int getDayOfWeek() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
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

    public IslamicDate rollDay(int amount, boolean up) {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public IslamicDate rollMonth(int amount, boolean up) {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public IslamicDate rollYear(int amount, boolean up) {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public String getEvent() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public int getDayOfYear() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public int getWeekOfMonth() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public boolean isLeapYear() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    @Override
    public IslamicDate clone() {
        return new IslamicDate(getYear(), getMonth(), getDayOfMonth());
    }
}
