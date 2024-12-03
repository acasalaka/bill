package com.tk.bill.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tk.bill.model.Bill;

@Repository
public interface BillDb extends JpaRepository<Bill, UUID> {
    List<Bill> findByPatientId(UUID patientId);
    List<Bill> findByPolicyId(String policyId);
    List<Bill> findByAppointmentId(String appointmentId);
    List<Bill> findByReservationId(String reservationId);
    List<Bill> findByPatientIdAndPolicyId(UUID patientId, String policyId);
    List<Bill> findByPatientIdAndAppointmentId(UUID patientId, String appointmentId);
    List<Bill> findByPatientIdAndReservationId(UUID patientId, String reservationId);
    List<Bill> findByPolicyIdAndAppointmentId(String policyId, String appointmentId);
    List<Bill> findByPolicyIdAndReservationId(String policyId, String reservationId);
    List<Bill> findByAppointmentIdAndReservationId(String appointmentId, String reservationId);
    List<Bill> findByPatientIdAndPolicyIdAndAppointmentId(UUID patientId, String policyId, String appointmentId);
    List<Bill> findByPatientIdAndPolicyIdAndReservationId(UUID patientId, String policyId, String reservationId);
    List<Bill> findByPatientIdAndAppointmentIdAndReservationId(UUID patientId, String appointmentId, String reservationId);
    List<Bill> findByPolicyIdAndAppointmentIdAndReservationId(String policyId, String appointmentId, String reservationId);
    List<Bill> findByPatientIdAndPolicyIdAndAppointmentIdAndReservationId(UUID patientId, String policyId, String appointmentId, String reservationId);
    
}
