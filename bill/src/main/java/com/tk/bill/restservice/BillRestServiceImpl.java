package com.tk.bill.restservice;

import java.security.Policy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tk.bill.model.Bill;
import com.tk.bill.model.BillStatus;
import com.tk.bill.repository.BillDb;
import com.tk.bill.restdto.request.AddBillRequestDTO;
import com.tk.bill.restdto.request.UpdateBillRequestDTO;
import com.tk.bill.restdto.response.AppointmentResponseDTO;
import com.tk.bill.restdto.response.BillResponseDTO;
import com.tk.bill.restdto.response.CoverageResponseDTO;
import com.tk.bill.restdto.response.PolicyResponseDTO;
import com.tk.bill.restdto.response.ReservationResponseDTO;

import jakarta.transaction.Transactional;
import lombok.experimental.var;

@Service
@Transactional
public class BillRestServiceImpl implements BillRestService {
    @Autowired
    private BillDb billDb;

    @Autowired
    private HospitalizationRestServiceImpl hospitalService;

    @Autowired
    private AppointmentServiceImpl appointmentService;





    private BillResponseDTO billToBillResponseDTO(Bill bill) {
        var billResponseDTO = new BillResponseDTO();


        billResponseDTO.setId(bill.getId());
        billResponseDTO.setPolicyId(bill.getPolicyId());
        billResponseDTO.setAppointmentId(bill.getAppointmentId());
        billResponseDTO.setReservationId(bill.getReservationId());
        billResponseDTO.setPatientId(bill.getPatientId());
        billResponseDTO.setStatus(bill.getStatus());


        return billResponseDTO;
    }

    @Override
    public BillResponseDTO getBillByPatientId(UUID patientId) {
        ReservationResponseDTO reservation;
        AppointmentResponseDTO appointment;
        List<Bill> bills = billDb.findAll();
        for (Bill bill : bills) {
            if (bill.getPatientId().equals(patientId)) {
                BillResponseDTO billResponseDTO = billToBillResponseDTO(bill);
                if(bill.getReservationId() != null  && bill.getAppointmentId() == null || bill.getAppointmentId().isEmpty()){
                    try{
                        reservation = hospitalService.getReservationFromRest(bill.getReservationId());
                        var reservationFee = reservation.getTotalFee();
                        var subtotal =(long)reservationFee;
                        System.out.println("Reservation Fee: " + reservationFee);
                        billResponseDTO.setReservationFee(reservationFee);
                        billResponseDTO.setSubtotal(subtotal);
                        return billResponseDTO;
                    }catch (Exception e){
                        throw new RuntimeException("Failed to get reservation");
                    }
                }
                if (bill.getAppointmentId() != null && bill.getReservationId() == null || bill.getReservationId().isEmpty()){
                    try{
                        appointment = appointmentService.getAppointmentFromRest(bill.getAppointmentId());
                        var appointmentFee = appointment.getTotalFee();
                        System.out.println("Appointment Fee: " + appointmentFee);
                        billResponseDTO.setAppointmentFee(appointmentFee);
                        billResponseDTO.setSubtotal(appointmentFee);
                        return billResponseDTO;
                    }catch (Exception e){
                        throw new RuntimeException("Failed to get appointment");
                    }
                }
            }
        }
        return null;
    }
 
    @Override
    public BillResponseDTO payBill(UUID billId, UpdateBillRequestDTO billDTO) {
       Bill bill = billDb.findById(billId).orElse(null);
       if(bill == null){
        return null;
       }
       
       bill.setPolicyId(billDTO.getPolicyId());
       bill.setReservationId(billDTO.getReservationId());
       System.out.println("PERTAMA : " + billDTO.getStatus());
       System.out.println("KEDUA : " + bill.getStatus());
       bill.setStatus(billDTO.getStatus());
       System.out.println("KETIGA : " + bill.getStatus());
       BillResponseDTO billResponseDTO = billToBillResponseDTO(bill);
       System.out.println("Data = "+ billResponseDTO);
       try {
           var reservation = hospitalService.getReservationFromRest(bill.getReservationId());
           var reservationFee = reservation.getTotalFee();
           billResponseDTO.setReservationFee(reservationFee);
           return billResponseDTO;
       } catch (Exception e) {
           throw new RuntimeException("Failed to get reservation", e);
       }
       
    }

    @Override
    public BillResponseDTO updateBillStatus(UUID billId, UpdateBillRequestDTO billDTO) {
        ReservationResponseDTO reservation;
        AppointmentResponseDTO appointment;
        PolicyResponseDTO policy;
        CoverageResponseDTO coverage;

        Bill bill = billDb.findById(billId).orElseThrow(
            () -> new IllegalArgumentException("Bill dengan ID " + billId + " tidak ditemukan"));

        if (bill.getAppointmentId() != null || !bill.getAppointmentId().isEmpty()) {
            try {
                appointment = appointmentService.getAppointmentFromRest(bill.getAppointmentId());
                var appointmentFee = appointment.getTotalFee();
                bill.setStatus(billDTO.getStatus());
                bill.setUpdatedAt(new Date());
                billDb.save(bill);
                BillResponseDTO billResponseDTO = billToBillResponseDTO(bill);
                billResponseDTO.setAppointmentFee(appointmentFee);
                billResponseDTO.setSubtotal(appointmentFee);
                return billResponseDTO;
            } catch (Exception e) {
                throw new RuntimeException("Failed to get appointment", e);
            }
        }
        
        return null;
    }

