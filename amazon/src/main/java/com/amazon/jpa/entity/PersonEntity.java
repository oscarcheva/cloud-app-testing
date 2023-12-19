package com.amazon.jpa.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Table(name = "PERSON")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonEntity implements Serializable {

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @Column("last_name")
    private String lastName;

    @Column("house_id")
    private Long house_id;

    @Transient
    private HouseEntity house;


}
