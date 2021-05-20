package io.github.seondongpyo.embedded;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Contract {

	private LocalDate startDate;
	private LocalDate expireDate;

}
