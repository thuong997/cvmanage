package com.bezkoder.springjwt.repository;


import com.bezkoder.springjwt.models.TicketDepRelationEntity;
import com.bezkoder.springjwt.models.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketDepartmentRepository extends JpaRepository<TicketDepRelationEntity, Integer> {
    List<TicketEntity> findByDepEntityDepId(int depId);

    Optional<List<TicketDepRelationEntity>> findByTicketEntityTicketId(int ticketId);

    Optional<List<TicketDepRelationEntity>> findByTicketEntity(TicketEntity ticketEntity);

    Optional<List<TicketDepRelationEntity>> findAllByTicketEntity(TicketEntity ticketEntity);

    @Modifying
    @Query("DELETE FROM TicketDepRelationEntity where ticketEntity.ticketId = ?1")
    void deleteByTicketEntityTicketId(int ticketId);

}
