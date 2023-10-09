package ppzeff.entity;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Getter
@ToString

//@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Zone  extends AbstractEntity{
    String myId;
    String zoneName;

}
