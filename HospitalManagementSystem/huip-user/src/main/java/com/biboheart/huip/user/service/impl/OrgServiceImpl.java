package com.biboheart.huip.user.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.biboheart.brick.utils.CheckUtils;
import com.biboheart.brick.utils.ListUtils;
import com.biboheart.brick.utils.TimeUtils;
import com.biboheart.huip.user.domain.Org;
import com.biboheart.huip.user.domain.OrgType;
import com.biboheart.huip.user.repository.OrgRepository;
import com.biboheart.huip.user.repository.OrgTypeRepository;
import com.biboheart.huip.user.repository.OrgTypeRuleRepository;
import com.biboheart.huip.user.service.OrgService;

@Service
public class OrgServiceImpl implements OrgService {
	@Autowired
	private OrgRepository orgRepository;
	@Autowired
	private OrgTypeRepository orgTypeRepository;
	@Autowired
	private OrgTypeRuleRepository orgTypeRuleRepository;

	@Override
	public Org save(Org org) throws BhException {
		if (null == org.getId()) {
			org.setId(0);
		}
		if (null == org.getPid()) {
			org.setPid(0);
		}
		if (CheckUtils.isEmpty(org.getOtid())) {
			throw new BhException("组织类型不能为空");
		}
		if (CheckUtils.isEmpty(org.getName())) {
			throw new BhException("名称不能为空");
		}
		if (null != orgRepository.findByNameAndPidAndIdNot(org.getName(), org.getPid(), org.getId())) {
			throw new BhException("组织名称已经存在");
		}
		if (!CheckUtils.isEmpty(org.getPid()) && org.getPid().equals(org.getId())) {
			throw new BhException("父组织不能是自己");
		}
		OrgType ot = orgTypeRepository.findById(org.getOtid()).get();
		if (null == ot) {
			throw new BhException("不可用的组织类型");
		}
		Org parent = null;
		if (!CheckUtils.isEmpty(org.getPid())) {
			parent = orgRepository.findById(org.getPid()).get();
		}
		if (null != parent) {
			Integer potid = parent.getOtid();
			List<Integer> inPotidList = new ArrayList<>();
			inPotidList.add(potid);
			List<Integer> cotidList = orgTypeRuleRepository.findCidsByPidIn(inPotidList);
			if (!CheckUtils.isEmpty(cotidList) && !cotidList.contains(org.getOtid())) {
				throw new BhException("父组织中不允许添加此类型的组织");
			}
			org.setPname(parent.getName());
			List<Org> orgs = new ArrayList<>();
			orgs.add(parent);
			List<Integer> pids = new ArrayList<>();
			getParentsId(orgs, pids, null);
			if (!CheckUtils.isEmpty(pids)) {
				orgs = orgRepository.findAllById(pids);
			} else {
				orgs = null;
			}
			String path = "";
			if (!CheckUtils.isEmpty(orgs)) {
				for (Org item : orgs) {
					path += ("".equals(path) ? "" : ">") + item.getName();
				}
			}
			path += ("".equals(path) ? "" : ">") + parent.getName();
			path += ("".equals(path) ? "" : ">") + org.getName();
			org.setPath(path);
		} else {
			org.setPath(org.getName());
		}
		if(CheckUtils.isEmpty(org.getSn())) {
			org.setSn("org_" + TimeUtils.getCurrentTimeInMillis());
		}
		org.setOtsn(ot.getSn());
		org.setOtname(ot.getName());
		org = orgRepository.save(org);
		return org;
	}

	@Override
	public Org delete(Integer id) {
		Org org = null;
		if (null == org && !CheckUtils.isEmpty(id)) {
			org = orgRepository.findById(id).get();
		}
		if (null != org) {
			orgRepository.delete(org);
		}
		return org;
	}

