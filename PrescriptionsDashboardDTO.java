package br.com.mv.clinic.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrescriptionsDashboardDTO {

    private PrescriptionIndicatorsTotalDTO TotalIndicators;
    private List<PrescriptionIndicatorsDTO> monthlyPrescriptionIndicators;
    private List<MedicationDTO> medicines;
    private List<GraphicPrescriptionDTO> graphicMonthyPrescriptionsIndicators;
    private List<ClientInfoDTO> clientInfo;
    private String startDate;
    private String endDate;


}