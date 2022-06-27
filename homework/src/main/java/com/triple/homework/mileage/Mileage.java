package com.triple.homework.mileage;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_mileage")
public class Mileage {

    public Mileage() {
        this.delYn = 'N';
    }

    @Id
    @Column(name = "idx", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @NotNull
    @Column(name = "user_id")
    private String userId;

    @NotNull
    @Column(name = "review_id")
    private String reviewId;

    @Column(name = "mileage_log")
    private String mileageLog;

    @Column(name = "mileage_value")
    private Long mileageValue;

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
