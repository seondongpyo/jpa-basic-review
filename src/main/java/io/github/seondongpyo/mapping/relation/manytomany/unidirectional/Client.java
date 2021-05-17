package io.github.seondongpyo.mapping.relation.manytomany.unidirectional;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Client {

	@Id @GeneratedValue
	@Column(name = "CLIENT_ID")
	private Long id;

	@ManyToMany
	@JoinTable(name = "CLIENT_BANK")
	private List<Bank> banks = new ArrayList<>();

	private String name;

}
