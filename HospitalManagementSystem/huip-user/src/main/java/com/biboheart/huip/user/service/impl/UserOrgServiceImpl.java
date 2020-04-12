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
import com.biboheart.huip.user.domain.Org;
import com.biboheart.huip.user.domain.UserOrg;
import com.biboheart.huip.user.repository.OrgRepository;
import com.biboheart.huip.user.repository.UserOrgRepository;
import com.biboheart.huip.user.service.UserOrgService;

@Service
public class UserOrgServiceImpl implements UserOrgService {
	@Autowired
	private UserOrgRepository userOrgRepository;
	@Autowired
	private OrgRepository orgRepository;

	@Override
	public UserOrg save(UserOrg uo) throws BhException {
		if (null == uo.getId()) {
			uo.setId(0L);
		}
		if (CheckUtils.isEmpty(uo.getUid())) {
			throw new BhException("User cannot be null");
		}
		if (CheckUtils.isEmpty(uo.getOid())) {
			throw new BhException("Organization cannot be null");
		}
		Org org = orgRepository.findById(uo.getOid()).get();
		if (null == org) {
			throw new BhException("Organization don't exist");
		}
		UserOrg source = userOrgRepository.findByUidAndOid(uo.getUid(), uo.getOid());
		if (null != source) {
			uo.setId(source.getId());
		}
		uo.setOtid(org.getOtid());
		uo.setOtname(org.getOtname());
		uo.setOname(org.getName());
		uo.setOpath(org.getPath());
		uo = userOrgRepository.save(uo);
		return uo;
	}

	@Override
	public UserOrg delete(Long id, Long uid, Integer oid) {
		UserOrg uo = null;
		if (null == uo && !CheckUtils.isEmpty(uid) && !CheckUtils.isEmpty(oid)) {
			uo = userOrgRepository.findByUidAndOid(uid, oid);
		}
		if (null == uo && !CheckUtils.isEmpty(id)) {
			uo = userOrgRepository.findById(id).get();
		}
		if (null != uo) {
			userOrgRepository.delete(uo);
		}
		return uo;
	}

	@Override
	public List<UserOrg> list(Long uid) {
		Specification<UserOrg> spec = processSpec(uid);
		List<UserOrg> uos = userOrgRepository.findAll(spec);
		return uos;
	}
	
	private Specification<UserOrg> processSpec(Long uid) {
		return new Specification<UserOrg>() {
			private static final long serialVersionUID = -7996418398519618907L;

			@Override
			public Predicate toPredicate(Root<UserOrg> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> pres = new ArrayList<>();
				if(!CheckUtils.isEmpty(uid)) {
					pres.add(cb.equal(root.get("uid"), uid));
				}
				if(CheckUtils.isEmpty(pres)) {
					return null;
				}
				return cb.and(pres.toArray(new Predicate[pres.size()]));
			}
		};
	}

}
