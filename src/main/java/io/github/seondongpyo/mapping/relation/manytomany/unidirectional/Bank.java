package io.github.seondongpyo.mapping.relation.manytomany.unidirectional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Bank {

	@Id @GeneratedValue
	@Column(name = "BANK_ID")
	private Long id;

	private String name;

	public Bank(String name) {
		this.name = name;
	}
}
