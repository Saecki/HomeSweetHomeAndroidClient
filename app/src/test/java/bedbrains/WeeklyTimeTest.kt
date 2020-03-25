package bedbrains

import bedbrains.shared.datatypes.rules.WeeklyTime
import kotlin.test.assertEquals

fun main() {
    testWeeklyTimeMin()
    testWeeklyTimeMax()
    testWeeklyTime()
    testWeeklyTimeDaily()
}

fun testWeeklyTimeMin() {
    val time = WeeklyTime.MIN
    assertEquals(0, time.day)
    assertEquals(0, time.hour)
    assertEquals(0, time.minute)
    assertEquals(0, time.second)
    assertEquals(0, time.millisecond)
}

fun testWeeklyTimeMax() {
    val time = WeeklyTime.MAX
    assertEquals(6, time.day)
    assertEquals(23, time.hour)
    assertEquals(59, time.minute)
    assertEquals(59, time.second)
    assertEquals(999, time.millisecond)
}

fun testWeeklyTime() {
    val time = WeeklyTime(3, 5, 6, 7, 8)
    time.day -= 1
    assertEquals(2, time.day)
    assertEquals(5, time.hour)
    assertEquals(6, time.minute)
    assertEquals(7, time.second)
    assertEquals(8, time.millisecond)

    val time1 = WeeklyTime(0)
    time1.day = 4
    val time2 = WeeklyTime(4, 0, 0, 0, 0)
    assertEquals(time1, time2)
}

fun testWeeklyTimeDaily() {
    val time2 = WeeklyTime(8)
    assertEquals(8, time2.inDailyMilliseconds)
    val time1 = WeeklyTime(2, 0, 0, 0, 0)
    assertEquals(0, time1.inDailyMilliseconds)
}