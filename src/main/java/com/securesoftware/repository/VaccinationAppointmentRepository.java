package com.securesoftware.repository;

import com.securesoftware.model.VaccinationAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationAppointmentRepository extends JpaRepository<VaccinationAppointment, Long> { }
