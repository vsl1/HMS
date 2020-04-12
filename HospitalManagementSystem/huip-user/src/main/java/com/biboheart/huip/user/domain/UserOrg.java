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
@Table(name = "bh_user_user_org")
public class UserOrg implements Serializable {
	private static final long serialVersionUID = 2439488455909891027L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long uid; 
	private Integer oid; 
	private Integer otid; 
	private String otname; 
	private String oname; 
	private String opath; 
}
