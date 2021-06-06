package io.github.seondongpyo.jpql;

import lombok.*;

import javax.persistence.Embeddable;
import java.util.Objects;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class Profile {

    private String email;
    private String website;
    private String twitterUsername;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var profile = (Profile) o;
        return Objects.equals(getEmail(), profile.getEmail()) && Objects.equals(getWebsite(), profile.getWebsite()) && Objects.equals(getTwitterUsername(), profile.getTwitterUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getWebsite(), getTwitterUsername());
    }
}
