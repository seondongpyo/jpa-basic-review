package io.github.seondongpyo.embedded;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Entertainer {

	@Id @GeneratedValue
	private Long id;

	private String name;

	public Entertainer(String name) {
		this.name = name;
	}

	@Embedded
	private Contract contract;

	@Embedded
	private Address homeAddress;

	@Embedded
	@AttributeOverrides(value = {
		@AttributeOverride(name="city", column=@Column(name = "COMPANY_CITY")),
		@AttributeOverride(name="street", column=@Column(name = "COMPANY_STREET")),
		@AttributeOverride(name="zipcode", column=@Column(name = "COMPANY_ZIPCODE"))
	})
	private Address companyAddress;

	@ElementCollection
	@CollectionTable(
		name = "FAVORITE_DIRECTOR",
		joinColumns = @JoinColumn(name = "ENTERTAINER_ID")
	)
	@Column(name = "DIRECTOR_NAME")
	private Set<String> favoriteDirectors = new HashSet<>();

	@ElementCollection
	@CollectionTable(
		name = "ADDRESS",
		joinColumns = @JoinColumn(name = "ENTERTAINER_ID")
	)
	private List<Address> addressHistory = new ArrayList<>();

}
