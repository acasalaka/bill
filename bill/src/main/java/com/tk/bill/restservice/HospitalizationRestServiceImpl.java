package com.tk.bill.restservice;

import java.util.UUID;

import org.jboss.jandex.ParameterizedType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.tk.bill.restdto.response.BaseResponseDTO;
import com.tk.bill.restdto.response.BillResponseDTO;
import com.tk.bill.restdto.response.ReservationResponseDTO;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class HospitalizationRestServiceImpl implements HospitalizationRestService {

    private final WebClient webClient;
    

    public HospitalizationRestServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
            .baseUrl("http://localhost:8083/api")
            .build();
    }

    @Override
    public ReservationResponseDTO getReservationFromRest(String reservationId) throws Exception {
        var reservation = webClient
            .get()
            .uri("/reservations/" + reservationId)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<ReservationResponseDTO>>() {})
            .block();

            if (reservation == null) {
                throw new Exception("Failed consume API");
            }
    
            if (reservation.getStatus() != 200) {
                throw new Exception(reservation.getMessage());
            }

           return reservation.getData();
    }
    // @Override
    // public Double getReservationFee(String reservationId) {
    //     ReservationResponseDTO reservation;
    //     try {
    //         reservation = getReservationFromRest(reservationId);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return null;
    //     }
    //     Double reservationFee = reservation.getReservationFee();
    //     return reservationFee;
    // }

    
    


}
