package com.tk.bill.restservice;

import java.util.List;
import java.util.UUID;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import jakarta.transaction.Transactional;

import com.tk.bill.restdto.response.BaseResponseDTO;
import com.tk.bill.restdto.response.CoverageResponseDTO;
import com.tk.bill.restdto.response.PolicyResponseDTO;

@Service
@Transactional

public class PolicyServiceImpl implements PolicyService {
    private final WebClient webClient;

    public PolicyServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
            .baseUrl("http://localhost:8082/api")
            .build();
    }

    @Override
    public List<PolicyResponseDTO> getPoliciesByTreatments(List<Long> idTreatments) throws Exception {
        var policies = webClient
            .post()
            .uri("/policy/policy-list-by-treatments")
            .bodyValue(idTreatments)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<List<PolicyResponseDTO>>>() {})
            .block();

        if (policies == null) {
            throw new Exception("Failed consume API");
        }

        if (policies.getStatus() != 200) {
            throw new Exception(policies.getMessage());
        }

        return policies.getData();
    }

    @Override
    public List<CoverageResponseDTO> getUsedCoverages(String id) throws Exception {
        var coverages = webClient
            .get()
            .uri("/policy/get_used_coverages")
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<List<CoverageResponseDTO>>>() {})
            .block();

        if (coverages == null) {
            throw new Exception("Failed consume API");
        }

        if (coverages.getStatus() != 200) {
            throw new Exception(coverages.getMessage());
        }

        return coverages.getData();
    }

    @Override
    public PolicyResponseDTO getPolicyById(String policyId) throws Exception {
        var policy = webClient
            .get()
            .uri("/policy/detail?policyId=" + policyId)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<PolicyResponseDTO>>() {})
            .block();

        if (policy == null) {
            throw new Exception("Failed consume API");
        }

        if (policy.getStatus() != 200) {
            throw new Exception(policy.getMessage());
        }

        return policy.getData();
    }

    



}
