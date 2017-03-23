package ir.mirrajabi.pc.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import ir.mirrajabi.persiancalendar.PersianCalendarView;
import ir.mirrajabi.persiancalendar.core.interfaces.OnDayClickedListener;
import ir.mirrajabi.persiancalendar.core.models.CalendarEvent;
import ir.mirrajabi.persiancalendar.core.models.PersianDate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final PersianCalendarView persianCalendarView  = (PersianCalendarView)findViewById(R.id.persian_calendar);
        PersianDate today = persianCalendarView.getCalendar().getToday();
        persianCalendarView.getCalendar().addLocalEvent(new CalendarEvent(
                persianCalendarView.getCalendar().getToday(), "Custom event", false
        ));
        persianCalendarView.getCalendar().addLocalEvent(new CalendarEvent(
                today.clone().rollDay(12,true), "Custom event 2", true
        ));
        persianCalendarView.setOnDayClickedListener(new OnDayClickedListener() {
            @Override
            public void onClick(PersianDate date) {
                for(CalendarEvent e : persianCalendarView.getCalendar().getAllEventsForDay(date))
                Toast.makeText(MainActivity.this, e.getTitle(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
