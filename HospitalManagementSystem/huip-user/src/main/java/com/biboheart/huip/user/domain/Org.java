package com.biboheart.huip.user.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "bh_user_org")
public class Org implements Serializable {
	private static final long serialVersionUID = -2086039594378900727L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String sn; 
	private String name; 
	private Integer otid;
	private String otsn; 
	private String otname;
	private Integer pid;
	private String pname; 
	private String path; 
}
