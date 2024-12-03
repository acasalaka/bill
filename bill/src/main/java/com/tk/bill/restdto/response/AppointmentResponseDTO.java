package com.tk.bill.restdto.response;

import java.util.UUID;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentResponseDTO {
    private String id;
    private UUID idDoctor;
    private Date date;
    private String diagnosis;
    private Long totalFee;
    private int status;
    
}
