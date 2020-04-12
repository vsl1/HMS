package com.biboheart.huip.reservation.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "bh_reservation_patient")
public class Patient {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String sn; 
	private Long pat; 
	private String name; 
	private Integer sex; 
	private Integer age; 
	private String idcard; 
	private String phone; 
	private String spell; 
	private Long createTime; 
	private Long upeateTime; 
}
