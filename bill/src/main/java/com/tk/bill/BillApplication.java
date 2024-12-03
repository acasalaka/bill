package com.tk.bill;

import com.github.javafaker.Faker;
import com.tk.bill.model.Bill;
import com.tk.bill.model.BillStatus;
import com.tk.bill.repository.BillDb;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@SpringBootApplication
public class BillApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner run(BillDb billDb) {
        return args -> {
            Faker faker = new Faker(new Locale("in-ID"));

            // Generate 5 Bills
            for (int i = 0; i < 5; i++) {
                Bill bill = new Bill();

                // Generate random IDs
                bill.setId(UUID.randomUUID());
                bill.setPatientId(UUID.randomUUID());

                // Randomly assign either Appointment ID or Reservation ID
                if (faker.bool().bool()) {
                    bill.setAppointmentId("APP-" + faker.number().digits(5));
                    bill.setReservationId(null);
                } else {
                    bill.setReservationId("RES-" + faker.number().digits(5));
                    bill.setAppointmentId(null);
                }

                // Policy ID is optional
                if (faker.bool().bool()) {
                    bill.setPolicyId("POLICY-" + faker.number().digits(4));
                }

                // Set Status to "TREATMENT_IN_PROGRESS" upon creation
                bill.setStatus(BillStatus.TREATMENT_IN_PROGRESS);

                // Set timestamps
                bill.setCreatedAt(new Date());
                bill.setUpdatedAt(new Date());


                // Save to the database
                billDb.save(bill);

                // Log generated bill
                System.out.println("Generated Bill:");
                System.out.println("ID: " + bill.getId());
                System.out.println("Patient ID: " + bill.getPatientId());
                System.out.println("Appointment ID: " + bill.getAppointmentId());
                System.out.println("Reservation ID: " + bill.getReservationId());
                System.out.println("Policy ID: " + bill.getPolicyId());
                System.out.println("Status: " + bill.getStatus());
                System.out.println("Created At: " + bill.getCreatedAt());
                System.out.println("Updated At: " + bill.getUpdatedAt());
                System.out.println("==========================================");
            }
        };
    }
}
