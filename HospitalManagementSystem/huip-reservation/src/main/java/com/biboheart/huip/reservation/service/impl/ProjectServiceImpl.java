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
import com.biboheart.huip.reservation.domain.Project;
import com.biboheart.huip.reservation.repository.ProjectRepository;
import com.biboheart.huip.reservation.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	private ProjectRepository projectRepository;

	@Override
	public Project save(Project project) throws BhException {
		if (null == project.getId()) {
			project.setId(0);
		}
		if (CheckUtils.isEmpty(project.getName())) {
			throw new BhException("Name cannot be null");
		}
		Long now = TimeUtils.getCurrentTimeInMillis();
		if (CheckUtils.isEmpty(project.getSn())) {
			project.setSn("project_" + now);
		}
		if (null != projectRepository.findBySnAndIdNot(project.getSn(), project.getId())) {
			throw new BhException("Coding arealdy existed");
		}
		if (CheckUtils.isEmpty(project.getCreateTime())) {
			project.setCreateTime(now);
		}
		project.setUpdateTime(now);
		project = projectRepository.save(project);
		return project;
	}

	@Override
	public Project delete(Integer id) {
		Project project = null;
		if (null == project && !CheckUtils.isEmpty(id)) {
			try {
				project = projectRepository.findById(id).get();
			} catch (Exception e) {
				project = null;
			}
		}
		if (null != project) {
			projectRepository.delete(project);
		}
		return project;
	}

	@Override
	public Project load(Integer id, String sn) {
		Project project = null;
		if (null == project && !CheckUtils.isEmpty(sn)) {
			project = projectRepository.findBySnAndIdNot(sn, 0);
		}
		if (null == project && !CheckUtils.isEmpty(id)) {
			try {
				project = projectRepository.findById(id).get();
			} catch (Exception e) {
				project = null;
			}
		}
		return project;
	}

	@Override
	public List<Project> list(List<Integer> inIdList, String match) {
		Specification<Project> spec = processSpec(inIdList, match);
		List<Project> projects = projectRepository.findAll(spec);
		return projects;
	}

	@Override
	public Page<Project> find(List<Integer> inIdList, String match) {
		Specification<Project> spec = processSpec(inIdList, match);
		Page<Project> projects = projectRepository.findAll(spec, (Pageable) null);
		return projects;
	}
	
	private Specification<Project> processSpec(List<Integer> inIdList, String match) {
		return new Specification<Project>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> pres = new ArrayList<>();
				if(!CheckUtils.isEmpty(inIdList)) {
					pres.add(root.get("id").in(inIdList));
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
