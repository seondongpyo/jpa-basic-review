package io.github.seondongpyo.cascade.nothing;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Child {

	@Id @GeneratedValue
	@Column(name = "CHILD_ID")
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PARENT_ID")
	private Parent parent;

}
