package com.bezkoder.springjwt.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Builder
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "username", nullable = false, unique = true)
    @Size(max = 255)
    @NotBlank(message = "Không được để trống")
    private String username;

    @Column(name = "password",nullable = false)
    @Size(max = 255)
    @NotBlank(message = "Không được để trống")
    private String password;

    @Column(name = "full_name",  length = 50, nullable = false)
    private String fullName;

    @Column(name = "role",nullable = false)
    private String role;

    @ManyToOne
    @JoinColumn(name = "dep_id", nullable = false)
    private DepEntity depEntity;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TicketUserRelationEntity> ticketUserRelationEntities;


}
