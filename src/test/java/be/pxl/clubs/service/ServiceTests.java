package be.pxl.clubs.service;

import be.pxl.clubs.domain.Club;
import be.pxl.clubs.domain.Member;
import be.pxl.clubs.domain.Owner;
import be.pxl.clubs.domain.dto.ClubRequest;
import be.pxl.clubs.domain.dto.MemberRequest;
import be.pxl.clubs.domain.dto.OwnerRequest;
import be.pxl.clubs.exception.ClubException;
import be.pxl.clubs.repository.ClubRepository;
import be.pxl.clubs.repository.MemberRepository;
import be.pxl.clubs.repository.OwnerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceTests {
    @Mock
    private ClubRepository clubRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private ClubService clubService;

    @Captor
    private ArgumentCaptor<Club> clubArgumentCaptor;
    @Captor
    private ArgumentCaptor<Member> memberArgumentCaptor;
    @Captor
    private ArgumentCaptor<Owner> ownerArgumentCaptor;

    private String name;
    private Owner owner;
    private Club club;
    private Member member;
    private ClubRequest clubRequest;
    private MemberRequest memberRequest;
    private OwnerRequest ownerRequest;

    @BeforeEach
    public void Setup() {
        Random random = new Random();
        name = UUID.randomUUID().toString();
        owner = Owner.builder()
                .name(UUID.randomUUID().toString())
                .id(1L)
                .clubs(new ArrayList<>())
                .build();

        club = Club.builder()
                .name(UUID.randomUUID().toString())
                .capacity(random.nextInt())
                .id(1L)
                .members(new ArrayList<>())
                .build();

        member = Member.builder()
                .name(UUID.randomUUID().toString())
                .id(1L)
                .build();

        ownerRequest = OwnerRequest.builder().name(owner.getName()).build();
        clubRequest = ClubRequest.builder().name(club.getName()).capacity(club.getCapacity()).build();
        memberRequest = MemberRequest.builder().name(member.getName()).build();
    }

    @Test
    void addClubToOwner_WillThrowException_WhenClubIsFound(){
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.ofNullable(owner));
        clubService.addNewClubToOwner(owner.getId(), clubRequest);

        var exception = assertThrows(ClubException.class,
                () -> clubService.addNewClubToOwner(owner.getId(), clubRequest));

        assertEquals("Club is already in the list", exception.getMessage());
    }

    @Test
    void removeMember_WillThrowException_WhenMemberNotFound(){
        when(clubRepository.findById(anyLong())).thenReturn(Optional.ofNullable(club));

        var exception = assertThrows(ClubException.class,
                () -> clubService.deleteMemberFromAClub(club.getId(), anyLong()));

        assertEquals("Member not found", exception.getMessage());
    }

    @Test
    void removeClub_WillThrowException_WhenClubNotFound(){
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.ofNullable(owner));

        var exception = assertThrows(ClubException.class,
                () -> clubService.deleteClubFromOwner(owner.getId(), anyLong()));

        assertEquals("No club found", exception.getMessage());
    }

    @Test
    void updateClubInformation_WillReturnUpdatedClubInfo(){
        ClubRequest clubRequestUpdate = ClubRequest.builder().name("update").capacity(250).build();
        when(clubRepository.findById(anyLong())).thenReturn(Optional.ofNullable(club));

        clubService.updateClubInformation(anyLong(), clubRequestUpdate);

        assertEquals(club.getName(), clubRequestUpdate.getName());
        assertEquals(club.getCapacity(), clubRequestUpdate.getCapacity());
    }

    @Test
    void getOwnerWithClubs_ReturnsOwnerDTO(){
        owner.addClubToOwner(club);
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.ofNullable(owner));

        var result = clubService.getAllClubsByOwner(owner.getId());

        assertEquals(owner.getClubs().size(), result.size());
    }

    @Test
    void getOwnerWithClubs_WillThrowException_WhenNoOwnerFound(){
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.empty());

        var exception = assertThrows(ClubException.class,
                () -> clubService.getAllClubsByOwner(owner.getId()));

        assertEquals("No owner found", exception.getMessage());
    }

    @Test
    void deleteMemberFromAClub_WillReturnMessageAfterDelete(){
        var secondMember = Member.builder()
                .name("Tom")
                .insz("300390-123.11")
                .id(2L).build();
        club.addMemberToClub(member);
        club.addMemberToClub(secondMember);
        var resultMessage = MessageFormat.format("Member {0} has been removed from {1}", member.getName(), club.getName());
        when(clubRepository.findById(anyLong())).thenReturn(Optional.ofNullable(club));

        var result = clubService.deleteMemberFromAClub(secondMember.getId(), club.getId());

        assertEquals(resultMessage, result);
    }
}