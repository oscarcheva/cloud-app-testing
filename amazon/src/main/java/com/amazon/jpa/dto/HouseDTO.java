package com.amazon.jpa.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HouseDTO {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String address;

    List<PersonDTO>persons;

}
