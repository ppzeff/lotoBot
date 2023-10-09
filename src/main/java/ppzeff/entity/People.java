package ppzeff.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class People extends AbstractEntity {
    String myId;
    String surname;
    String name;
    String patronymic;
    String role;
    String certificate;
    LocalDate validity;
    String photoLink;
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "company_id")
    Company company;

    Integer score;
//    public String getCompanyName() {
//        return company.getName();
//    }
}
