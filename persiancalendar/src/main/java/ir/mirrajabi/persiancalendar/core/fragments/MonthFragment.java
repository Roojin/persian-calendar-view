package ir.mirrajabi.persiancalendar.core.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ir.mirrajabi.persiancalendar.R;
import ir.mirrajabi.persiancalendar.core.Constants;
import ir.mirrajabi.persiancalendar.core.PersianCalendarHandler;
import ir.mirrajabi.persiancalendar.core.adapters.MonthAdapter;
import ir.mirrajabi.persiancalendar.core.models.Day;
import ir.mirrajabi.persiancalendar.core.models.PersianDate;

public class MonthFragment extends Fragment {
    private PersianCalendarHandler mPersianCalendarHandler;
    private CalendarFragment mCalendarFragment;
    private PersianDate mPersianDate;
    private int mOffset;
    private MonthAdapter mMonthAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        mPersianCalendarHandler = PersianCalendarHandler.getInstance(getContext());
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        mOffset = getArguments().getInt(Constants.OFFSET_ARGUMENT);
        List<Day> days = mPersianCalendarHandler.getDays(mOffset);

        mPersianDate = mPersianCalendarHandler.getToday();
        int month = mPersianDate.getMonth() - mOffset;
        month -= 1;
        int year = mPersianDate.getYear();

        year = year + (month / 12);
        month = month % 12;
        if (month < 0) {
            year -= 1;
            month += 12;
        }

        month += 1;
        mPersianDate.setMonth(month);
        mPersianDate.setYear(year);
        mPersianDate.setDayOfMonth(1);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.month_recycler);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        recyclerView.setLayoutManager(layoutManager);
        mMonthAdapter = new MonthAdapter(getContext(), this, days);
        recyclerView.setAdapter(mMonthAdapter);

        mCalendarFragment = (CalendarFragment) getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag(CalendarFragment.class.getName());

        if (mOffset == 0 && mCalendarFragment.getViewPagerPosition() == mOffset) {
            // mCalendarFragment.selectDay(mPersianCalendarHandler.getToday());
            // updateTitle();
        }

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(setCurrentMonthReceiver,
                new IntentFilter(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT));

        return view;
    }

    private BroadcastReceiver setCurrentMonthReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int value = intent.getExtras().getInt(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT);
            if (value == mOffset) {
                if(mPersianCalendarHandler.getOnMonthChangedListener() != null)
                    mPersianCalendarHandler.getOnMonthChangedListener().onChanged(mPersianDate);
                int day = intent.getExtras().getInt(Constants.BROADCAST_FIELD_SELECT_DAY);
                if (day != -1) {
                    mMonthAdapter.selectDay(day);
                }
            } else if (value == Constants.BROADCAST_TO_MONTH_FRAGMENT_RESET_DAY) {
                mMonthAdapter.clearSelectedDay();
            }
        }
    };

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(setCurrentMonthReceiver);
        super.onDestroy();
    }

    public void onClickItem(PersianDate day) {
        //mCalendarFragment.selectDay(day);
        if (mPersianCalendarHandler.getOnDayClickedListener() != null)
            mPersianCalendarHandler.getOnDayClickedListener().onClick(day);
    }

    public void onLongClickItem(PersianDate day) {
        if (mPersianCalendarHandler.getOnDayLongClickedListener() != null)
            mPersianCalendarHandler.getOnDayLongClickedListener().onLongClick(day);

        //mCalendarFragment.addEventOnCalendar(day);
    }
}
