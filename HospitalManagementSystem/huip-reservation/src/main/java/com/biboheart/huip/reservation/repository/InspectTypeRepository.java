package com.biboheart.huip.reservation.repository;

import com.biboheart.huip.reservation.basejpa.CustomRepository;
import com.biboheart.huip.reservation.domain.InspectType;

public interface InspectTypeRepository extends CustomRepository<InspectType, Integer> {
	InspectType findBySnAndIdNot(String sn, Integer id);
	InspectType findByNameAndIdNot(String name, Integer id);
}
