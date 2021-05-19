package io.github.seondongpyo.mapping.common;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Post extends BaseEntity {

	@Id @GeneratedValue
	private Long id;

	@OneToMany(mappedBy = "post")
	private List<Comment> comments = new ArrayList<>();

	private String title;
	private String content;

}
