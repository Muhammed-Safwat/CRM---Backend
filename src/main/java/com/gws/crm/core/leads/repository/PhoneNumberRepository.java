package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {

   List<PhoneNumber> findAllByLeadId(Long leadId);

    boolean existsByPhone(String mobile);
}
