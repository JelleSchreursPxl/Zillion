package be.pxl.clubs.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "owner")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String name;

    @OneToMany(fetch = FetchType.EAGER)                                                  // cascade = CascadeType.ALL ??
    private List<Club> clubs = new ArrayList<>();

    public boolean addClubToOwner(Club newClub) {
        List<String> clubNames = new ArrayList<>();
        for ( Club club : clubs ) {
            clubNames.add(club.getName().toLowerCase());
        }
        if(!clubNames.contains(newClub.getName().toLowerCase())){
            clubs.add(newClub);
            return true;
        } else {
            return false;
        }
    }
}