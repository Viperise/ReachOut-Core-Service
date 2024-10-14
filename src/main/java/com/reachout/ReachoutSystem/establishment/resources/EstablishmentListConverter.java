package com.reachout.ReachoutSystem.establishment.resources;

import com.reachout.ReachoutSystem.establishment.dto.EstablishmentListResponseDTO;
import com.reachout.ReachoutSystem.establishment.entity.Establishment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EstablishmentListConverter {

    public static EstablishmentListResponseDTO establishmentToEstablishmentListResponseConverter(Establishment establishment) {
        var response = new EstablishmentListResponseDTO();

        response.setName(establishment.getName());
        response.setPhotoPath(establishment.getPhotoPath());
        response.setType(establishment.getType());
        response.setAddress(establishment.getAddress());
        response.setOwner(establishment.getOwner().getName());

        return response;
    }
}
