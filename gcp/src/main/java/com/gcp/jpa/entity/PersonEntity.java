package com.gcp.jpa.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;

@Entity
@Table(name = "person")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonEntity implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "house_id" )
    private HouseEntity house;


}