	@Override
	public Org load(Integer id, String sn, Integer pid, String name, Integer parentTypeId) {
		Org org = null;
		if (null == org && !CheckUtils.isEmpty(id) && !CheckUtils.isEmpty(parentTypeId)) {
			org = orgRepository.findById(id).get();
			while (null != org && !org.getOtid().equals(parentTypeId)) {
				org = orgRepository.findById(org.getPid()).get();
			}
		}
		if (null == org && !CheckUtils.isEmpty(sn)) {
			org = orgRepository.findBySnAndIdNot(sn, 0);
		}
		if (null == org && !CheckUtils.isEmpty(pid) && !CheckUtils.isEmpty(name)) {
			org = orgRepository.findByNameAndPidAndIdNot(name, pid, 0);
		}
		if (null == org && !CheckUtils.isEmpty(id) && CheckUtils.isEmpty(parentTypeId)) {
			org = orgRepository.findById(id).get();
		}
		return org;
	}

	@Override
	public List<Org> list(List<Integer> inIdList, List<Integer> inPidList, List<Integer> inOtidList, Integer descendant,
			Integer parents, String match) {
		boolean noneOrg = false;
		if (null != inIdList && 1 == inIdList.size() && inIdList.get(0).equals(0)) {
			noneOrg = true;
		}
		if(!noneOrg && (!CheckUtils.isEmpty(descendant) || !CheckUtils.isEmpty(parents))) {
			inIdList = this.listId(inIdList, inPidList, inOtidList, descendant, parents);
		}
		if(!CheckUtils.isEmpty(inPidList) && !CheckUtils.isEmpty(descendant)) {
			inPidList = null;
		}
		Specification<Org> spec = processSpec(inIdList, inPidList, inOtidList, match);
		List<Org> orgs = orgRepository.findAll(spec);
		return orgs;
	}

	@Override
	public Page<Org> find(List<Integer> inIdList, List<Integer> inPidList, List<Integer> inOtidList, Integer descendant,
			Integer parents, String match) {
		boolean noneOrg = false;
		if (null != inIdList && 1 == inIdList.size() && inIdList.get(0).equals(0)) {
			noneOrg = true;
		}
		if(!noneOrg && (!CheckUtils.isEmpty(descendant) || !CheckUtils.isEmpty(parents))) {
			inIdList = this.listId(inIdList, inPidList, inOtidList, descendant, parents);
		}
		if(!CheckUtils.isEmpty(inPidList) && !CheckUtils.isEmpty(descendant)) {
			inPidList = null;
		}
		Specification<Org> spec = processSpec(inIdList, inPidList, inOtidList, match);
		Page<Org> orgs = orgRepository.findAll(spec, (Pageable)null);
		return orgs;
	}

	@Override
	public List<Integer> listId(List<Integer> inIdList, List<Integer> inPidList, List<Integer> inOtidList,
			Integer descendant, Integer parents) {
		List<Integer> ids = new ArrayList<>();
		if(!CheckUtils.isEmpty(descendant)) {
			if(CheckUtils.isEmpty(inPidList)) {
				inPidList = inIdList;
			}
			List<Org> all = orgRepository.findAll();
			Map<Integer, List<Org>> map = org2map(all);
			List<Integer> descendantIdList = new ArrayList<>();
			getChildrenId(map, inPidList, descendantIdList, inOtidList);
			if(CheckUtils.isEmpty(inIdList)) {
				inIdList = descendantIdList;
			} else {
				inIdList = ListUtils.intersectionList(inIdList, descendantIdList);
			}
			ListUtils.mergeList(inIdList, inPidList);
			inPidList = null;
		}
		List<Integer> inTypeIdList = null;
		if(CheckUtils.isEmpty(parents)) {
			inTypeIdList = inOtidList;
		}
		Specification<Org> spec = processSpec(inIdList, inPidList, inTypeIdList, null);
		List<Org> orgs = orgRepository.findAll(spec);
		if(!CheckUtils.isEmpty(orgs)) {
			for (Org org : orgs) {
				if(!CheckUtils.isEmpty(inOtidList) && !inOtidList.contains(org.getOtid())) {
					continue;
				}
				if(!ids.contains(org.getId())) {
					ids.add(org.getId());
				}
			}
		}
		if(!CheckUtils.isEmpty(parents)) {
			List<Integer> pids = new ArrayList<>();
			getParentsId(orgs, pids, inOtidList);
			if(!CheckUtils.isEmpty(pids)) {
				ListUtils.mergeList(ids, pids);
			}
		}
		return ids;
	}
	
