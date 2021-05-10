package io.github.seondongpyo.mapping;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class GenerateIdByUser {

	@Id
	private String id;

}
