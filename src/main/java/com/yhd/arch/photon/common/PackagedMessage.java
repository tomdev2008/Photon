package com.yhd.arch.photon.common;

import scala.concurrent.Promise;

import com.yhd.arch.photon.actor.MethodActorStatus;

public class PackagedMessage {

	private final RemoteRequest request;
	private final Promise<RemoteResponse> promise;
	private MethodActorStatus remoteStatus;

	public PackagedMessage(RemoteRequest request, Promise<RemoteResponse> promise) {
		this.request = request;
		this.promise = promise;
	}

	public RemoteRequest getRequest() {
		return request;
	}

	public Promise<RemoteResponse> getPromise() {
		return promise;
	}

	public MethodActorStatus getRemoteStatus() {
		return remoteStatus;
	}

	public void setRemoteStatus(MethodActorStatus remoteStatus) {
		this.remoteStatus = remoteStatus;
	}

}
