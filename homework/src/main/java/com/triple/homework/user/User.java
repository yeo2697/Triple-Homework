package com.triple.homework.user;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_user")
public class User {

    public User() {
        this.delYn = 'N';
    }

    @Id
    @Column(name = "idx", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @NotNull
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_tel")
    private String userTel;

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
