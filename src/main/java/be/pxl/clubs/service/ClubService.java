package be.pxl.clubs.service;

import be.pxl.clubs.domain.Club;
import be.pxl.clubs.domain.Owner;
import be.pxl.clubs.domain.dto.ClubDto;
import be.pxl.clubs.domain.dto.ClubRequest;
import be.pxl.clubs.domain.dto.OwnerDto;
import be.pxl.clubs.domain.dto.OwnerRequest;
import be.pxl.clubs.repository.ClubRepository;
import be.pxl.clubs.repository.MemberRepository;
import be.pxl.clubs.repository.OwnerRepository;
import be.pxl.clubs.service.contracts.IClubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClubService implements IClubService {

    private final ClubRepository clubRepository;
    private final OwnerRepository ownerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OwnerDto> getOwnersWithAllTheirClubs() throws Exception {
        List<Owner> owners = ownerRepository.findAll();
        if(owners.isEmpty()){
            throw new Exception("No owners are found");
        } else {
            List<OwnerDto> ownerDtos = new ArrayList<>();
            for(Owner owner : owners){
                OwnerDto ownerDto = new OwnerDto();
                List<ClubDto> clubDtos = new ArrayList<>();

                for(Club club : owner.getClubs()){
                    clubDtos.add(new ClubDto(club.getName(), club.getCapacity(), club.getMembers()));
                }
                ownerDto.setName(owner.getName());
                ownerDto.setClubs(clubDtos);

                ownerDtos.add(ownerDto);
            }
            return ownerDtos;
        }
    }

    private Owner findOwnerById(Long ownerId) throws Exception{
        return ownerRepository.findById(ownerId).orElseThrow(() -> new Exception("No owner found"));
    }

    @Override
    public List<ClubDto> getAllClubsByOwner(Long ownerId) throws Exception {
        Owner owner = findOwnerById(ownerId);
        List<ClubDto> ownerClubs = new ArrayList<>();
        for(Club club : owner.getClubs()){
            ownerClubs.add(new ClubDto(club.getName(), club.getCapacity(), club.getMembers()));
        }
        return ownerClubs;
    }

    @Override
    public ClubDto addNewClubToOwner(Long ownerId, ClubRequest clubRequest) throws Exception {
        Owner owner = findOwnerById(ownerId);
        Club newClub = Club.builder().name(clubRequest.getName()).capacity(clubRequest.getCapacity()).build();

        if(!owner.addClubToOwner(newClub)){
            throw new Exception("Club is already in the list");
        } else {
            clubRepository.save(newClub);
        }
        return new ClubDto(newClub.getName(), newClub.getCapacity(), new ArrayList<>());
    }

    @Override
    public ClubDto updateClubInformation(Long clubId, ClubRequest clubRequest) throws Exception {
        return null;
    }

    @Override
    public String deleteClubFromOwner(Long ownerId, Long clubId) throws Exception {
        Owner owner = findOwnerById(ownerId);
        Club club = owner.getClubs().stream().filter(c -> Objects.equals(c.getId(), clubId))
                .findFirst().orElseThrow(() -> new Exception("No club found"));

        owner.getClubs().remove(club);
        return MessageFormat.format("Club {0} has been removed from {1}", club.getName(), owner.getName());
    }

    @Override
    public OwnerDto newOwner(OwnerRequest ownerRequest) {
        return null;
    }

    @Override
    public Club addNewClub(ClubRequest clubRequest) {
        Club createdClub = Club.builder().name(clubRequest.getName()).capacity(clubRequest.getCapacity()).build();
        createdClub = clubRepository.save(createdClub);
        return createdClub;
    }
}
