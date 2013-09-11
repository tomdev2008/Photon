package com.yhd.arch.photon.router.balancer;


public interface ILoadBalancer<T, R> {

	public R select();

	public void update(Iterable<T> serviceSet);

	public void setWhiteList(Iterable<String> serviceSet);

}
