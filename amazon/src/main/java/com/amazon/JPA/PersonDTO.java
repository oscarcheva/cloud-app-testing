package com.amazon.JPA;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String lastName;
}
