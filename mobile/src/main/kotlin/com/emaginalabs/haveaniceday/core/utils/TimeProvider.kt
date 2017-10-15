package com.emaginalabs.haveaniceday.core.utils

import org.joda.time.DateTime

class TimeProvider {
    fun now(): DateTime = DateTime.now()
}