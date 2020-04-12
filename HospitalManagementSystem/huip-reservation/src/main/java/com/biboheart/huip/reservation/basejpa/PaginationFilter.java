package com.biboheart.huip.reservation.basejpa;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component("paginationFilter")
public class PaginationFilter implements Filter {
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		int offset = 0;
		int size = 10;
		String offsetStr = request.getParameter("pageOffset");
		String sizeStr = request.getParameter("pageSize");
		if(null != offsetStr && !"".equals(offsetStr)){
			offset = Integer.valueOf(offsetStr);
		}
		if(null != sizeStr && !"".equals(sizeStr)){
			size = Integer.valueOf(sizeStr);
		}
		try {
			SystemRequest systemRequest = new SystemRequest();
			systemRequest.setOrder(request.getParameter("order"));
			systemRequest.setPageOffset(offset);
			systemRequest.setPageSize(size);
			systemRequest.setRequest(request);
			systemRequest.setSort(request.getParameter("sort"));
			SystemRequestHolder.initRequestHolder(systemRequest);
			chain.doFilter(request, response);
		} finally {
			SystemRequestHolder.remove();
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void destroy() {}

}
