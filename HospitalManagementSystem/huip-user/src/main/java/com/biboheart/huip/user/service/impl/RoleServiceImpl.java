package com.biboheart.huip.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.utils.BeanUtils;
import com.biboheart.brick.utils.CheckUtils;
import com.biboheart.brick.utils.TimeUtils;
import com.biboheart.huip.user.domain.Role;
import com.biboheart.huip.user.repository.RoleRepository;
import com.biboheart.huip.user.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Role save(Role role) throws BhException {
		if (null == role.getId()) {
			role.setId(0);
		}
		if (CheckUtils.isEmpty(role.getName())) {
			throw new BhException("Name cannot be null");
		}
		Long now = TimeUtils.getCurrentTimeInMillis();
		if (CheckUtils.isEmpty(role.getSn())) {
			role.setSn("role_" + now);
		}
		if (CheckUtils.isEmpty(role.getCreateTime())) {
			role.setCreateTime(now);
		}
		List<String> scope = new ArrayList<>();
		scope.add("aids");
		BeanUtils.compareObjCommaString2BraceString(role, scope);
		role.setUpdateTime(now);
		role = roleRepository.save(role);
		return role;
	}

	@Override
	public Role delete(Integer id) {
		Role role = null;
		if (null == role && !CheckUtils.isEmpty(id)) {
			role = roleRepository.findById(id).get();
		}
		if (null == role) {
			roleRepository.delete(role);
		}
		return role;
	}

	@Override
	public Role load(Integer id, String sn) {
		Role role = null;
		if (null == role && !CheckUtils.isEmpty(sn)) {
			role = roleRepository.findBySnAndIdNot(sn, 0);
		}
		if (null == role && !CheckUtils.isEmpty(id)) {
			role = roleRepository.findById(id).get();
		}
		return role;
	}

	@Override
	public List<Role> list(List<Integer> inIdList, List<String> inSnList) {
		Specification<Role> spec = processSpec(inIdList, inSnList, null);
		List<Role> roles = roleRepository.findAll(spec);
		return roles;
	}

	@Override
	public Page<Role> find(List<Integer> inIdList, List<String> inSnList, String match) {
		Specification<Role> spec = processSpec(inIdList, inSnList, match);
		Page<Role> roles = roleRepository.findAll(spec, (Pageable) null);
		return roles;
	}
	
	private Specification<Role> processSpec(List<Integer> inIdList, List<String> inSnList, String match) {
		return new Specification<Role>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> pres = new ArrayList<>();
				if(!CheckUtils.isEmpty(inIdList)) {
					pres.add(root.get("id").in(inIdList));
				}
				if(!CheckUtils.isEmpty(inSnList)) {
					pres.add(root.get("sn").in(inSnList));
				}
				if(!CheckUtils.isEmpty(match)) {
					pres.add(cb.or(
							cb.like(root.get("sn"), "%" + match + "%"),
							cb.like(root.get("name"), "%" + match + "%")
							));
				}
				if(CheckUtils.isEmpty(pres)) {
					return null;
				}
				return cb.and(pres.toArray(new Predicate[pres.size()]));
			}
		};
	}

}
