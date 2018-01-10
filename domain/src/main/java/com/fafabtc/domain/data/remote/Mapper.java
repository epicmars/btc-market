package com.fafabtc.domain.data.remote;

/**
 * Created by jastrelax on 2018/1/7.
 */

public interface Mapper<F, T> {

    T from(F source);

}
