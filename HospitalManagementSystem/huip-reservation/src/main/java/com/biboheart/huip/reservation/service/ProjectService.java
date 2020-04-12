package com.biboheart.huip.reservation.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.biboheart.brick.exception.BhException;
import com.biboheart.huip.reservation.domain.Project;

public interface ProjectService {
	public Project save(Project project) throws BhException;
	
	public Project delete(Integer id);
	
	public Project load(Integer id, String sn);
	
	public List<Project> list(List<Integer> inIdList, String match);
	
	public Page<Project> find(List<Integer> inIdList, String match);
}
