# PersianCalendarView - تقویم فارسی اندروید
[![](https://jitpack.io/v/mirrajabi/persian-calendar-view.svg)](https://jitpack.io/#mirrajabi/persian-calendar-view)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Persian%20Calendar%20View-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/5460) <a href="http://www.methodscount.com/?lib=com.github.mirrajabi%3Apersian-calendar-view%3A1.1.1"><img src="https://img.shields.io/badge/Methods and size-core: 397 | 144 KB-e91e63.svg"/></a>


[![CircleCI](https://circleci.com/gh/mirrajabi/persian-calendar-view.svg?style=svg)](https://circleci.com/gh/mirrajabi/persian-calendar-view)

Persian calendar view for android based on [ebraminio](https://github.com/ebraminio)'s [DroidPersianCalendar](https://github.com/ebraminio/DroidPersianCalendar) open-source project.

![demo](https://cloud.githubusercontent.com/assets/8886687/24263697/8ca36fec-101b-11e7-8664-975465d2e78a.jpg)

### Usage
First add jitpack to your projects build.gradle file
```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
   	}
}
```
Then add the dependency in modules build.gradle file
```groovy
dependencies {
    compile 'com.github.roojin:persian-calendar-view:1.2.2'
}
```
now just simply add PersianCalendarView to your layouts
```xml
<ir.mirrajabi.persiancalendar.PersianCalendarView
    android:id="@+id/persian_calendar"
    android:layout_width="match_parent"
    android:layout_height="290dp"
    app:pcv_colorBackground="#292929"
    app:pcv_colorDayName="#bab6b6"
    app:pcv_colorHoliday="#ffd506"
    app:pcv_colorHolidaySelected="#f1f2f3"
    app:pcv_colorNormalDaySelected="#d9d9d9"
    app:pcv_colorNormalDay="#f3f4f5"
    app:pcv_eventUnderlineColor="#02f0f0"/>
```
[Sample App](https://github.com/mirrajabi/persian-calendar-view/tree/master/app) is provided to demonstrate the basic scenario.

You can also interact with the sample app in [this Page](https://appetize.io/embed/gu17tjj7p68z09w3vm7e4hb0mc?device=nexus5&scale=75&orientation=portrait&osVersion=7.0)

## Styling
#### Attributes
| Attribute                  | Type      | Description                                        |
| -------------------------- |:---------:| :------------------------------------------------- |
| pcv_typefacePath           | string    | Path to custom font in assets folder               |
| pcv_headersTypefacePath    | string    | Path to custom font for header in assets folder    |
| pcv_colorBackground        | color     | Calendar's background color                        |
| pcv_colorHoliday           | color     | Text color of holidays                             |
| pcv_colorHolidaySelected   | color     | Text color of holidays when selected               |
| pcv_colorNormalDay         | color     | Text color of normal days                          |
| pcv_colorNormalDaySelected | color     | Text color of normal days when selected            |
| pcv_colorDayName           | color     | Text color of day name headers                     |
| pcv_eventUnderlineColor    | color     | Underline color of days with events                |
| pcv_todayBackground        | reference | Background resource used to highlight today        |
| pcv_selectedDayBackground  | reference | Background resource used to highlight selected day |
| pcv_fontSize               | dimension | Day digits font size                               |
| pcv_headersFontSize        | dimension | Day name headers font size                         |

## Functions
Most of the functionalities are inside the handler class. you can get it like this
```java
  PersianCalendarView calendarView  = (PersianCalendarView) findViewById(R.id.persian_calendar);
  PersianCalendarHandler calendarHandler = persianCalendarView.getCalendar();
                PersianDate today = calendar.getToday();
```
### Events
The library includes official events but you can also add your own events to it
#### Adding local events
```java
// Add an event called "Custom event" to this day
calendarHandler.addLocalEvent(new CalendarEvent(calendar.getToday(), "Custom event", false));
// Add an event called "Custom event 2" to 12 days later
calendarHandler.addLocalEvent(new CalendarEvent(today.clone().rollDay(12,true), "Custom event 2", true));
// Add an event called "Custom event 3" to 1399/1/10 later
calendarHandler.addLocalEvent(new CalendarEvent(new PersianDate(1399,1,10), "Custom event 2", true));
```
#### Getting events for specific day
```java
PersianDate today = calendar.getToday();
calendarHandler.getAllEventsForDay(today);
calendarHandler.getLocalEvents();
calendarHandler.getLocalEventsForDay(today);
calendarHandler.getOfficialEventsForDay(today);
```
### Listeners
currently you can set Click, LongClick and MonthChange listeners on [PersianCalendarView](https://github.com/mirrajabi/persian-calendar-view/blob/master/persiancalendar/src/main/java/ir/mirrajabi/persiancalendar/PersianCalendarView.java)
```java
void setOnDayClickedListener(OnDayClickedListener listener)
void setOnDayLongClickedListener(OnDayLongClickedListener listener)
void setOnMonthChangedListener(OnMonthChangedListener listener)
```
### Other functions of [PersianCalendarView](https://github.com/mirrajabi/persian-calendar-view/blob/master/persiancalendar/src/main/java/ir/mirrajabi/persiancalendar/PersianCalendarView.java)
```java
void update()
void goToDate(PersianDate date)
void goToToday()
void goToNextMonth()
void goToPreviousMonth()
void goToMonthFromNow(int offset)
PersianCalendarHandler getCalendar()
```

### Useful methods of [PersianCalendarHandler](https://github.com/mirrajabi/persian-calendar-view/blob/master/persiancalendar/src/main/java/ir/mirrajabi/persiancalendar/core/PersianCalendarHandler.java)
```java
PersianDate getToday()
// These ones Convert english digits to persian
String formatNumber(int number) 
String formatNumber(String number)
// Converts classes inherited from AbstractDate class to date string
String dateToString(AbstractDate date)
String dayTitleSummary
String dayTitleSummary(PersianDate persianDate)
String getMonthName(AbstractDate date)
String getWeekDayName(AbstractDate date)
List<CalendarEvent> getOfficialEventsForDay(PersianDate day)
List<CalendarEvent> getAllEventsForDay(PersianDate day)
List<CalendarEvent> getLocalEvents()
List<CalendarEvent> getLocalEventsForDay(PersianDate day)
String getEventsTitle(PersianDate day, boolean holiday)
// These ones also have getters
PersianCalendarHandler setMonthNames(String[] monthNames)
PersianCalendarHandler setWeekDaysNames(String[] weekDaysNames)
PersianCalendarHandler setTypeface(Typeface typeface)
PersianCalendarHandler setColorBackground(int colorBackground) 
PersianCalendarHandler setColorDayName(int colorDayName)
PersianCalendarHandler setColorHoliday(int colorHoliday)
PersianCalendarHandler setColorHolidaySelected(int colorHolidaySelected)
PersianCalendarHandler setColorNormalDay(int colorNormalDay)
PersianCalendarHandler setColorNormalDaySelected(int colorNormalDaySelected)
PersianCalendarHandler setColorEventUnderline(int colorEventUnderline)
PersianCalendarHandler setDaysFontSize(int daysFontSize)
PersianCalendarHandler setHeadersFontSize(int headersFontSize)
PersianCalendarHandler setTodayBackground(int todayBackground)
PersianCalendarHandler setSelectedDayBackground(int selectedDayBackground)
PersianCalendarHandler setHighlightOfficialEvents(boolean highlightOfficialEvents)
PersianCalendarHandler setHighlightLocalEvents(boolean highlightLocalEvents)
```
### Notes

If you want to use this library with Xamarin or Basic4Android you can use the following links. I didn't test any of these myself but I guess they may work fine.

#### using with Xamarin

take a look at [issue #11](https://github.com/Roojin/persian-calendar-view/issues/11#event-1378687554)

#### Using with B4A

[https://b4x.com/android/forum/threads/persiancalendarview.79171/](https://b4x.com/android/forum/threads/persiancalendarview.79171/)

#### Todo
- [ ] Make it even more flexible and customizable
- [ ] Clean it up

### Contributions
Any contributions are welcome. just fork it and submit your changes to your fork and then create a pull request

### Thanks
Special thanks to [ebrahimino](https://github.com/ebraminio) for his open-source project which this library is based on

### Links
[Roojin co](http://roojin.org)

[Roojin co (Github page)](https://github.com/roojin)

[Mirrajabi.ir](http://mirrajabi.ir)

