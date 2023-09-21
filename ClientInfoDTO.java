package br.com.mv.clinic.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientInfoDTO {

    private String clientKey;
    private int totalPrescriptions;
    private int totalPrescribingDoctors;
    private int totalPrescriptionItems;
    private int totalNonStandardItems;
    private double totalPMC20ItensPrescriptions;

}