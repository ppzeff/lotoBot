package ppzeff.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Loto extends AbstractEntity {

    Status status;
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "people_id")
    People people;
    LocalDateTime dt;
    String date;
    String complex;
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "zone_id")
    Zone zone;
    @ToString.Exclude
    @Lob
    byte[] img;
}
