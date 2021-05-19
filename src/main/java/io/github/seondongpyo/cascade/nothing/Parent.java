package io.github.seondongpyo.cascade.nothing;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Parent {

	@Id @GeneratedValue
	@Column(name = "PARENT_ID")
	private Long id;

	@OneToMany(mappedBy = "parent")
	private List<Child> children = new ArrayList<>();

	// 연관관계 편의 메서드
	public void addChild(Child child) {
		children.add(child);
		child.setParent(this);
	}

}
