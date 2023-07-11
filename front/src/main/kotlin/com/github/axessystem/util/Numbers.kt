package com.github.axessystem.util

internal fun Long.millisToTicks(): Long = this / 50
internal fun Long.ticksToMillis(): Long = this * 50