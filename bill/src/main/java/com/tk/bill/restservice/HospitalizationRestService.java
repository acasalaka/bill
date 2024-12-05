package com.tk.bill.restservice;

import java.util.UUID;

import com.tk.bill.restdto.response.BillResponseDTO;
import com.tk.bill.restdto.response.ReservationResponseDTO;

public interface HospitalizationRestService {
    ReservationResponseDTO getReservationByPatientId(UUID patientId);
    ReservationResponseDTO getReservationFromRest (String reservationId) throws Exception;
}
