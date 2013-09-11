package com.yhd.arch.photon.usage;

public interface UsageCapacity {
	
	boolean isLimit(long size);
	
	long getLimit();
	
	void setLimit(long limit);
}
