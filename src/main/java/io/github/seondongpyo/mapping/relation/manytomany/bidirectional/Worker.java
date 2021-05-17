package io.github.seondongpyo.mapping.relation.manytomany.bidirectional;

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
public class Worker {

	@Id @GeneratedValue
	@Column(name = "WORKER_ID")
	private Long id;

	@ManyToMany
	@JoinTable(name = "WORKER_TASK")
	private List<Task> tasks = new ArrayList<>();

	private String name;

	public Worker(String name) {
		this.name = name;
	}
}
