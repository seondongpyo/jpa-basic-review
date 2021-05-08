package io.github.seondongpyo.mapping;

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
@Entity
public class Employee {

	@Id @GeneratedValue
	private Long id;

	@Column(updatable = false)
	private String name;

	@Column(nullable = false)
	private int age;

	@Column(insertable = false)
	private String address;

	@Column(insertable = false)
	private String residentNumber;

	@Enumerated(value = EnumType.STRING)
	private Gender gender;

	@Temporal(TemporalType.TIMESTAMP)
	private Date joinDateTime;

	@Lob
	private String workHistory;

	@Transient
	private String temp;

}
