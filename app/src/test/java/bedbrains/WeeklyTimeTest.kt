package bedbrains

import bedbrains.shared.datatypes.rules.WeeklyTime

fun main() {
    testWeeklyTimeMin()
    testWeeklyTimeMax()
    testWeeklyTimeNow()
}

fun testWeeklyTimeMin() {
    val time = WeeklyTime.MIN
    println("MIN")
    println("time.day: ${time.day}")
    println("time.hour: ${time.hour}")
    println("time.minute: ${time.minute}")
    println("time.second: ${time.second}")
    println("time.millis: ${time.millis}")
}

fun testWeeklyTimeMax() {
    val time = WeeklyTime.MAX
    println("MAX")
    println("time.day: ${time.day}")
    println("time.hour: ${time.hour}")
    println("time.minute: ${time.minute}")
    println("time.second: ${time.second}")
    println("time.millis: ${time.millis}")
}

fun testWeeklyTimeNow() {
    val time = WeeklyTime.now()
    println("now()")
    println("time.day: ${time.day}")
    println("time.hour: ${time.hour}")
    println("time.minute: ${time.minute}")
    println("time.second: ${time.second}")
    println("time.millis: ${time.millis}")
}