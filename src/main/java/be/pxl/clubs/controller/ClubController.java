package be.pxl.clubs.controller;

import be.pxl.clubs.domain.Club;
import be.pxl.clubs.domain.dto.ClubDto;
import be.pxl.clubs.domain.dto.ClubRequest;
import be.pxl.clubs.domain.dto.OwnerDto;
import be.pxl.clubs.service.contracts.IClubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor // autowiring wordt voorzien
@Slf4j
public class ClubController {
    private final IClubService clubService;

    @GetMapping("clubowners")
    public ResponseEntity<List<OwnerDto>> getClubOwners(){
        try{
            List<OwnerDto> owners = clubService.getOwnersWithAllTheirClubs();
            return new ResponseEntity<>(owners, HttpStatus.OK);
        } catch ( Exception e ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("clubowner/{ownerId}")
    public ResponseEntity<List<ClubDto>> getClubsByOwner(@PathVariable Long ownerId){
        try{
            List<ClubDto> clubs = clubService.getAllClubsByOwner(ownerId);
            return new ResponseEntity<>(clubs, HttpStatus.FOUND);
        } catch ( Exception e ) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("clubowner/{ownerId}/add")
    public ResponseEntity<ClubDto> addClubToOwner(@PathVariable Long ownerId,
                                                  @RequestBody ClubRequest clubRequest) {
        try{
            ClubDto newClub = clubService.addNewClubToOwner(ownerId, clubRequest);
            return new ResponseEntity<>(newClub, HttpStatus.CREATED);
        } catch ( Exception e ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("clubs/add")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Club> addClub(@RequestBody ClubRequest clubRequest){
        try{
            Club club = clubService.addNewClub(clubRequest);
            return new ResponseEntity<>(club, HttpStatus.CREATED);
        } catch ( Exception e ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("clubowner/{ownerId}/delete/{clubId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> removeClubFromOwner(@PathVariable Long ownerId, @PathVariable Long clubId){
        try{
            String deleted = clubService.deleteClubFromOwner(ownerId, clubId);
            return new ResponseEntity<>(deleted, HttpStatus.OK);
        } catch ( Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
