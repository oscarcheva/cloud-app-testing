package com.gcp.jpa.mappers;

import com.gcp.jpa.dto.HouseDTO;
import com.gcp.jpa.dto.PersonDTO;
import com.gcp.jpa.entity.HouseEntity;
import com.gcp.jpa.entity.PersonEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class HouseMapper {

    public static HouseEntity mapDtoToEntity(HouseDTO houseDTO) {
        Set<PersonEntity> personEntityList = new HashSet<>();
        if (!(houseDTO.getPersons().isEmpty()))
            houseDTO.getPersons().forEach(personEntity -> personEntityList.add(mapPersonDtoToEntity(personEntity)));

        return HouseEntity.builder()
                .id(houseDTO.getId())
                .name(houseDTO.getName())
                .address(houseDTO.getAddress())
                .persons(personEntityList)
                .build();
    }


    public static HouseDTO mapEntityToDTO(HouseEntity houseEntity) {
        List<PersonDTO> personDTOList = new ArrayList<>();
        if (!(houseEntity.getPersons().isEmpty()))
            houseEntity.getPersons().forEach(personEntity -> personDTOList.add(mapPersonEntityToDTO(personEntity)));


        return HouseDTO.builder()
                .id(houseEntity.getId())
                .name(houseEntity.getName())
                .address(houseEntity.getAddress())
                .persons(personDTOList)
                .build();
    }

    private static PersonEntity mapPersonDtoToEntity(PersonDTO personDTO) {
        return PersonEntity.builder()
                .id(personDTO.getId())
                .name(personDTO.getName())
                .lastName(personDTO.getLastName())
                .build();
    }

    private static PersonDTO mapPersonEntityToDTO(PersonEntity personEntity) {
        return PersonDTO.builder()
                .id(personEntity.getId())
                .name(personEntity.getName())
                .lastName(personEntity.getLastName())
                .build();
    }


}
