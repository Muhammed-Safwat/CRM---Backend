package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber,Long> {


}