    @Override
    public BillResponseDTO addAppointmentBill(AddBillRequestDTO billDTO) throws Exception {
        ReservationResponseDTO reservation;
        if (billDTO.getAppointmentId() == null || billDTO.getAppointmentId().isEmpty()){
            throw new Exception("Appointment ID can't be null or empty");
        }

        AppointmentResponseDTO appointment = appointmentService.getAppointmentFromRest(billDTO.getAppointmentId());
        if(appointment == null){
            throw new Exception("Appointment ID is not valid or not found.");
        }

        if(appointment.getStatus().equals("Done")){
            Bill bill = new Bill();

            
            
            var appointmentFee = appointment.getTotalFee();
            bill.setId(UUID.randomUUID());
            bill.setPolicyId(billDTO.getPolicyId());
            bill.setAppointmentId(billDTO.getAppointmentId());
            bill.setReservationId(billDTO.getReservationId());
            bill.setPatientId(billDTO.getPatientId());
            bill.setStatus(BillStatus.UNPAID);
            bill.setCreatedAt(new Date());
            bill.setUpdatedAt(new Date());

            billDb.save(bill);

            BillResponseDTO billResponseDTO = billToBillResponseDTO(bill);
            billResponseDTO.setAppointmentFee(appointmentFee);
            billResponseDTO.setSubtotal(appointmentFee);
            return billResponseDTO;
            
        }

        var appointmentFee = appointment.getTotalFee();

        Bill bill = new Bill();
        
        bill.setId(UUID.randomUUID());
        bill.setPolicyId(billDTO.getPolicyId());
        bill.setAppointmentId(billDTO.getAppointmentId());
        bill.setReservationId(billDTO.getReservationId());
        bill.setPatientId(billDTO.getPatientId());
        bill.setStatus(BillStatus.TREATMENT_IN_PROGRESS);
        bill.setCreatedAt(new Date());
        bill.setUpdatedAt(new Date());

        billDb.save(bill);

        if(appointment.getTreatments() != null && !appointment.getTreatments().isEmpty()){
            BillResponseDTO billResponseDTO = billToBillResponseDTO(bill);
            billResponseDTO.setAppointmentFee(appointmentFee);
            billResponseDTO.setSubtotal(appointmentFee);
            return billResponseDTO;
            
        }

        BillResponseDTO billResponseDTO = billToBillResponseDTO(bill);
        billResponseDTO.setAppointmentFee(appointmentFee);

        return billResponseDTO;
        
    }

    @Override
    public BillResponseDTO addReservationBill(AddBillRequestDTO billDTO) throws Exception {

        if (billDTO.getReservationId() == null || billDTO.getReservationId().isEmpty()) {
            throw new Exception("Reservation ID is mandatory and cannot be null or empty.");
        }
    
        ReservationResponseDTO reservation = hospitalService.getReservationFromRest(billDTO.getReservationId());
        if (reservation == null) {
            throw new Exception("Reservation ID is not valid or not found.");
        }

        if (billDTO.getAppointmentId() != null){
            throw new Exception("This Bill is Related with an Appointment");
        }

        var reservationFee = reservation.getTotalFee();


        Bill bill = new Bill();

        bill.setId(UUID.randomUUID());
        bill.setPolicyId(billDTO.getPolicyId());
        bill.setAppointmentId(billDTO.getAppointmentId());
        bill.setReservationId(billDTO.getReservationId());
        bill.setPatientId(billDTO.getPatientId());
        bill.setStatus(BillStatus.UNPAID);
        bill.setCreatedAt(new Date());
        bill.setUpdatedAt(new Date());


        billDb.save(bill);
        BillResponseDTO billResponseDTO = billToBillResponseDTO(bill);
        billResponseDTO.setReservationFee(reservationFee);
        billResponseDTO.setSubtotal((long) reservationFee);

        return billResponseDTO;
        
    }

    @Override
    public List<BillResponseDTO> getAllBill() {
        List<Bill> bills = billDb.findAll();
        List<BillResponseDTO> billResponseDTOs = new ArrayList<>();
        for (Bill bill : bills) {
            billResponseDTOs.add(billToBillResponseDTO(bill));
        }
        return billResponseDTOs;
    }

    @Override
    public BillResponseDTO getBillById(UUID billId) {
        Bill bill = billDb.findById(billId).orElse(null);
        if (bill == null) {
            return null;
        }
        return billToBillResponseDTO(bill);
    }

    @Override
    public long countTotalBill(long appointmentFee, double reservationFee, long policyFee) {
        return appointmentFee + (long) reservationFee + policyFee;
    }

    
    


}
