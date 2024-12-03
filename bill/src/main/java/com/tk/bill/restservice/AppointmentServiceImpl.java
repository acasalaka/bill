package com.tk.bill.restservice;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.tk.bill.restdto.response.AppointmentResponseDTO;
import com.tk.bill.restdto.response.BaseResponseDTO;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
   private final WebClient webClient;

   public AppointmentServiceImpl(WebClient.Builder webClientBuilder) {
      this.webClient = webClientBuilder
      .baseUrl("http://localhost:8081/api")
        .build();
   }

    @Override
    public AppointmentResponseDTO getAppointmentFromRest(String appointmentId) throws Exception {
    var appointment = webClient
        .get()
        .uri("/appointment?id=" + appointmentId )
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<AppointmentResponseDTO>>() {})
        .block();

        if (appointment == null) {
            throw new Exception("Failed consume API getAppointmentById");
        }

        if (appointment.getStatus() != 200) {
            throw new Exception(appointment.getMessage());
        }

        return appointment.getData();
    }
}