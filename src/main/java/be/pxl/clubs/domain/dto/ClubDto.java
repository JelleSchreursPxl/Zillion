package be.pxl.clubs.domain.dto;

import be.pxl.clubs.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubDto implements Serializable {
    private String name;
    private int capacity;
    private List<Member> memberList;
}