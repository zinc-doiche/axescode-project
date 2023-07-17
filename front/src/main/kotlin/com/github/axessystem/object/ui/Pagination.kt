package com.github.axessystem.`object`.ui

import kotlin.math.min

/**
 * page: 0부터 시작함
 */
class Pagination<E>(

    /**
     * 전체 요소 목록
     */
    private val list: List<E>,

    /**
     * 한 페이지에서 보여줄 요소의 개수
     */
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