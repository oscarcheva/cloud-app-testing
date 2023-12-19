package com.amazon.jpa.entity;

import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table("person_house")
public class PersonHouseEntity {

    @Column("person_id")
    private Long personId;

    @Transient
    private PersonEntity personEntity;


    @Column("house_id")
    private Long houseId;

    @Transient
    private HouseEntity houseEntity;

}
