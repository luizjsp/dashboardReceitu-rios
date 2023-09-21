package br.com.mv.clinic.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrescriptionIndicatorsDTO {

    private int year;
    private int month;
    private int totalPrescriptions;
    private int totalPrescribingDoctors;
    private int totalPrescriptionItems;
    private int totalNonStandardItems;
    private double totalPMCItensPrescriptions;

}