package com.biboheart.huip.user.basejpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
@Transactional(readOnly=true)
public interface CustomRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
	List<Object[]> listBySQL(String sql);
	List<Object[]> listByHQL(String hql);
	
	@Transactional
    public void updateBySql(String sql, Object...args);
    @Transactional
    public void updateByHql(String hql, Object...args);
}
