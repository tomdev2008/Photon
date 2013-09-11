package com.yhd.arch.photon.usage;

public class DefaultUsageCapacity implements UsageCapacity{

	//默认不限制
	private long limit=Long.MAX_VALUE;
	@Override
	public boolean isLimit(long size) {
		// TODO Auto-generated method stub
		return size>=limit;
	}

	@Override
	public long getLimit() {
		// TODO Auto-generated method stub
		return this.limit;
	}

	@Override
	public void setLimit(long limit) {
		// TODO Auto-generated method stub
		this.limit=limit;
	}

}
