package be.pxl.clubs.service;

import be.pxl.clubs.domain.Club;
import be.pxl.clubs.domain.Member;
import be.pxl.clubs.domain.Owner;
import be.pxl.clubs.domain.dto.*;
import be.pxl.clubs.exception.ClubException;
import be.pxl.clubs.repository.ClubRepository;
import be.pxl.clubs.repository.MemberRepository;
import be.pxl.clubs.repository.OwnerRepository;
import be.pxl.clubs.service.contracts.IClubService;
import be.pxl.clubs.service.contracts.LogExecutionTime;
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
    private final MemberRepository memberRepository;
    private final OwnerRepository ownerRepository;

    @Override
    @Transactional(readOnly = true)
    @LogExecutionTime
    public List<OwnerDto> getOwnersWithAllTheirClubs(){
        List<Owner> owners = ownerRepository.findAll();
        if(owners.isEmpty()){
            throw new ClubException("No owners are found");
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

    private Owner findOwnerById(Long ownerId){
        return ownerRepository.findById(ownerId).orElseThrow(() -> new ClubException("No owner found"));
    }

    private Club findClubById(Long clubId){
        return clubRepository.findById(clubId).orElseThrow(() -> new ClubException("No club found"));
    }

    @Override
    public List<ClubDto> getAllClubsByOwner(Long ownerId){
        Owner owner = findOwnerById(ownerId);
        List<ClubDto> ownerClubs = new ArrayList<>();
        for(Club club : owner.getClubs()){
            ownerClubs.add(new ClubDto(club.getName(), club.getCapacity(), club.getMembers()));
        }
        return ownerClubs;
    }

    @Override
    public ClubDto addNewClubToOwner(Long ownerId, ClubRequest clubRequest){
        Owner owner = findOwnerById(ownerId);
        Club newClub = Club.builder().name(clubRequest.getName()).capacity(clubRequest.getCapacity()).build();

        if(!owner.addClubToOwner(newClub)){
            throw new ClubException("Club is already in the list");
        } else {
            clubRepository.save(newClub);
        }
        return new ClubDto(newClub.getName(), newClub.getCapacity(), new ArrayList<>());
    }

    @Override
    public ClubDto updateClubInformation(Long clubId, ClubRequest clubRequest){
        Club club = findClubById(clubId);
        club.setName(clubRequest.getName());
        club.setCapacity(clubRequest.getCapacity());

        return new ClubDto(club.getName(), club.getCapacity(), club.getMembers());
    }

    @Override
    public String deleteClubFromOwner(Long ownerId, Long clubId){
        Owner owner = findOwnerById(ownerId);
        Club club = owner.getClubs().stream().filter(c -> Objects.equals(c.getId(), clubId))
                .findFirst().orElseThrow(() -> new ClubException("No club found"));

        owner.getClubs().remove(club);
        return MessageFormat.format("Club {0} has been removed from {1}", club.getName(), owner.getName());
    }

    @Override
    public Owner newOwner(OwnerRequest ownerRequest) {
        Owner createdOwner = Owner.builder().name(ownerRequest.getName()).build();
        createdOwner = ownerRepository.save(createdOwner);
        return createdOwner;
    }

    @Override
    public Club addNewClub(ClubRequest clubRequest) {
        Club createdClub = Club.builder().name(clubRequest.getName()).capacity(clubRequest.getCapacity()).build();
        createdClub = clubRepository.save(createdClub);
        return createdClub;
    }

    @Override
    public MemberResponse addMemberToAClub(Long clubId, MemberRequest memberRequest) {
        Club club = findClubById(clubId);
        Member member = Member.builder().name(memberRequest.getName()).insz(memberRequest.getInsz()).build();
        if(!club.addMemberToClub(member)){
            throw new ClubException("Member is already in the list");
        } else {
            memberRepository.save(member);
        }
        return new MemberResponse(member.getName());
    }

    @Override
    public String deleteMemberFromAClub(Long clubId, Long memberId) {
        Club club = findClubById(clubId);
        Member member = club.getMembers().stream().filter(c -> Objects.equals(c.getId(), memberId))
                .findFirst().orElseThrow(() -> new ClubException("Member not found"));

        club.getMembers().remove(member);
        return MessageFormat.format("Member {0} has been removed from {1}", member.getName(), club.getName());
    }
}
