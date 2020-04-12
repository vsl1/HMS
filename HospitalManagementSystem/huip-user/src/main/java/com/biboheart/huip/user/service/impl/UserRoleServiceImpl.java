package com.biboheart.huip.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.utils.CheckUtils;
import com.biboheart.huip.user.domain.Role;
import com.biboheart.huip.user.domain.UserRole;
import com.biboheart.huip.user.repository.RoleRepository;
import com.biboheart.huip.user.repository.UserRoleRepository;
import com.biboheart.huip.user.service.UserRoleService;

@Service
public class UserRoleServiceImpl implements UserRoleService {
	@Autowired
	private UserRoleRepository userRolerePository;
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public UserRole save(UserRole ur) throws BhException {
		if (null == ur.getId()) {
			ur.setId(0L);
		}
		if (CheckUtils.isEmpty(ur.getUid())) {
			throw new BhException("User cannot be null");
		}
		if (CheckUtils.isEmpty(ur.getRid())) {
			throw new BhException("Role cannot be null");
		}
		UserRole source = userRolerePository.findByUidAndRid(ur.getUid(), ur.getRid());
		if (null != source) {
			ur.setId(source.getId());
		}
		Role role = roleRepository.findById(ur.getRid()).get();
		if (null != role) {
			ur.setRname(role.getName());
		}
		ur = userRolerePository.save(ur);
		return ur;
	}

	@Override
	public UserRole delete(Long uid, Integer rid) {
		UserRole ur = null;
		if (null == ur && !CheckUtils.isEmpty(uid) && !CheckUtils.isEmpty(rid)) {
			ur = userRolerePository.findByUidAndRid(uid, rid);
		}
		if (null != ur) {
			userRolerePository.delete(ur);
		}
		return ur;
	}

	@Override
	public List<UserRole> list(List<Long> inUidList, List<Integer> inRidList) {
		Specification<UserRole> spec = processSpec(inUidList, inRidList);
		List<UserRole> urs = userRolerePository.findAll(spec);
		return urs;
	}
	
	private Specification<UserRole> processSpec(List<Long> inUidList, List<Integer> inRidList) {
		return new Specification<UserRole>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<UserRole> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> pres = new ArrayList<>();
				if(!CheckUtils.isEmpty(inUidList)) {
					pres.add(root.get("uid").in(inUidList));
				}
				if(!CheckUtils.isEmpty(inRidList)) {
					pres.add(root.get("rid").in(inRidList));
				}
				if(CheckUtils.isEmpty(pres)) {
					return null;
				}
				return cb.and(pres.toArray(new Predicate[pres.size()]));
			}
		};
	}

}
