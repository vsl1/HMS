package com.biboheart.huip.reservation.repository;

import com.biboheart.huip.reservation.basejpa.CustomRepository;
import com.biboheart.huip.reservation.domain.Device;

public interface DeviceRepository extends CustomRepository<Device, Integer> {
	Device findBySnAndIdNot(String sn, Integer id);
}
