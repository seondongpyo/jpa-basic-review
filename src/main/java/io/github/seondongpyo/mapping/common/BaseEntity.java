package io.github.seondongpyo.mapping.common;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@MappedSuperclass
public abstract class BaseEntity {

	private String createdBy;
	private String lastUpdatedBy;
	private LocalDateTime createdAt;
	private LocalDateTime lastModifiedAt;

}
