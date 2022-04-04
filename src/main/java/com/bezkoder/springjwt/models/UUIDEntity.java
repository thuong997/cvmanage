package com.bezkoder.springjwt.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "UUID")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UUIDEntity extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  int id;

    @Column(name = "uuidStr")
    private String uuidStr;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

}
