package io.github.seondongpyo.mapping.id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import lombok.Getter;

@Getter
@Entity
@TableGenerator(
	name = "ID_SEQ_GENERATOR",
	table = "ID_SEQUENCES",
	pkColumnValue = "ID_SEQ",
	allocationSize = 1
)
public class GenerateIdByTable {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ID_SEQ_GENERATOR")
	private Long id;

}
