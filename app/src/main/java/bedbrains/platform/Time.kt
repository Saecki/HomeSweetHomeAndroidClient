package bedbrains.platform

import android.annotation.TargetApi
import android.os.Build
import bedbrains.shared.datatypes.rules.WeeklyTime
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*

object Time {

    fun formatTime(hour: Int, minute: Int, locale: Locale): String {
        val sdkVersion = Build.VERSION.SDK_INT

        if (sdkVersion < Build.VERSION_CODES.O) {
            val cal = Calendar.getInstance(locale)
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            val date = Date(cal.timeInMillis)
            val df = DateFormat.getTimeInstance(DateFormat.SHORT, locale)

            return df.format(date)
        } else @TargetApi(Build.VERSION_CODES.O) {
            val dtf = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
            val localTime = LocalTime.of(hour, minute)

            return localTime.format(dtf)
        }
    }

    fun formatWeekdayNarrow(day: Int, locale: Locale): String {
        val sdkVersion = Build.VERSION.SDK_INT

        if (sdkVersion < Build.VERSION_CODES.O) {
            val dfs = DateFormatSymbols.getInstance(locale)
            val dfsStrings = dfs.shortWeekdays
            val firstDayOfWeek = Calendar.getInstance(locale).firstDayOfWeek

            return dfsStrings[(firstDayOfWeek + day - 1) % 7 + 1][0].toString()
        } else @TargetApi(Build.VERSION_CODES.O) {
            val wf = WeekFields.of(locale)
            val firstDayOfWeek = wf.firstDayOfWeek

            return firstDayOfWeek.plus(day.toLong()).getDisplayName(TextStyle.NARROW, locale)
        }
    }

    fun formatWeekDayFull(day: Int, locale: Locale): String {
        val sdkVersion = Build.VERSION.SDK_INT

        if (sdkVersion < Build.VERSION_CODES.O) {
            val dfs = DateFormatSymbols.getInstance(locale)
            val dfsStrings = dfs.weekdays
            val firstDayOfWeek = Calendar.getInstance(locale).firstDayOfWeek

            return dfsStrings[(firstDayOfWeek + day - 1) % 7 + 1].toString()
        } else @TargetApi(Build.VERSION_CODES.O) {
            val wf = WeekFields.of(locale)
            val firstDayOfWeek = wf.firstDayOfWeek

            return firstDayOfWeek.plus(day.toLong()).getDisplayName(TextStyle.FULL, locale)
        }
    }

    fun currentWeeklyTime(): WeeklyTime {
        val sdkVersion = Build.VERSION.SDK_INT

        if (sdkVersion < Build.VERSION_CODES.O) {
            val cal = Calendar.getInstance()
            val day = cal.get(Calendar.DAY_OF_WEEK)
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)
            val second = cal.get(Calendar.SECOND)
            val millis = cal.get(Calendar.MILLISECOND)

            return WeeklyTime(day, hour, minute, second, millis)

        } else @TargetApi(Build.VERSION_CODES.O) {
            val dateTime = LocalDateTime.now()
            val day = dateTime.dayOfWeek.value
            val hour = dateTime.hour
            val minute = dateTime.minute
            val second = dateTime.second
            val millis = dateTime.nano / 1000000

            return WeeklyTime(day, hour, minute, second, millis)
        }
    }

    fun getFirstWeekDay(): Int {
        val sdkVersion = Build.VERSION.SDK_INT

        if (sdkVersion < Build.VERSION_CODES.O) {
            val firstDayOfWeek = Calendar.getInstance(Locale.getDefault()).firstDayOfWeek

            return (firstDayOfWeek + 6) % 7
        } else @TargetApi(Build.VERSION_CODES.O) {
            val wf = WeekFields.of(Locale.getDefault())
            val firstDayOfWeek = wf.firstDayOfWeek

            return firstDayOfWeek.value
        }
    }
}
