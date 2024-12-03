package com.tk.bill.restservice;

import com.tk.bill.restdto.response.AppointmentResponseDTO;

public interface AppointmentService {
     AppointmentResponseDTO getAppointmentFromRest(String appointmentId) throws Exception;
     
}

