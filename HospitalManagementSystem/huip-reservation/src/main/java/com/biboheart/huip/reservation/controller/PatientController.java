package com.biboheart.huip.reservation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.model.BhResponseResult;
import com.biboheart.huip.reservation.domain.Patient;
import com.biboheart.huip.reservation.service.PatientService;

@RestController
public class PatientController {
	@Autowired
	private PatientService patientService;
	
	@RequestMapping(value = "/maaapi/maa/patient/save", method = {RequestMethod.POST})
	public BhResponseResult<?> save(Patient patient) throws BhException {
		patient = patientService.save(patient);
		return new BhResponseResult<>(0, "success", patient);
	}
	
	@RequestMapping(value = "/maaapi/maa/patient/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> delete(Integer id, Long pat, String idcard) {
		Patient patient = patientService.delete(id, pat, idcard);
		return new BhResponseResult<>(0, "success", patient);
	}
	
	@RequestMapping(value = "/maaapi/maa/patient/load", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> load(Integer id, Long pat, String sn, String idcard) {
		Patient patient = patientService.load(id, pat, sn, idcard);
		return new BhResponseResult<>(0, "success", patient);
	}
	
	@RequestMapping(value = "/maaapi/maa/patient/list", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> list(String match) {
		List<Patient> patients = patientService.list(null, null, match);
		return new BhResponseResult<>(0, "success", patients);
	}
	
	@RequestMapping(value = "/maaapi/maa/patient/find", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> find(String match) {
		Page<Patient> patients = patientService.find(null, null, match);
		return new BhResponseResult<>(0, "success", patients);
	}
}
