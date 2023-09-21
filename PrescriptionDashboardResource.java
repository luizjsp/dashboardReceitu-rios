package br.com.mv.clinic.rest;

import br.com.mv.clinic.constants.AppConstants;
import br.com.mv.clinic.dto.dashboard.PrescriptionDashboardFilter;
import br.com.mv.clinic.dto.dashboard.PrescriptionsDashboardDTO;
import br.com.mv.clinic.service.AbstractMessage;
import br.com.mv.clinic.service.prescription_dashboard.PrescriptionDashboardService;
import com.codahale.metrics.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = AppConstants.PATH + AppConstants.V1 + "/dashboard")
public class PrescriptionDashboardResource extends AbstractMessage {

    @Autowired
    private PrescriptionDashboardService prescriptionDashboardService;

    public PrescriptionDashboardResource(PrescriptionDashboardService prescriptionDashboardService) {
        this.prescriptionDashboardService = prescriptionDashboardService;
    }

    @Timed
    @Transactional
    @RequestMapping(value = "/prescription",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PrescriptionsDashboardDTO> findMetricsForPrescriptionsDashboard(PrescriptionDashboardFilter filter) throws IOException {
        PrescriptionsDashboardDTO prescriptionsDashboardDTO = this.prescriptionDashboardService.findMetricsForPrescriptionsDashboard(filter);
        return new ResponseEntity<>(prescriptionsDashboardDTO, HttpStatus.OK);
    }
}