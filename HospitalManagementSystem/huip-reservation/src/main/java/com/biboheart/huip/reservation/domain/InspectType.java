package com.biboheart.huip.reservation.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 检查类别
 * @author crj
 *
 */
@Data
@Entity
@Table(name = "bh_reservation_inspect_type")
public class InspectType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String sn; 
	private String name; 
	private String remark; 
	private Long createTime; 
	private Long updateTime; 
}
