package com.biboheart.huip.reservation.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "bh_reservation_device")
public class Device {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String sn; 
	private String name; 
	private Integer state; 
	private String remark; 
	private String site;
	private Long createTime; 
	private Long updateTime; 
}
