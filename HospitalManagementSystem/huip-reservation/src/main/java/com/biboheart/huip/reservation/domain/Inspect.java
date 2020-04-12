package com.biboheart.huip.reservation.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 检查预约
 * @author crj
 *
 */
@Data
@Entity
@Table(name = "bh_reservation_inspect")
public class Inspect {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String sn; 
	private Integer device; 
	private String dsn; 
	private String dname; 
	private Integer project; 
	private String psn; 
	private String pname; 
	private Long pat; 
	private String patName; 
	private String patSn; 
	private String medicalNumber; 
	private Integer source; 
	private Long time; 
	private Long duration; 
	private Long creator;
	private String creatorName; 
	private Long createTime; 
	private Long upeateTime; 
	private Integer charged; 
	private Integer state;
	private String remark; 
}
