package com.biboheart.huip.reservation.basejpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class CustomRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID>
		implements CustomRepository<T, ID> {

	private final EntityManager em;

	public CustomRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.em = em;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> listBySQL(String sql) {
		return em.createNativeQuery(sql).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> listByHQL(String hql) {
		return em.createQuery(hql).getResultList();
	}
	
	@Override
    public void updateBySql(String sql, Object...args) {
		Query query = em.createNativeQuery(sql);
        int i = 0;
        for(Object arg : args) {
            query.setParameter(++i, arg);
        }
        query.executeUpdate();
	}
	
	@Override
    public void updateByHql(String hql, Object...args) {
		Query query = em.createQuery(hql);
        int i = 0;
        for(Object arg : args) {
            query.setParameter(++i, arg);
        }
        query.executeUpdate();
	}
	
	public List<T> findAll() {
		Sort sort = getRequestSort();
		if(null != sort) {
			return super.findAll(sort);
		}
		return super.findAll();
	}
	
	public Page<T> findAll(Pageable pageable) {
		if(null == pageable) {
			pageable = getRequestPageable();
		}
		return super.findAll(pageable);
	}
	
	public List<T> findAll(Specification<T> spec) {
		Sort sort = getRequestSort();
		if(null != sort) {
			return super.findAll(spec, sort);
		}
		return super.findAll(spec);
	}
	
	public Page<T> findAll(Specification<T> spec, Pageable pageable) {
		if(null == pageable) {
			pageable = getRequestPageable();
		}
		return super.findAll(spec, pageable);
	}
	
	@Override
	public <S extends T> List<S> findAll(Example<S> example) {
		Sort sort = getRequestSort();
		if(null != sort) {
			return super.findAll(example, sort);
		}
		return super.findAll(example);
	}

	@Override
	public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
		if(null == pageable) {
			pageable = getRequestPageable();
		}
		return super.findAll(example, pageable);
	}
	
	// 取请求中的排序属性
	private Sort getRequestSort() {
		SystemRequest sr = SystemRequestHolder.getSystemRequest();
		if(null == sr) {
			return null;
		}
		String order = sr.getOrder();
		String sort = sr.getSort();
		if(null == sort || "".equals(sort)) {
			return null;
		}
		if(null != order && !"".equals(order)) {
			order = order.toLowerCase();
		}
		sort = sort.replaceAll("\\s*", "");
		String[] atts = sort.split(",");
		if(0 >= atts.length){
			return null;
		}
		if(null == order || "".equals(order)) {
			order = "desc";
		}
		String[] orderTypes = order.split(",");
		List<Order> orders=new ArrayList<Order>();
		for(int i = 0; i < atts.length; i ++) {
			String property = null;
			Class<T> clazz = this.getDomainClass();
			try {
				clazz.getDeclaredField(atts[i]);
				property = atts[i];
			} catch (NoSuchFieldException | SecurityException e) {
				property = null;
			}
			if(property == null) {
				continue;
			}
			Direction direction = null;
			if(i < orderTypes.length) {
				direction = "desc".equals(orderTypes[i]) ? Direction.DESC : Direction.ASC;
			} else {
				direction = "desc".equals(orderTypes[orderTypes.length - 1]) ? Direction.DESC : Direction.ASC;
			}
			orders.add(new Order(direction, property));
		}
		if(0 >= orders.size()) {
			return null;
		}
		return Sort.by(orders);
	}

	// 取请求中的分页属性
	private Pageable getRequestPageable() {
		Sort sort = getRequestSort();
		if(null == sort) {
			return PageRequest.of(getRequestPageOffset(), getRequestPageSize());
		} else {
			return PageRequest.of(getRequestPageOffset(), getRequestPageSize(), sort);
		}
	}
	
	protected SystemRequest getSystemRequest() {
		SystemRequest sr = SystemRequestHolder.getSystemRequest();
		if (sr == null){
			sr = new SystemRequest();
		}
		return sr;
	}
	
	private Integer getRequestPageOffset() {
		Integer pageOffset = getSystemRequest().getPageOffset();
		if (pageOffset == null || pageOffset < 1) {
			pageOffset = 1;
		}
		return pageOffset - 1;
	}
	
	private Integer getRequestPageSize() {
		Integer pageSize = getSystemRequest().getPageSize();
		if (pageSize == null || pageSize < 0) {
			pageSize = SystemRequest.DEFAULT_PAGE_SIZE;
		}
		return pageSize;
	}
}
