package com.tk.bill.restservice;

import java.util.List;
import java.util.UUID;

import com.tk.bill.model.Bill;
import com.tk.bill.restdto.request.AddBillRequestDTO;
import com.tk.bill.restdto.request.UpdateBillRequestDTO;
import com.tk.bill.restdto.response.BillResponseDTO;

public interface BillRestService {
    BillResponseDTO getBillByPatientId(UUID patientId);
    BillResponseDTO updateBillStatus(UUID billId, UpdateBillRequestDTO billDTO);
    BillResponseDTO addAppointmentBill (AddBillRequestDTO billDTO) throws Exception;
    BillResponseDTO addReservationBill (AddBillRequestDTO billDTO) throws Exception;
    List<BillResponseDTO> getAllBill();
    BillResponseDTO getBillById(UUID billId);
    long countTotalBill(long appointmentFee, double reservationFee, long policyFee);
    BillResponseDTO payBill(UUID billId, UpdateBillRequestDTO billDTO);
 

}
