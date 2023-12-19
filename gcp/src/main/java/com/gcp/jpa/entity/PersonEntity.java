package com.gcp.jpa.entity;

import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;

@Entity
@Table(name = "PERSON")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonEntity implements Serializable {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "house_id")
    private Long house_id;

    @ManyToOne
    @JoinColumn(name = "house_id",nullable = false)
    private HouseEntity house;


}
