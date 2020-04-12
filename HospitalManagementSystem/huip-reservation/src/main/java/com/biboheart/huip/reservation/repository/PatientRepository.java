package com.biboheart.huip.reservation.repository;

import com.biboheart.huip.reservation.basejpa.CustomRepository;
import com.biboheart.huip.reservation.domain.Patient;

public interface PatientRepository extends CustomRepository<Patient, Integer> {
	Patient findBySnAndIdNot(String sn, Integer id);
	Patient findByIdcardAndIdNot(String idcard, Integer id);
	Patient findByPat(Long pat);
}
