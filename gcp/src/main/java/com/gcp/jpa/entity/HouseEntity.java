package com.gcp.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Builder
@Table(name = "HOUSE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HouseEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy="house")
    Set<PersonEntity> persons;

}
