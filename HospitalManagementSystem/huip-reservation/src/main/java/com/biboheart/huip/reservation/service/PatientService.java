package com.biboheart.huip.reservation.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.biboheart.brick.exception.BhException;
import com.biboheart.huip.reservation.domain.Patient;

public interface PatientService {
	public Patient save(Patient patient) throws BhException;
	
	public Patient delete(Integer id, Long pat, String idcard);
	
	public Patient load(Integer id, Long pat, String sn, String idcard);
	
	public List<Patient> list(List<Integer> inIdList, List<Long> inPatList, String match);
	
	public Page<Patient> find(List<Integer> inIdList, List<Long> inPatList, String match);
}
