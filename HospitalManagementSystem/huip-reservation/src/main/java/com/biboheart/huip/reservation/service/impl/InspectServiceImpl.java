package com.biboheart.huip.reservation.service.impl;

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
import com.biboheart.brick.utils.CheckUtils;
import com.biboheart.brick.utils.TimeUtils;
import com.biboheart.huip.reservation.domain.Device;
import com.biboheart.huip.reservation.domain.Patient;
import com.biboheart.huip.reservation.domain.Project;
import com.biboheart.huip.reservation.domain.Inspect;
import com.biboheart.huip.reservation.repository.DeviceRepository;
import com.biboheart.huip.reservation.repository.PatientRepository;
import com.biboheart.huip.reservation.repository.ProjectRepository;
import com.biboheart.huip.reservation.repository.InspectRepository;
import com.biboheart.huip.reservation.service.InspectService;

@Service
public class InspectServiceImpl implements InspectService {
	@Autowired
	private InspectRepository inspectRepository;
	@Autowired
	private PatientRepository patientRepository;
	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private ProjectRepository projectRepository;

	@Override
	public Inspect save(Inspect inspect) throws BhException {
		if (null == inspect.getId()) {
			inspect.setId(0);
		}
		if (CheckUtils.isEmpty(inspect.getPat()) && CheckUtils.isEmpty(inspect.getPatName())) {
			throw new BhException("Patient cannot be null");
		}
		if (CheckUtils.isEmpty(inspect.getProject())) {
			throw new BhException("Project cannot be null");
		}
		if (CheckUtils.isEmpty(inspect.getDevice())) {
			throw new BhException("Decive cannot be null");
		}
		Long now = TimeUtils.getCurrentTimeInMillis();
		if (CheckUtils.isEmpty(inspect.getSn())) {
			inspect.setSn("ud_" + now);
		}
		if (null != inspectRepository.findBySnAndIdNot(inspect.getSn(), inspect.getId())) {
			throw new BhException("Number already existed");
		}
		Inspect source = inspectRepository.findByPatAndDeviceAndProject(inspect.getPat(), inspect.getDevice(),
				inspect.getProject());
		if (null != source) {
			inspect.setId(source.getId());
			inspect.setCreateTime(source.getCreateTime());
		}
		if (!CheckUtils.isEmpty(inspect.getPat())) {
			Patient patient = patientRepository.findByPat(inspect.getPat());
			if (null != patient) {
				inspect.setPatName(patient.getName());
				inspect.setPatSn(patient.getSn());
			}
		}
		Device device = null;
		try {
			device = deviceRepository.findById(inspect.getDevice()).get();
		} catch (Exception e) {
			device = null;
		}
		if (null == device) {
			throw new BhException("Decive cannot use or exist");
		}
		Project project = null;
		try {
			project = projectRepository.findById(inspect.getProject()).get();
		} catch (Exception e) {
			project = null;
		}
		if (null == project) {
			throw new BhException("Project don't exist");
		}
		inspect.setDsn(device.getSn());
		inspect.setDname(device.getName());
		inspect.setPsn(project.getSn());
		inspect.setPname(project.getName());
		if (null == inspect.getCharged()) {
			inspect.setCharged(0);
		}
		if (CheckUtils.isEmpty(inspect.getState())) {
			inspect.setState(1);
		}
		if (CheckUtils.isEmpty(inspect.getCreateTime())) {
			inspect.setCreateTime(now);
		}
		if (inspect.getState() >= 2 && CheckUtils.isEmpty(inspect.getTime())) {
			inspect.setTime(now);
		}
		inspect.setUpeateTime(now);
		inspect = inspectRepository.save(inspect);
		return inspect;
	}

	@Override
	public Inspect delete(Integer id, String sn, Long pat, Integer device, Integer project) {
		Inspect inspect = null;
		if (null == inspect && !CheckUtils.isEmpty(pat) && !CheckUtils.isEmpty(device) && !CheckUtils.isEmpty(project)) {
			inspect = inspectRepository.findByPatAndDeviceAndProject(pat, device, project);
		}
		if (null == inspect && !CheckUtils.isEmpty(sn)) {
			inspect = inspectRepository.findBySnAndIdNot(sn, 0);
		}
		if (null == inspect && !CheckUtils.isEmpty(id)) {
			try {
				inspect = inspectRepository.findById(id).get();
			} catch (Exception e) {
				inspect = null;
			}
		}
		if (null != inspect) {
			inspectRepository.delete(inspect);
		}
		return inspect;
	}

	@Override
	public Inspect load(Integer id, String sn, Long pat, Integer device, Integer project) {
		Inspect inspect = null;
		if (null == inspect && !CheckUtils.isEmpty(pat) && !CheckUtils.isEmpty(device) && !CheckUtils.isEmpty(project)) {
			inspect = inspectRepository.findByPatAndDeviceAndProject(pat, device, project);
		}
		if (null == inspect && !CheckUtils.isEmpty(sn)) {
			inspect = inspectRepository.findBySnAndIdNot(sn, 0);
		}
		if (null == inspect && !CheckUtils.isEmpty(id)) {
			try {
				inspect = inspectRepository.findById(id).get();
			} catch (Exception e) {
				inspect = null;
			}
		}
		return inspect;
	}

	@Override
	public List<Inspect> list(Long start, Long end, List<Long> inPatList, List<Integer> inDeviceList, List<Integer> inProjectList,
			String match) {
		Specification<Inspect> spec = processSpec(start, end, inPatList, inDeviceList, inProjectList, match);
		List<Inspect> inspects = inspectRepository.findAll(spec);
		return inspects;
	}

	@Override
	public Page<Inspect> find(Long start, Long end, List<Long> inPatList, List<Integer> inDeviceList, List<Integer> inProjectList,
			String match) {
		Specification<Inspect> spec = processSpec(start, end, inPatList, inDeviceList, inProjectList, match);
		Page<Inspect> inspects = inspectRepository.findAll(spec, (Pageable) null);
		return inspects;
	}

	private Specification<Inspect> processSpec(Long start, Long end, List<Long> inPatList, List<Integer> inDeviceList,
			List<Integer> inProjectList, String match) {
		return new Specification<Inspect>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Inspect> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> pres = new ArrayList<>();
				if (!CheckUtils.isEmpty(inPatList)) {
					pres.add(root.get("pat").in(inPatList));
				}
				if (!CheckUtils.isEmpty(inDeviceList)) {
					pres.add(root.get("device").in(inDeviceList));
				}
				if (!CheckUtils.isEmpty(inProjectList)) {
					pres.add(root.get("project").in(inProjectList));
				}
				if(!CheckUtils.isEmpty(start)) {
					pres.add(cb.ge(root.get("time"), start));
				}
				if(!CheckUtils.isEmpty(end)) {
					pres.add(cb.le(root.get("time"), end));
				}
				if (!CheckUtils.isEmpty(match)) {
					pres.add(cb.or(cb.like(root.get("sn"), "%" + match + "%"),
							cb.like(root.get("dname"), "%" + match + "%"),
							cb.like(root.get("pname"), "%" + match + "%"),
							cb.like(root.get("patName"), "%" + match + "%")));
				}
				if (CheckUtils.isEmpty(pres)) {
					return null;
				}
				return cb.and(pres.toArray(new Predicate[pres.size()]));
			}
		};
	}

}
