package com.tk.bill.restservice;

import java.util.List;
import java.util.UUID;

import com.tk.bill.restdto.response.CoverageResponseDTO;
import com.tk.bill.restdto.response.PolicyResponseDTO;

public interface PolicyService {
     List<PolicyResponseDTO> getPoliciesByTreatments(List<Long> idTreatments) throws Exception;
     List<CoverageResponseDTO> getUsedCoverages(String id) throws Exception;
     PolicyResponseDTO getPolicyById(String policyId) throws Exception;
}
