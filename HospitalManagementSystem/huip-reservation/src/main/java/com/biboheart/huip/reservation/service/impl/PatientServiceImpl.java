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
import com.biboheart.brick.utils.PinyinUtils;
import com.biboheart.brick.utils.TimeUtils;
import com.biboheart.huip.reservation.domain.Patient;
import com.biboheart.huip.reservation.repository.PatientRepository;
import com.biboheart.huip.reservation.service.PatientService;

@Service
public class PatientServiceImpl implements PatientService {
	@Autowired
	private PatientRepository patientRepository;

	@Override
	public Patient save(Patient patient) throws BhException {
		if (null == patient.getId()) {
			patient.setId(0);
		}
		if (CheckUtils.isEmpty(patient.getName())) {
			throw new BhException("Name cannot be null");
		}
		Long now = TimeUtils.getCurrentTimeInMillis();
		Patient source = null;
		if (!CheckUtils.isEmpty(patient.getPat())) {
			source = patientRepository.findByPat(patient.getPat());
		}
		if (null != source) {
			patient.setId(source.getId());
			patient.setCreateTime(source.getCreateTime());
			if (CheckUtils.isEmpty(patient.getSn())) {
				patient.setSn(source.getSn());
			}
		}
		if (CheckUtils.isEmpty(patient.getSn())) {
			patient.setSn("patient_" + now);
		}
		if (null != patientRepository.findBySnAndIdNot(patient.getSn(), patient.getId())) {
			throw new BhException("编号已经存在");
		}
		if (!CheckUtils.isEmpty(patient.getIdcard()) && null != patientRepository.findByIdcardAndIdNot(patient.getIdcard(), patient.getId())) {
			throw new BhException("身份证号码已经被使用");
		}
		if (CheckUtils.isEmpty(patient.getCreateTime())) {
			patient.setCreateTime(now);
		}
		patient.setSpell(PinyinUtils.strFirst2Pinyin(patient.getName()));
		patient.setUpeateTime(now);
		patient = patientRepository.save(patient);
		return patient;
	}

	@Override
	public Patient delete(Integer id, Long pat, String idcard) {
		Patient patient = null;
		if (null == patient && !CheckUtils.isEmpty(idcard)) {
			patient = patientRepository.findByIdcardAndIdNot(idcard, 0);
		}
		if (null == patient && !CheckUtils.isEmpty(pat)) {
			patient = patientRepository.findByPat(pat);
		}
		if (null == patient && !CheckUtils.isEmpty(id)) {
			try {
				patient = patientRepository.findById(id).get();
			} catch (Exception e) {
				patient = null;
			}
		}
		if (null != patient) {
			patientRepository.delete(patient);
		}
		return patient;
	}

	@Override
	public Patient load(Integer id, Long pat, String sn, String idcard) {
		Patient patient = null;
		if (null == patient && !CheckUtils.isEmpty(idcard)) {
			patient = patientRepository.findByIdcardAndIdNot(idcard, 0);
		}
		if (null == patient && !CheckUtils.isEmpty(pat)) {
			patient = patientRepository.findByPat(pat);
		}
		if (null == patient && !CheckUtils.isEmpty(sn)) {
			patient = patientRepository.findBySnAndIdNot(sn, 0);
		}
		if (null == patient && !CheckUtils.isEmpty(id)) {
			try {
				patient = patientRepository.findById(id).get();
			} catch (Exception e) {
				patient = null;
			}
		}
		return patient;
	}

	@Override
	public List<Patient> list(List<Integer> inIdList, List<Long> inPatList, String match) {
		Specification<Patient> spec = processSpec(inIdList, inPatList, match);
		List<Patient> patients = patientRepository.findAll(spec);
		return patients;
	}

	@Override
	public Page<Patient> find(List<Integer> inIdList, List<Long> inPatList, String match) {
		Specification<Patient> spec = processSpec(inIdList, inPatList, match);
		Page<Patient> patients = patientRepository.findAll(spec, (Pageable) null);
		return patients;
	}
	
	private Specification<Patient> processSpec(List<Integer> inIdList, List<Long> inPatList, String match) {
		return new Specification<Patient>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> pres = new ArrayList<>();
				if(!CheckUtils.isEmpty(inIdList)) {
					pres.add(root.get("id").in(inIdList));
				}
				if (!CheckUtils.isEmpty(inPatList)) {
					pres.add(root.get("pat").in(inPatList));
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
