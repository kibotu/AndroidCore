package com.exozet.android.core.extensions

import org.joda.time.DateTimeZone
import org.joda.time.Duration
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter


fun LocalDateTime.durationUntil(end: LocalDateTime) = Duration(toDateTime(DateTimeZone.UTC), end.toDateTime(DateTimeZone.UTC))

val String.iso8601Date: LocalDateTime
    get() = LocalDateTime.parse(this, iso8601Formatter)

val LocalDateTime.iso8601Date: String
    get() = toString(iso8601Formatter)

/**
 * 2013-01-09T19:32:49.103+05:30
 */
val iso8601Formatter: DateTimeFormatter by lazy { DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ") }