	private Specification<Org> processSpec(List<Integer> inIdList, List<Integer> inPidList, List<Integer> inOtidList, String match) {
		return new Specification<Org>() {
			private static final long serialVersionUID = -7996418398519618907L;

			@Override
			public Predicate toPredicate(Root<Org> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> pres = new ArrayList<>();
				if(!CheckUtils.isEmpty(inIdList)) {
					pres.add(root.get("id").in(inIdList));
				}
				if(!CheckUtils.isEmpty(inPidList)) {
					pres.add(root.get("pid").in(inPidList));
				}
				if(!CheckUtils.isEmpty(inOtidList)) {
					pres.add(root.get("otid").in(inOtidList));
				}
				if(!CheckUtils.isEmpty(match)) {
					pres.add(cb.or(
							cb.like(root.get("sn"), "%" + match + "%"),
							cb.like(root.get("name"), "%" + match + "%"),
							cb.like(root.get("otname"), "%" + match + "%")
							));
				}
				if(CheckUtils.isEmpty(pres)) {
					return null;
				}
				return cb.and(pres.toArray(new Predicate[pres.size()]));
			}
		};
	}
	
	private Map<Integer, List<Org>> org2map(List<Org> orgs) {
		Map<Integer, List<Org>> maps = new HashMap<Integer, List<Org>>();
		List<Org> os = null;
		for (Org org : orgs) {
			if(CheckUtils.isEmpty(org.getPid())) {
				os = new ArrayList<Org>();
				maps.put(org.getId(), os);
			} else {
				if (maps.containsKey(org.getPid())) {
					maps.get(org.getPid()).add(org);
				} else {
					os = new ArrayList<Org>();
					if (!os.contains(org))
						os.add(org);
					maps.put(org.getPid(), os);
				}
			}
		}
		return maps;
	}
	
	private void getChildrenId(Map<Integer, List<Org>> map, List<Integer> pids, List<Integer> result, List<Integer> typeIds) {
		if(CheckUtils.isEmpty(pids)) {
			return;
		}
		for(Integer pid : pids) {
			if(CheckUtils.isEmpty(pid)) {
				continue;
			}
			List<Org> torgs = map.get(pid);
			if(CheckUtils.isEmpty(torgs)) {
				continue;
			}
			List<Integer> next = new ArrayList<>();
			for (Org org : torgs) {
				if(pids.contains(org.getId())) {
					continue;
				}
				if (map.containsKey(org.getId())) {
					next.add(org.getId());
				}
				if(CheckUtils.isEmpty(typeIds)) {
					if(!result.contains(org.getId())) {
						result.add(org.getId());
					}
				} else {
					if(!result.contains(org.getId()) && typeIds.contains(org.getOtid())) {
						result.add(org.getId());
					}
				}
			}
			if(!result.contains(pid)) {
				result.add(pid);
			}
			if(!CheckUtils.isEmpty(next)) {
				getChildrenId(map, next, result, typeIds);
			}
		}
	}
	
	private void getParentsId(List<Org> orgs, List<Integer> result, List<Integer> typeIds) {
		if(CheckUtils.isEmpty(orgs)) {
			return;
		}
		List<Integer> pids = new ArrayList<>();
		for(Org org : orgs) {
			if(!CheckUtils.isEmpty(org.getPid()) && !pids.contains(org.getPid())) {
				pids.add(org.getPid());
			}
		}
		if(CheckUtils.isEmpty(pids)) {
			return;
		}
		List<Org> next = orgRepository.findAllById(pids);
		if(CheckUtils.isEmpty(next)) {
			return;
		}
		for(Org org : next) {
			if(CheckUtils.isEmpty(typeIds)) {
				if(!result.contains(org.getId())) {
					result.add(org.getId());
				}
			} else {
				if(!result.contains(org.getId()) && typeIds.contains(org.getOtid())) {
					result.add(org.getId());
				}
			}
		}
		getParentsId(next, result, typeIds);
	}

}
