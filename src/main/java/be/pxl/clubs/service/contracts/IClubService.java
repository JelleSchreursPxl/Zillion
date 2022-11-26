package be.pxl.clubs.service.contracts;

import be.pxl.clubs.domain.Club;
import be.pxl.clubs.domain.Owner;
import be.pxl.clubs.domain.dto.*;

import java.util.List;

public interface IClubService {
    List<OwnerDto> getOwnersWithAllTheirClubs();                                        // get
    List<ClubDto> getAllClubsByOwner(Long ownerId);                                     // get by param
    ClubDto addNewClubToOwner(Long ownerId, ClubRequest clubRequest);                   // put by param & request
    ClubDto updateClubInformation(Long clubId, ClubRequest clubRequest);                // put by param & request
    String deleteClubFromOwner(Long ownerId, Long clubId);                              // delete by param
    Owner newOwner(OwnerRequest ownerRequest);                                          // post
    Club addNewClub(ClubRequest clubRequest);                                           // post
    MemberResponse addMemberToAClub(Long clubId, MemberRequest memberRequest);          // put by param & request
    String deleteMemberFromAClub(Long clubId, Long memberId);                           // delete by param
}
