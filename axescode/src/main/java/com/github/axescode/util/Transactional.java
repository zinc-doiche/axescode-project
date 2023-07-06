package com.github.axescode.util;

/**
 * 거래 등 트랜잭션이 필요한 경우를 나타냅니다.
 */
public interface Transactional {

    void commit();
    void rollback();
}
