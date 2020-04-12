package com.biboheart.huip.user.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "bh_user_client")
public class Client implements Serializable {
	private static final long serialVersionUID = -6421664309310055644L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String clientName; 
	private String clientId; 
	private String resourceIds;
	private String clientSecret; 
	private String scope; 
	private String authorizedGrantTypes; 
	@Column(length = 4096)
	private String webServerRedirectUri; 
	private String authorities; 
	private Integer accessTokenValidity;
	private Integer refreshTokenValidity; 
	private String additionalInformation; 
	private String autoapprove;
	private String registeredRedirectUri;
	private Long createTime; 
	private int self = 1; 
}
