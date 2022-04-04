package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.JobLevelRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Repository
public interface TicketJobLevelRepository extends JpaRepository<JobLevelRelationEntity, Integer> {

//    Optional<JobLevelRelationEntity> findByTicketIdAndIsDeletedFalse(int ticketId);
    Optional<List<JobLevelRelationEntity>> findByTicketEntityTicketId(int ticketId);
    @Modifying
    @Query("DELETE FROM JobLevelRelationEntity WHERE ticketEntity.ticketId = ?1")
    void deleteByTicketEntityTicketId(int ticketId);
}
