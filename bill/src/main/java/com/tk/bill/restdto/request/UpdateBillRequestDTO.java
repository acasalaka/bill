package com.tk.bill.restdto.request;

import com.tk.bill.model.BillStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateBillRequestDTO {
    private String policyId;
    private String reservationId;
    private BillStatus status;


}
