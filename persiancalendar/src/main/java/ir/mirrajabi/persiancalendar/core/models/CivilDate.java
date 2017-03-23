package ir.mirrajabi.persiancalendar.core.models;

import java.util.Calendar;

import ir.mirrajabi.persiancalendar.core.Constants;
import ir.mirrajabi.persiancalendar.core.exceptions.DayOutOfRangeException;
import ir.mirrajabi.persiancalendar.core.exceptions.MonthOutOfRangeException;
import ir.mirrajabi.persiancalendar.core.exceptions.YearOutOfRangeException;

/**
 * @author Amir
 * @author ebraminio
 */

public class CivilDate extends AbstractDate {
    private static final int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private int mYear;
    private int mMonth;
    private int mDay;

    public CivilDate() {
        this(Calendar.getInstance());
    }

    public CivilDate(Calendar calendar) {
        this.mYear = calendar.get(Calendar.YEAR);
        this.mMonth = calendar.get(Calendar.MONTH) + 1;
        this.mDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public CivilDate(int year, int month, int day) {
        this();
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
        if (day < 1)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " "  + Constants.IS_OUT_OF_RANGE);

        if (mMonth != 2 && day > daysInMonth[mMonth])
            throw new DayOutOfRangeException(
                    Constants.DAY + " "  + day + " "  + Constants.IS_OUT_OF_RANGE);

        if (mMonth == 2 && isLeapYear() && day > 29)
            throw new DayOutOfRangeException(
                    Constants.DAY + " "  + day + " "  + Constants.IS_OUT_OF_RANGE);

        if (mMonth == 2 && (!isLeapYear()) && day > 28)
            throw new DayOutOfRangeException(
                    Constants.DAY + " "  + day + " "  + Constants.IS_OUT_OF_RANGE);

        // TODO check for the case of leap mYear for February
        this.mDay = day;
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

    public String getEvent() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        if (month < 1 || month > 12)
            throw new MonthOutOfRangeException(
                    Constants.MONTH  + " " + month + " "  + Constants.IS_OUT_OF_RANGE);

        // Set the mDay again, so that exceptions are thrown if the
        // mDay is out of range
        setDayOfMonth(getDayOfMonth());

        this.mMonth = month;
    }

    public int getWeekOfMonth() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
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

    public boolean isLeapYear() {
        if (mYear % 400 == 0)
            return true;

        else if (mYear % 100 == 0)
            return false;

        return (mYear % 4 == 0);
    }

    public CivilDate rollDay(int amount, boolean up) {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public CivilDate rollMonth(int amount, boolean up) {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public CivilDate rollYear(int amount, boolean up) {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public boolean equals(CivilDate civilDate) {
        return this.getDayOfMonth() == civilDate.getDayOfMonth()
                && this.getMonth() == civilDate.getMonth()
                && this.getYear() == civilDate.getYear();
    }

    @Override
    public CivilDate clone() {
        return new CivilDate(getYear(), getMonth(), getDayOfMonth());
    }
}
