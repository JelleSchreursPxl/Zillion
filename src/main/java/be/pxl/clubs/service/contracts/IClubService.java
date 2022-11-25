package be.pxl.clubs.service.contracts;

import be.pxl.clubs.domain.Club;
import be.pxl.clubs.domain.dto.ClubDto;
import be.pxl.clubs.domain.dto.ClubRequest;
import be.pxl.clubs.domain.dto.OwnerDto;
import be.pxl.clubs.domain.dto.OwnerRequest;

import java.util.List;

public interface IClubService {
    List<OwnerDto> getOwnersWithAllTheirClubs() throws Exception;                                   // get
    List<ClubDto> getAllClubsByOwner(Long ownerId) throws Exception;                                // get by param
    ClubDto addNewClubToOwner(Long ownerId, ClubRequest clubRequest) throws Exception;                      // put by param & request
    ClubDto updateClubInformation(Long clubId, ClubRequest clubRequest) throws Exception;           // put by param & request
    String deleteClubFromOwner(Long ownerId, Long clubId) throws Exception;                         // delete by param
    OwnerDto newOwner(OwnerRequest ownerRequest);                                                   // post

    Club addNewClub(ClubRequest clubRequest);
}
