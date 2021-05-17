package io.github.seondongpyo.mapping.relation.manytomany.bidirectional;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Task {

	@Id @GeneratedValue
	@Column(name = "TASK_ID")
	private Long id;

	@ManyToMany(mappedBy = "tasks")
	private List<Worker> workers = new ArrayList<>();

	private String description;

	public Task(String description) {
		this.description = description;
	}
}
