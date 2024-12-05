package com.tk.bill.restcontroller;

import java.util.UUID;
import java.util.Date;
import java.util.List;

import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tk.bill.model.Bill;
import com.tk.bill.restdto.request.AddBillRequestDTO;
import com.tk.bill.restdto.request.UpdateBillRequestDTO;
import com.tk.bill.restdto.response.BaseResponseDTO;
import com.tk.bill.restdto.response.BillResponseDTO;
import com.tk.bill.restservice.BillRestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bill")
public class BillRestController {

    @Autowired
    BillRestService billRestService;

    @GetMapping("/viewall")
    public ResponseEntity<BaseResponseDTO<List<BillResponseDTO>>> listBill() {
        var baseResponseDTO = new BaseResponseDTO<List<BillResponseDTO>>();
        var bill = billRestService.getAllBill();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setMessage("List of bills");
        baseResponseDTO.setTimestamp(new Date());
        baseResponseDTO.setData(bill);
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
    
    @GetMapping("/{patientId}")
    public ResponseEntity<?> detailBill(@PathVariable("patientId") UUID patientId) {
        var baseResponseDTO = new BaseResponseDTO<BillResponseDTO>();
        BillResponseDTO bill = billRestService.getBillByPatientId(patientId);
        if (bill == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage("Bill not found");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setMessage(String.format("Bill dengan id %s berhasil ditemukan", bill.getId()));
        baseResponseDTO.setTimestamp(new Date());
        baseResponseDTO.setData(bill);

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/detail/{billId}")
    public ResponseEntity<?> detailBillById(@PathVariable("billId") UUID billId) {
        var baseResponseDTO = new BaseResponseDTO<BillResponseDTO>();
        BillResponseDTO bill = billRestService.getBillById(billId);
        if (bill == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage("Bill not found");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setMessage(String.format("Bill dengan id %s berhasil ditemukan", bill.getId()));
        baseResponseDTO.setTimestamp(new Date());
        baseResponseDTO.setData(bill);

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }


    // Ini yang pay bill 
    @PutMapping("/{billId}/update")
    public ResponseEntity<?> updateBill(@PathVariable("billId") UUID billId, @RequestBody UpdateBillRequestDTO billDTO) {
        try {
            BillResponseDTO billResponse = billRestService.payBill(billId, billDTO);
            var response = new BaseResponseDTO<BillResponseDTO>();
            response.setStatus(200);
            response.setMessage("Bill dengan ID " + billId + " berhasil diubah");
            response.setData(billResponse);
            response.setTimestamp(new Date());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            var response = new BaseResponseDTO<>();
            response.setStatus(400);
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createByAppointment")
    public ResponseEntity<?> createBillAppointment(@RequestBody AddBillRequestDTO billDTO) {
        try {
            BillResponseDTO billResponse = billRestService.addAppointmentBill(billDTO);
  
            var baseResponseDTO = new BaseResponseDTO<>();
            baseResponseDTO.setStatus(HttpStatus.CREATED.value());
            baseResponseDTO.setMessage("Bill created successfully");
            baseResponseDTO.setData(billResponse);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            var baseResponseDTO = new BaseResponseDTO<>();
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage("Failed to create bill: " + e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createByReservation")
    public ResponseEntity<?> createBillReservation(@Valid @RequestBody AddBillRequestDTO billDTO) {
        try {
            BillResponseDTO billResponse = billRestService.addReservationBill(billDTO);
  
            var baseResponseDTO = new BaseResponseDTO<>();
            baseResponseDTO.setStatus(HttpStatus.CREATED.value());
            baseResponseDTO.setMessage("Bill created successfully");
            baseResponseDTO.setData(billResponse);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            var baseResponseDTO = new BaseResponseDTO<>();
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage("Failed to create bill: " + e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }


}
