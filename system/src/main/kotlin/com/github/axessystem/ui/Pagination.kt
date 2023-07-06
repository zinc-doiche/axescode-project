package com.github.axessystem.ui

import kotlin.math.min

/**
 * page: 0부터 시작함
 */
class Pagination<E>(
    private val list: List<E>,
    private val size: Int
) {

    /**
     * 페이지 개수를 리턴합니다. Ex: {0, 1, 2} => return 3
     */
    val totalPage = list.size / size + 1

    fun getPagedList(page: Int): List<E> {
        assert(page <= totalPage)
        return list.subList(page * size, min((page + 1) * size, list.size))
    }
}