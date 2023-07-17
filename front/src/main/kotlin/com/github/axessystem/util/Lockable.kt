package com.github.axessystem.util

interface Lockable {
    fun lock(key: String)
    fun isLocked(key: String): Boolean
    fun unLock(key: String)
}