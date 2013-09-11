/**
 * 
 */
package com.yhd.arch.photon.actor;

/**
 * @author Archer
 * 
 */
public interface IStatusManager<T> {

	public void changeStatus(T e);

	public T getStatus();

	public boolean isUseFul();
}
