package ir.mirrajabi.persiancalendar.core.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import ir.mirrajabi.persiancalendar.R;
import ir.mirrajabi.persiancalendar.core.Constants;
import ir.mirrajabi.persiancalendar.core.PersianCalendarHandler;
import ir.mirrajabi.persiancalendar.core.adapters.CalendarAdapter;
import ir.mirrajabi.persiancalendar.core.interfaces.OnEventUpdateListener;
import ir.mirrajabi.persiancalendar.core.models.CivilDate;
import ir.mirrajabi.persiancalendar.core.models.PersianDate;
import ir.mirrajabi.persiancalendar.helpers.DateConverter;

public class CalendarFragment extends Fragment implements ViewPager.OnPageChangeListener {
    private ViewPager mMonthViewPager;
    private PersianCalendarHandler mPersianCalendarHandler;
    private int mViewPagerPosition;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        mPersianCalendarHandler = PersianCalendarHandler.getInstance(getContext());
        mViewPagerPosition = 0;
        mMonthViewPager = (ViewPager) view.findViewById(R.id.calendar_pager);
        mPersianCalendarHandler.setOnEventUpdateListener(new OnEventUpdateListener() {
            @Override
            public void update() {
                createViewPagers();
            }
        });

        createViewPagers();
        return view;
    }

    private void createViewPagers() {
        mMonthViewPager.setAdapter(new CalendarAdapter(getChildFragmentManager()));
        mMonthViewPager.setCurrentItem(Constants.MONTHS_LIMIT / 2);

        mMonthViewPager.addOnPageChangeListener(this);
    }

    public void changeMonth(int position) {
        mMonthViewPager.setCurrentItem(mMonthViewPager.getCurrentItem() + position, true);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void addEventOnCalendar(PersianDate persianDate) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);

        CivilDate civil = DateConverter.persianToCivil(persianDate);

        intent.putExtra(CalendarContract.Events.DESCRIPTION,
                mPersianCalendarHandler.dayTitleSummary(persianDate));

        Calendar time = Calendar.getInstance();
        time.set(civil.getYear(), civil.getMonth() - 1, civil.getDayOfMonth());

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                time.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                time.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        startActivity(intent);
    }

    private void bringTodayYearMonth() {
        Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT);
        intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT,
                Constants.BROADCAST_TO_MONTH_FRAGMENT_RESET_DAY);
        intent.putExtra(Constants.BROADCAST_FIELD_SELECT_DAY, -1);

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        if (mMonthViewPager.getCurrentItem() != Constants.MONTHS_LIMIT / 2) {
            mMonthViewPager.setCurrentItem(Constants.MONTHS_LIMIT / 2);
        }
    }

    public void bringDate(PersianDate date) {
        PersianDate today = mPersianCalendarHandler.getToday();
        mViewPagerPosition =
                (today.getYear() - date.getYear()) * 12 + today.getMonth() - date.getMonth();

        mMonthViewPager.setCurrentItem(mViewPagerPosition + Constants.MONTHS_LIMIT / 2);

        Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT);
        intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT, mViewPagerPosition);
        intent.putExtra(Constants.BROADCAST_FIELD_SELECT_DAY, date.getDayOfMonth());

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mViewPagerPosition = position - Constants.MONTHS_LIMIT / 2;

        Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT);
        intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT, mViewPagerPosition);
        intent.putExtra(Constants.BROADCAST_FIELD_SELECT_DAY, -1);

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
    public int getViewPagerPosition() {
        return mViewPagerPosition;
    }
}