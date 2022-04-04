package com.bezkoder.springjwt.repository;


import com.bezkoder.springjwt.models.TicketEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Integer> {
    Optional<TicketEntity> findByTicketId(int ticketId);

    // Trưởng phòng ban
    @Query("select t from TicketEntity t inner join TicketDepRelationEntity td " +
            "on t.ticketId = td.ticketEntity.ticketId " +
            "inner join  TicketUserRelationEntity tu " +
            "on t.ticketId = tu.ticketEntity.ticketId " +
            "where td.depEntity.depId = ?1 or tu.userEntity.userId = ?2")
    Optional<List<TicketEntity>> findAllByDepId(int depId, int userId);

    // User bình thường
    @Query("SELECT t from TicketEntity t inner join TicketUserRelationEntity tu " +
            "on t.ticketId = tu.ticketEntity.ticketId " +
            "WHERE tu.userEntity.userId = ?1")
    Optional<List<TicketEntity>> findAllByUserId(int UserId);




    Optional<TicketEntity> findByTicketIdAndIsDeletedFalse(int ticketId);
}
