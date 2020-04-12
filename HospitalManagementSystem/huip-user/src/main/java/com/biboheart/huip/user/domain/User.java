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
@Table(name = "bh_user_user")
public class User implements Serializable {
	private static final long serialVersionUID = -989635790212353396L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	private String sn; 
	private String account; 
	private String name; 
	private String phone; 
	private Long birthday; 
	private Long createTime; 
	private Long updateTime;
}
