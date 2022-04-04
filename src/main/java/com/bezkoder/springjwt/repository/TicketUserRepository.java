package com.bezkoder.springjwt.repository;


import com.bezkoder.springjwt.models.TicketDepRelationEntity;
import com.bezkoder.springjwt.models.TicketEntity;
import com.bezkoder.springjwt.models.TicketUserRelationEntity;
import com.bezkoder.springjwt.payload.request.TicketDetailRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketUserRepository extends JpaRepository<TicketUserRelationEntity, Integer> {


    Optional<List<TicketUserRelationEntity>> findByTicketEntityTicketId(int ticketId);

    Optional<List<TicketUserRelationEntity>> findAllByTicketEntity(TicketEntity ticketEntity);

    @Modifying
    @Query("DELETE FROM TicketUserRelationEntity WHERE ticketEntity.ticketId = ?1")
    void deleteByTicketEntityTicketId(int ticketId);

}
