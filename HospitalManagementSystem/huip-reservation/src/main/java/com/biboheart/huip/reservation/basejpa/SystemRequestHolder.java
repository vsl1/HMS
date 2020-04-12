package com.biboheart.huip.reservation.basejpa;

public class SystemRequestHolder {
	private final static ThreadLocal<SystemRequest> systemRequesthreadLocal = new ThreadLocal<SystemRequest>();

	public static void initRequestHolder(SystemRequest systemRequest) {
		systemRequesthreadLocal.set(systemRequest);
	}

	public static SystemRequest getSystemRequest() {
		return systemRequesthreadLocal.get();
	}

	public static void remove() {
		systemRequesthreadLocal.remove();
	}

	public static Integer getRequestPageOffset() {
		Integer pageOffset = SystemRequestHolder.getSystemRequest().getPageOffset();
		if (pageOffset == null || pageOffset < 1) {
			pageOffset = 1;
		}
		return pageOffset - 1;
	}

	public static Integer getRequestPageSize() {
		Integer pageSize = SystemRequestHolder.getSystemRequest().getPageSize();
		if (pageSize == null || pageSize < 0) {
			pageSize = SystemRequest.DEFAULT_PAGE_SIZE;
		}
		return pageSize;
	}
}
