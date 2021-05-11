package io.github.seondongpyo.mapping.id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Getter;

@Getter
@Entity
@SequenceGenerator(
	name = "SEQ_GENERATOR",
	sequenceName = "ID_SEQ",
	initialValue = 1,
	allocationSize = 1
)
public class GenerateIdBySequence {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
	private Long id;

}
