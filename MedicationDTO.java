package br.com.mv.clinic.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MedicationDTO {

    private int year;
    private int month;
    private String drugName;
    private String presentation;
    private int totalDrugPrescription;
    private BigDecimal totalMedicationCost;

}