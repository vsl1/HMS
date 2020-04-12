package com.biboheart.huip.reservation.repository;

import com.biboheart.huip.reservation.basejpa.CustomRepository;
import com.biboheart.huip.reservation.domain.Inspect;

public interface InspectRepository extends CustomRepository<Inspect, Integer> {
	Inspect findBySnAndIdNot(String sn, Integer id);
	Inspect findByPatAndDeviceAndProject(Long pat, Integer device, Integer project);
}
