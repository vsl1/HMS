package com.biboheart.huip.reservation.repository;

import com.biboheart.huip.reservation.basejpa.CustomRepository;
import com.biboheart.huip.reservation.domain.Project;

public interface ProjectRepository extends CustomRepository<Project, Integer> {
	Project findBySnAndIdNot(String sn, Integer id);
}
