package com.gcp.jpa.mappers;

import com.gcp.jpa.dto.HouseDTO;
import com.gcp.jpa.dto.PersonDTO;
import com.gcp.jpa.entity.HouseEntity;
import com.gcp.jpa.entity.PersonEntity;

public final class PersonMapper {
    public static PersonEntity mapDtoToEntity(PersonDTO personDTO) {
        return PersonEntity.builder()
                .id(personDTO.getId())
                .name(personDTO.getName())
                .lastName(personDTO.getLastName())
                .house(mapHouseDtoToEntity(personDTO.getHouse()))
                .build();
    }

    public static PersonDTO mapEntityToDTO(PersonEntity personEntity) {
        return PersonDTO.builder()
                .id(personEntity.getId())
                .name(personEntity.getName())
                .lastName(personEntity.getLastName())
                .house(mapHouseEntityToDTO(personEntity.getHouse()))
                .build();
    }

    public static HouseEntity mapHouseDtoToEntity(HouseDTO houseDTO) {

        return HouseEntity.builder()
                .id(houseDTO.getId())
                .name(houseDTO.getName())
                .address(houseDTO.getAddress())
                .build();
    }


    public static HouseDTO mapHouseEntityToDTO(HouseEntity houseEntity) {


        return HouseDTO.builder()
                .id(houseEntity.getId())
                .name(houseEntity.getName())
                .address(houseEntity.getAddress())
                .build();
    }

}
