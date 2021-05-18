package io.github.seondongpyo.mapping.inheritance.joined;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Getter
@NoArgsConstructor
@Entity
public class Album extends Item {

    private String artist;

    public Album(String artist) {
        this.artist = artist;
    }
}
