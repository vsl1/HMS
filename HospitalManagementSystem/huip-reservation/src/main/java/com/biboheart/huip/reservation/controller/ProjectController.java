package com.biboheart.huip.reservation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.model.BhResponseResult;
import com.biboheart.huip.reservation.domain.Project;
import com.biboheart.huip.reservation.service.ProjectService;

@RestController
public class ProjectController {
	@Autowired
	private ProjectService projectService;
	
	@RequestMapping(value = "/maaapi/maa/project/save", method = {RequestMethod.POST})
	public BhResponseResult<?> save(Project project) throws BhException {
		project = projectService.save(project);
		return new BhResponseResult<>(0, "success", project);
	}
	
	@RequestMapping(value = "/maaapi/maa/project/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> delete(Integer id) {
		Project project = projectService.delete(id);
		return new BhResponseResult<>(0, "success", project);
	}
	
	@RequestMapping(value = "/maaapi/maa/project/load", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> load(Integer id, String sn) {
		Project project = projectService.load(id, sn);
		return new BhResponseResult<>(0, "success", project);
	}
	
	@RequestMapping(value = "/maaapi/maa/project/list", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> list(String match) {
		List<Project> projects = projectService.list(null, match);
		return new BhResponseResult<>(0, "success", projects);
	}
	
	@RequestMapping(value = "/maaapi/maa/project/find", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> find(String match) {
		Page<Project> projects = projectService.find(null, match);
		return new BhResponseResult<>(0, "success", projects);
	}
}
