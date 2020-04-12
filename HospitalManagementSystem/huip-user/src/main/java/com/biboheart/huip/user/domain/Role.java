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
public class Role implements Serializable {
	private static final long serialVersionUID = -395701688680020217L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String sn; 
	private String name;
	private String aids;
	private Long createTime;
	private Long updateTime; 
}
