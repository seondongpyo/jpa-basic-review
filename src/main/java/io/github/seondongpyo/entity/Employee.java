package io.github.seondongpyo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity(name = "EMP")
public class Employee {

	@Id @GeneratedValue
	private Long id;

	@Column(updatable = false)
	private String name;

	@Column(nullable = false)
	private Integer age;

	@Column(insertable = false)
	private String address;

	@Column(length = 13)
	private String residentNumber;

	@Enumerated(value = EnumType.STRING)
	private Gender gender;

	@Enumerated(value = EnumType.ORDINAL)
	private Role role;

	@Temporal(TemporalType.DATE)
	private Date joinDate;

	@Temporal(TemporalType.TIME)
	private Date startWorkTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastBusinessTripDateTime;

	@Lob
	private String workHistory;

	@Transient
	private String temp;

}
