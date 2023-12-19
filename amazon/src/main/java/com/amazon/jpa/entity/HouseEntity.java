package com.amazon.jpa.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Builder
@Table(name = "HOUSE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HouseEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @Column("address")
    private String address;

    @Transient
    List<PersonEntity> persons;

}
