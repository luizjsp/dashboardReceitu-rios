package br.com.mv.clinic.dto.dashboard;

import br.com.mv.clinic.util.DateDefaultDeserializer;
import br.com.mv.clinic.util.DateDefaultSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrescriptionDashboardFilter {

    private Long interval;
    private Boolean allUnimeds;
    private String clientKey;
    private String startDate;
    private String endDate;
    private List<Long> insurances;
    private List<Long> specialties;
}