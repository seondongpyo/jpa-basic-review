package io.github.seondongpyo.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Contract {

	private LocalDate startDate;
	private LocalDate expireDate;

}
