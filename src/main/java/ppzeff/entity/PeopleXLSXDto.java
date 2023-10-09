package ppzeff.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PeopleXLSXDto {
    String surname;
    String name;
    String patronymic;
    String role;
    String certificate;
    LocalDate validity;
    String linkPhoto;
    String uuid;
}
