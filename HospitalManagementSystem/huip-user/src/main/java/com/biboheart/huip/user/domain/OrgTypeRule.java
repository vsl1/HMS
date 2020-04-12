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
@Table(name = "bh_user_org_type_rule")
public class OrgTypeRule implements Serializable {
	private static final long serialVersionUID = -6713717729763625987L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer pid; 
	private Integer cid; 
}
