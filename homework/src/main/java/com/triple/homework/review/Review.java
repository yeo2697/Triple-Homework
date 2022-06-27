package com.triple.homework.review;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "tbl_review")
public class Review {

    public Review() {
        this.delYn = 'N';
    }

    @Id
    @Column(name = "idx", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @NotNull
    @Column(name = "review_id")
    private String reviewId;

    @NotNull
    @Column(name = "user_id")
    private String userId;

    @NotNull
    @Column(name = "place_id")
    private String placeId;

    @Column(name = "review_content")
    private String reviewContent;

    @Column(name = "review_photos")
    private String reviewPhotos;

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
