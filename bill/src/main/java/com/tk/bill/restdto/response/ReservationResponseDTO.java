package com.tk.bill.restdto.response;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationResponseDTO {
    private String id;
    private UUID patientId;
    private Date dateIn;
    private Date dateOut;
    private double totalFee;
    private String roomId;
}
