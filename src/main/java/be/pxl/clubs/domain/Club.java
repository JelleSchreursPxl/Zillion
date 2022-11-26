package be.pxl.clubs.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "club")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int capacity;

    @OneToMany // extra entity om meerdere objecten aan een lijst van een object toe te kunnen voegen.
    private List<Member> members = new ArrayList<>();

    public boolean addMemberToClub(Member newMember) {
        List<String> memberINSZ = new ArrayList<>();
        for ( Member member : members ) {
            memberINSZ.add(member.getInsz());
        }
        if(!memberINSZ.contains(newMember.getInsz())){
            members.add(newMember);
            return true;
        } else {
            return false;
        }
    }
}