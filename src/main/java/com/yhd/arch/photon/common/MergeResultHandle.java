package com.yhd.arch.photon.common;

public interface MergeResultHandle<T> {

	T handle(Iterable<RemoteResponse> parameter);
}
