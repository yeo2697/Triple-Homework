package com.triple.homework.place;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_place")
public class Place {

    public Place() {
        this.delYn = 'N';
    }

    @Id
    @Column(name = "idx", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @NotNull
    @Column(name = "place_id")
    private String placeId;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "del_yn")
    private Character delYn;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_dt")
    private Long createDt;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_dt")
    private Long updateDt;
}
