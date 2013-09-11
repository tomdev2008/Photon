package com.yhd.arch.photon.router;

import akka.actor.ActorRef;

import com.yhd.arch.photon.actor.MethodActorStatus;

public class RouteeMeta {
	private String clazzName;
	private String methodName;
	private String actorId;
	private ActorRef actor;
	private RouterType type = RouterType.ROUNDROBIN;
	private int weight = 1;
	private MethodActorStatus status = MethodActorStatus.ENABLE;

	public RouteeMeta(String clazzName, String methodName) {
		super();
		this.clazzName = clazzName;
		this.methodName = methodName;
	}

	public RouteeMeta(String clazzName, String methodName, String actorId, ActorRef actor) {
		super();
		this.clazzName = clazzName;
		this.methodName = methodName;
		this.actorId = actorId;
		this.actor = actor;
	}

	public RouteeMeta(String clazzName, String methodName, String actorId, ActorRef actor, RouterType type, int weight) {
		super();
		this.clazzName = clazzName;
		this.methodName = methodName;
		this.actorId = actorId;
		this.actor = actor;
		this.type = type;
		this.weight = weight;
	}

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public ActorRef getActor() {
		return actor;
	}

	public void setActor(ActorRef actor) {
		this.actor = actor;
	}

	public RouterType getType() {
		return type;
	}

	public void setType(RouterType type) {
		this.type = type;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public MethodActorStatus getStatus() {
		return status;
	}

	public void setStatus(MethodActorStatus status) {
		this.status = status;
	}

	public String getUniqueName() {
		return this.clazzName + "_" + this.methodName;
	}

	@Override
	public String toString() {
		return "RouteeMeta [clazzName=" + clazzName + ", methodName=" + methodName + ", actorId=" + actorId + ", actor=" + actor
				+ ", type=" + type + ", weight=" + weight + "]";
	}

}
