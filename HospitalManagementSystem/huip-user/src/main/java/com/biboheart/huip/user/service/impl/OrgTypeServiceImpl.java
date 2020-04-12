package com.biboheart.huip.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.utils.CheckUtils;
import com.biboheart.brick.utils.ListUtils;
import com.biboheart.huip.user.domain.OrgType;
import com.biboheart.huip.user.domain.OrgTypeRule;
import com.biboheart.huip.user.repository.OrgRepository;
import com.biboheart.huip.user.repository.OrgTypeRepository;
import com.biboheart.huip.user.repository.OrgTypeRuleRepository;
import com.biboheart.huip.user.service.OrgTypeService;

@Service
public class OrgTypeServiceImpl implements OrgTypeService {
	@Autowired
	private OrgTypeRepository orgTypeRepository;
	@Autowired
	private OrgTypeRuleRepository orgTypeRuleRepository;
	@Autowired
	private OrgRepository orgRepository;

	@Override
	public OrgType save(OrgType orgType) throws BhException {
		if(CheckUtils.isEmpty(orgType.getId())) {
			orgType.setId(0);
		}
		if (CheckUtils.isEmpty(orgType.getSn())) {
			throw new BhException("Type number cannot be null");
		}
		if(CheckUtils.isEmpty(orgType.getName())) {
			throw new BhException("Type name cannot by null");
		}
		if(null != orgTypeRepository.findBySnAndIdNot(orgType.getSn(), orgType.getId())) {
			throw new BhException("Number already existed");
		}
		if(null != orgTypeRepository.findByNameAndIdNot(orgType.getName(), orgType.getId())) {
			throw new BhException("Number already existed");
		}
		orgType = orgTypeRepository.save(orgType);
		return orgType;
	}

	@Override
	public OrgType delete(Integer id, String sn) throws BhException {
		OrgType ot = null;
		if (null == ot && !CheckUtils.isEmpty(sn)) {
			ot = orgTypeRepository.findBySnAndIdNot(sn, 0);
		}
		if (null == ot && !CheckUtils.isEmpty(id)) {
			ot = orgTypeRepository.findById(id).get();
		}
		if (null != ot) {
			if (!CheckUtils.isEmpty(orgRepository.countByOtid(ot.getId()))) {
				throw new BhException("Cannot delete used type");
			}
			orgTypeRepository.delete(ot);
		}
		return ot;
	}

	@Override
	public OrgType load(Integer id, String sn) {
		OrgType ot = null;
		if (null == ot && !CheckUtils.isEmpty(sn)) {
			ot = orgTypeRepository.findBySnAndIdNot(sn, 0);
		}
		if (null == ot && !CheckUtils.isEmpty(id)) {
			ot = orgTypeRepository.findById(id).get();
		}
		return ot;
	}

	@Override
	public List<OrgType> list(List<Integer> ids, List<String> sns, List<Integer> pidList, Integer descendant,
			Integer self) {
		if(CheckUtils.isEmpty(ids) && CheckUtils.isEmpty(sns) && CheckUtils.isEmpty(pidList)) {
			return orgTypeRepository.findAll();
		}
		if(!CheckUtils.isEmpty(sns)) {
			List<Integer> idList = orgTypeRepository.findIdsBySnIn(sns);
			if(CheckUtils.isEmpty(ids)) {
				ids = idList;
			} else {
				ids = ListUtils.intersectionList(ids, idList);
			}
		}
		if(!CheckUtils.isEmpty(pidList)) {
			if(CheckUtils.isEmpty(descendant)) {
				List<Integer> cidList = orgTypeRuleRepository.findCidsByPidIn(pidList);
				if(!CheckUtils.isEmpty(self)) {
					ListUtils.mergeList(cidList, pidList);
				}
				if(CheckUtils.isEmpty(ids)) {
					ids = cidList;
				} else {
					ids = ListUtils.intersectionList(ids, cidList);
				}
			} else {
				List<Integer> cidList = new ArrayList<>();
				listCid(pidList, cidList);
				if(!CheckUtils.isEmpty(self)) {
					ListUtils.mergeList(cidList, pidList);
				}
				if(CheckUtils.isEmpty(ids)) {
					ids = cidList;
				} else {
					ids = ListUtils.intersectionList(ids, cidList);
				}
			}
		}
		if(CheckUtils.isEmpty(ids)) {
			return null;
		}
		return orgTypeRepository.findAllById(ids);
	}

	@Override
	public List<Integer> listId(List<String> sns, List<Integer> pidList, Integer descendant, Integer self) {
		List<Integer> idList = new ArrayList<>();
		if(CheckUtils.isEmpty(sns) && CheckUtils.isEmpty(pidList)) {
			return orgTypeRepository.findAllIds();
		}
		if(!CheckUtils.isEmpty(sns)) {
			idList = orgTypeRepository.findIdsBySnIn(sns);
		}
		if(!CheckUtils.isEmpty(pidList)) {
			if(CheckUtils.isEmpty(descendant)) {
				List<Integer> cidList = orgTypeRuleRepository.findCidsByPidIn(pidList);
				if(!CheckUtils.isEmpty(self)) {
					ListUtils.mergeList(cidList, pidList);
				}
				if(CheckUtils.isEmpty(idList)) {
					idList = cidList;
				} else {
					idList = ListUtils.intersectionList(idList, cidList);
				}
			} else {
				List<Integer> cidList = new ArrayList<>();
				listCid(pidList, cidList);
				if(!CheckUtils.isEmpty(self)) {
					ListUtils.mergeList(cidList, pidList);
				}
				if(CheckUtils.isEmpty(idList)) {
					idList = cidList;
				} else {
					idList = ListUtils.intersectionList(idList, cidList);
				}
			}
		}
		return idList;
	}

	@Override
	public OrgTypeRule addOrgTypeRule(Integer pid, Integer cid) throws BhException {
		if(CheckUtils.isEmpty(pid) || CheckUtils.isEmpty(cid)) {
			throw new BhException("Wrong parameter");
		}
		OrgTypeRule otr = orgTypeRuleRepository.findByPidAndCid(pid, cid);
		if(null != otr) {
			return otr;
		}
		otr = new OrgTypeRule();
		otr.setPid(pid);
		otr.setCid(cid);
		return orgTypeRuleRepository.save(otr);
	}

	@Override
	public OrgTypeRule deleteOrgTypeRule(Integer pid, Integer cid) throws BhException {
		if(CheckUtils.isEmpty(pid) || CheckUtils.isEmpty(cid)) {
			throw new BhException("Wrong parameter");
		}
		OrgTypeRule otr = orgTypeRuleRepository.findByPidAndCid(pid, cid);
		if(null == otr) {
			return null;
		}
		orgTypeRuleRepository.delete(otr);
		return otr;
	}
	
	private void listCid(List<Integer> pidList, List<Integer> cidList) {
		if(CheckUtils.isEmpty(pidList)) {
			return;
		}
		if(null == cidList) {
			cidList = new ArrayList<>();
		}
		List<Integer> tcids = orgTypeRuleRepository.findCidsByPidIn(pidList);
		if(CheckUtils.isEmpty(tcids)) {
			return;
		}
		ListUtils.mergeList(cidList, tcids);
		listCid(tcids, cidList);
	}

}
