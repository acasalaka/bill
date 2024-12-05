package com.tk.bill.restdto.response;

import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentResponseDTO {
    private String id;
    private String doctorName;
    private String patientName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String diagnosis;
    private List<String> treatments;
    private Long totalFee;
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;
    
}
