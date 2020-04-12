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
import com.biboheart.brick.utils.ListUtils;
import com.biboheart.brick.utils.PrimaryTransverter;
import com.biboheart.brick.utils.TimeUtils;
import com.biboheart.huip.user.domain.Authority;
import com.biboheart.huip.user.domain.Role;
import com.biboheart.huip.user.repository.AuthorityRepository;
import com.biboheart.huip.user.repository.RoleRepository;
import com.biboheart.huip.user.repository.UserRoleRepository;
import com.biboheart.huip.user.service.AuthorityService;

@Service
public class AuthorityServiceImpl implements AuthorityService {
	@Autowired
	private AuthorityRepository authorityRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Authority save(Authority authority) throws BhException {
		if (null == authority.getId()) {
			authority.setId(0);
		}
		if (null == authority.getPid()) {
			authority.setPid(0);
		}
		if (CheckUtils.isEmpty(authority.getName())) {
			throw new BhException("Authority name cannot be null");
		}
		if (null != authorityRepository.findByNameAndPidAndIdNot(authority.getName(), authority.getPid(), authority.getId())) {
			throw new BhException("Authority name already existed");
		}
		Long now = TimeUtils.getCurrentTimeInMillis();
		if (CheckUtils.isEmpty(authority.getSn())) {
			authority.setSn("authority_" + now);
		}
		authority = authorityRepository.save(authority);
		return authority;
	}

	@Override
	public Authority delete(Integer id) {
		Authority authority = null;
		if (null == authority && !CheckUtils.isEmpty(id)) {
			authority = authorityRepository.findById(id).get();
		}
		if (null != authority) {
			authorityRepository.delete(authority);
		}
		return authority;
	}

	@Override
	public Authority load(Integer id, String sn) {
		Authority authority = null;
		if (null == authority && !CheckUtils.isEmpty(id)) {
			authority = authorityRepository.findById(id).get();
		}
		if (null == authority && !CheckUtils.isEmpty(sn)) {
			authority = authorityRepository.findBySnAndIdNot(sn, 0);
		}
		return authority;
	}

	@Override
	public List<Authority> list(List<Integer> inIdList, List<Integer> inPidList, Long uid) {
		if (!CheckUtils.isEmpty(uid)) {
			List<Integer> ridList = userRoleRepository.findRidByUid(uid);
			List<Integer> userAidList = new ArrayList<>();
			if (!CheckUtils.isEmpty(ridList)) {
				List<Role> roles = roleRepository.findAllById(ridList);
				for (Role role : roles) {
					String aids = role.getAids();
					if (CheckUtils.isEmpty(aids)) {
						continue;
					}
					ListUtils.mergeList(userAidList, PrimaryTransverter.idsStr2List(aids));
				}
			}
			if (CheckUtils.isEmpty(userAidList)) {
				inIdList = new ArrayList<>();
				inIdList.add(0);
			} else {
				if (CheckUtils.isEmpty(inIdList)) {
					inIdList = userAidList;
				} else {
					inIdList = ListUtils.intersectionList(inIdList, userAidList);
				}
			}
		}
		Specification<Authority> spec = processSpec(inIdList, inPidList);
		List<Authority> as = authorityRepository.findAll(spec);
		return as;
	}
	
	private Specification<Authority> processSpec(List<Integer> inIdList, List<Integer> inPidList) {
		return new Specification<Authority>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Authority> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> pres = new ArrayList<>();
				if(!CheckUtils.isEmpty(inIdList)) {
					pres.add(root.get("id").in(inIdList));
				}
				if(!CheckUtils.isEmpty(inPidList)) {
					pres.add(root.get("pid").in(inPidList));
				}
				if(CheckUtils.isEmpty(pres)) {
					return null;
				}
				return cb.and(pres.toArray(new Predicate[pres.size()]));
			}
		};
	}

}
