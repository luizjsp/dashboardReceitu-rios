package br.com.mv.clinic.service.prescription_dashboard;

import br.com.mv.clinic.dto.dashboard.*;
import br.com.mv.clinic.repository.PrescriptionDashboardRepository;
import br.com.mv.clinic.service.AbstractMessage;
import br.com.mv.clinic.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PrescriptionDashboardService extends AbstractMessage {

    @Autowired
    private PrescriptionDashboardRepository prescriptionDashboardRepository;

    public PrescriptionsDashboardDTO findMetricsForPrescriptionsDashboard(PrescriptionDashboardFilter filter) throws IOException {

        this.buildFiltersDashboardFilter(filter);
        String startDate = filter.getStartDate();
        String endDate = filter.getEndDate();
        String headerClientKey = HeaderUtil.getClientKey();
        String clientKey = !"clinic_mv".equalsIgnoreCase(headerClientKey) ? headerClientKey : filter.getClientKey();

        PrescriptionIndicatorsTotalDTO indicatorsTotalDTO = calcularIndicadoresTotais(clientKey, filter);
        List<MedicationDTO> medications = calcularMedicamentos(clientKey, filter);
        List<PrescriptionIndicatorsDTO> monthlyPrescriptionIndicators = calcularIndicadoresMensaisPrescricao(clientKey, filter);
        List<GraphicPrescriptionDTO> graphicMonthyPrescriptionsIndicators = calcularGraficoPrescricoes(monthlyPrescriptionIndicators);


        PrescriptionsDashboardDTO dashboardDTO = new PrescriptionsDashboardDTO();
        dashboardDTO.setTotalIndicators(indicatorsTotalDTO);
        dashboardDTO.setMonthlyPrescriptionIndicators(monthlyPrescriptionIndicators);
        dashboardDTO.setMedicines(medications);
        dashboardDTO.setGraphicMonthyPrescriptionsIndicators(graphicMonthyPrescriptionsIndicators);
        if ("clinic_mv".equalsIgnoreCase(headerClientKey)) {
            List<ClientInfoDTO> prescriptionsTotalClient = calcularTotalPrescricoesClienteMv(filter.getClientKey(), filter);
            dashboardDTO.setClientInfo(prescriptionsTotalClient);
        }
        dashboardDTO.setStartDate(startDate);
        dashboardDTO.setEndDate(endDate);

        return dashboardDTO;
    }

    private PrescriptionIndicatorsTotalDTO calcularIndicadoresTotais(String clientKey, PrescriptionDashboardFilter filter) throws IOException {

        this.buildFiltersDashboardFilter(filter);

        PrescriptionIndicatorsTotalDTO indicatorsTotalDTO = new PrescriptionIndicatorsTotalDTO();

        String clientkeyString = (clientKey != null && !clientKey.isEmpty() && !clientKey.equals("clinic_mv")) ? clientKey : null;

        boolean allUnimeds = false;

        if (filter.getAllUnimeds() != null) {
            String allUnimedsString = String.valueOf(filter.getAllUnimeds());
            allUnimeds = Boolean.parseBoolean(allUnimedsString);
        }

        List<Object[]> indicatorsData = null;
        if (allUnimeds) {
                if (filter.getInsurances() == null || filter.getInsurances().isEmpty()) {
                    if (filter.getSpecialties() == null || filter.getSpecialties().isEmpty()) {
                        // Lógica para quando allUnimeds estiver ativado (true)
                        indicatorsData = prescriptionDashboardRepository.countTotalPrescriptionsByClientKeyAllUniHealthPlan(
                                clientkeyString,
                                filter.getStartDate(),
                                filter.getEndDate()
                        );
                    } else {
                        indicatorsData = prescriptionDashboardRepository.countTotalPrescriptionsByAndSpecialtiesAllUniHealthPlan(
                                clientkeyString,
                                filter.getStartDate(),
                                filter.getEndDate(),
                                filter.getSpecialties()
                        );
                    }
                } else {
                    if (filter.getSpecialties() == null || filter.getSpecialties().isEmpty()) {
                        // Consulta 4: consultar com insurance, mas sem especialidade, com allUnimeds
                        indicatorsData = prescriptionDashboardRepository.countTotalPrescriptionsByInsurancesAllUniHealthPlan(
                                clientkeyString,
                                filter.getStartDate(),
                                filter.getEndDate(),
                                filter.getInsurances()
                        );
                    } else {
                        // Consulta 4: com insurance e com especialidade, com allUnimeds
                        indicatorsData = prescriptionDashboardRepository.countTotalPrescriptionsByInsurancesAndSpecialtiesAllUniHealthPlan(
                                clientkeyString,
                                filter.getStartDate(),
                                filter.getEndDate(),
                                filter.getInsurances(),
                                filter.getSpecialties()
                        );
                    }
                }
            } else {
                if (filter.getInsurances() == null || filter.getInsurances().isEmpty()) {
                    if (filter.getSpecialties() == null || filter.getSpecialties().isEmpty()) {
                        // Consulta 1: sem insurance e sem especialidade, sem allUnimeds
                        indicatorsData = prescriptionDashboardRepository.countTotalPrescriptionsByClientKey(
                                clientkeyString,
                                filter.getStartDate(),
                                filter.getEndDate()
                        );
                    } else {// Consulta 4: sem insurance, mas com especialidade, sem allUnimeds
                        indicatorsData = prescriptionDashboardRepository.countTotalPrescriptionsBySpecialties(
                                clientkeyString,
                                filter.getStartDate(),
                                filter.getEndDate(),
                                filter.getSpecialties()
                        );
                    }
                } else {
                    if (filter.getSpecialties() == null || filter.getSpecialties().isEmpty()) {
                        // Consulta 4: consultar com insurance, mas sem especialidade, sem allUnimeds
                        indicatorsData = prescriptionDashboardRepository.countTotalPrescriptionsByInsurances(
                                clientkeyString,
                                filter.getStartDate(),
                                filter.getEndDate(),
                                filter.getInsurances()
                        );
                    } else {
                        indicatorsData = prescriptionDashboardRepository.countTotalPrescriptionsByInsurancesAndSpecialties(
                                clientkeyString,
                                filter.getStartDate(),
                                filter.getEndDate(),
                                filter.getInsurances(),
                                filter.getSpecialties()
                        );
                    }
                }
            }
            for (Object[] data : indicatorsData) {
            int totalPrescriptions = ((Number) data[0]).intValue();
            int totalPrescribingDoctors = ((Number) data[1]).intValue();
            int totalPrescriptionItems = ((Number) data[2]).intValue();
            int totalNonStandardItems = ((Number) data[3]).intValue();
            Double totalPMC20ItensPrescriptions = data[4] != null ? (Double) data[4] : 0;

            indicatorsTotalDTO.setTotalPrescriptions(indicatorsTotalDTO.getTotalPrescriptions() + totalPrescriptions);
            indicatorsTotalDTO.setTotalPrescribingDoctors(indicatorsTotalDTO.getTotalPrescribingDoctors() + totalPrescribingDoctors);
            indicatorsTotalDTO.setTotalPrescriptionItems(indicatorsTotalDTO.getTotalPrescriptionItems() + totalPrescriptionItems);
            indicatorsTotalDTO.setTotalNonStandardItems(indicatorsTotalDTO.getTotalNonStandardItems() + totalNonStandardItems);
            indicatorsTotalDTO.setTotalPMC20ItensPrescriptions(indicatorsTotalDTO.getTotalPMC20ItensPrescriptions() + totalPMC20ItensPrescriptions);
        }
        return indicatorsTotalDTO;
    }
    private List<MedicationDTO> calcularMedicamentos(String clientKey, PrescriptionDashboardFilter filter) throws IOException {

        this.buildFiltersDashboardFilter(filter);

        boolean allUnimeds = false;

        if (filter.getAllUnimeds() != null) {
            String allUnimedsString = String.valueOf(filter.getAllUnimeds());
            allUnimeds = Boolean.parseBoolean(allUnimedsString);
        }

        //List<Object[]> medicationData;
        List<Object[]> medicationData = new ArrayList<>();

        if (allUnimeds) {
            if (clientKey == null || clientKey.isEmpty()) {
                // Consulta sem clientKey
                medicationData = prescriptionDashboardRepository.countTotalMedicationsByMonthAndYearAllUniHealthPlan(
                        null,
                        filter.getStartDate(),
                        filter.getEndDate()
                );
            } else {
                if (filter.getInsurances() == null || filter.getInsurances().isEmpty()) {
                    if (filter.getSpecialties() == null || filter.getSpecialties().isEmpty()) {
                        // Consulta 1: sem insurance e sem especialidade
                        medicationData = prescriptionDashboardRepository.countTotalMedicationsByMonthAndYearAllUniHealthPlan(
                                clientKey,
                                filter.getStartDate(),
                                filter.getEndDate()
                        );
                    } else {// Consulta 4: sem insurance, mas com especialidade
                        medicationData = prescriptionDashboardRepository.countTotalMedicationsByMonthAndYearAndSpecialtiesAllUniHealthPlan(
                                clientKey,
                                filter.getStartDate(),
                                filter.getEndDate(),
                                filter.getSpecialties()
                        );
                    }
                } else {
                    if (filter.getSpecialties() == null || filter.getSpecialties().isEmpty()) {
                        // Consulta 4: consultar com insurance, mas sem especialidade
                        medicationData = prescriptionDashboardRepository.countTotalMedicationsByMonthAndYearAndInsuranceAllUniHealthPlan(
                                clientKey,
                                filter.getStartDate(),
                                filter.getEndDate(),
                                filter.getInsurances()
                        );
                    } else {
                        medicationData = prescriptionDashboardRepository.countTotalMedicationsByMonthAndYearAndInsuranceAndSpecialtiesAllUniHealthPlan(
                                clientKey,
                                filter.getStartDate(),
                                filter.getEndDate(),
                                filter.getInsurances(),
                                filter.getSpecialties()
                        );
                    }
                }
            }
        }
        if (!allUnimeds) {
            if (clientKey == null || clientKey.isEmpty()) {
                    // Consulta sem clientKey
                medicationData = prescriptionDashboardRepository.countTotalMedicationsByMonthAndYear(
                   null,
                    filter.getStartDate(),
                    filter.getEndDate()
                );
            } else {
                if (filter.getInsurances() == null || filter.getInsurances().isEmpty()) {
                    if (filter.getSpecialties() == null || filter.getSpecialties().isEmpty()) {
                        // Consulta 1: sem insurance e sem especialidade
                        medicationData = prescriptionDashboardRepository.countTotalMedicationsByMonthAndYear(
                            clientKey,
                            filter.getStartDate(),
                            filter.getEndDate()
                        );
                    } else {// Consulta 4: sem insurance, mas com especialidade
                        medicationData = prescriptionDashboardRepository.countTotalMedicationsByMonthAndYearAndSpecialties(
                            clientKey,
                            filter.getStartDate(),
                            filter.getEndDate(),
                            filter.getSpecialties()
                        );
                    }
                } else {
                    if (filter.getSpecialties() == null || filter.getSpecialties().isEmpty()) {
                        // Consulta 4: consultar com insurance, mas sem especialidade
                        medicationData = prescriptionDashboardRepository.countTotalMedicationsByMonthAndYearAndInsurance(
                            clientKey,
                            filter.getStartDate(),
                            filter.getEndDate(),
                            filter.getInsurances()
                        );
                    } else {
                        medicationData = prescriptionDashboardRepository.countTotalMedicationsByMonthAndYearAndInsuranceAndSpecialties(
                            clientKey,
                            filter.getStartDate(),
                            filter.getEndDate(),
                            filter.getInsurances(),
                            filter.getSpecialties()
                        );
                    }
                }
            }

        }
        return medicationData.stream()
              .filter(data -> data != null && data.length >= 6)
              .map(data -> {
              int year = ((Number) data[0]).intValue();
              int month = ((Number) data[1]).intValue();
              String drugName = (String) data[2];
              String presentation = (String) data[3];
              int totalDrugPrescription = ((Number) data[4]).intValue();
              Double totalMedicationCostDouble = (Double) data[5];
              BigDecimal totalMedicationCost = BigDecimal.valueOf(totalMedicationCostDouble);

              MedicationDTO medicationDTO = new MedicationDTO();
              medicationDTO.setYear(year);
              medicationDTO.setMonth(month);
              medicationDTO.setDrugName(drugName);
              medicationDTO.setPresentation(presentation);
              medicationDTO.setTotalDrugPrescription(totalDrugPrescription);
              medicationDTO.setTotalMedicationCost(totalMedicationCost);

              return medicationDTO;

              })
              .collect(Collectors.toList());
    }
    private List<PrescriptionIndicatorsDTO> calcularIndicadoresMensaisPrescricao(String clientKey, PrescriptionDashboardFilter filter) throws IOException {

        this.buildFiltersDashboardFilter(filter);

        boolean allUnimeds = false;

        if (filter.getAllUnimeds() != null) {
            String allUnimedsString = String.valueOf(filter.getAllUnimeds());
            allUnimeds = Boolean.parseBoolean(allUnimedsString);
        }

        //List<Object[]> monthlyPrescriptionsIndicators;
        List<Object[]> monthlyPrescriptionsIndicators = new ArrayList<>();

        if (allUnimeds) {
            if (clientKey == null || clientKey.isEmpty()) {
                // Consulta sem clientKey
                monthlyPrescriptionsIndicators = prescriptionDashboardRepository.countMonthlyPrescriptionIndicatorsAllUniHealthPlan(
                        null,
                        filter.getStartDate(),
                        filter.getEndDate()
                );
            } else {
                if (filter.getInsurances() == null || filter.getInsurances().isEmpty()) {
                    if (filter.getSpecialties() == null || filter.getSpecialties().isEmpty()) {
                        // Consulta 1: sem insurance e sem especialidade
                        monthlyPrescriptionsIndicators = prescriptionDashboardRepository.countMonthlyPrescriptionIndicatorsAllUniHealthPlan(
                                clientKey,
                                filter.getStartDate(),
                                filter.getEndDate()
                        );
                    } else {
                        monthlyPrescriptionsIndicators = prescriptionDashboardRepository.countMonthlyPrescriptionIndicatorsAndSpecialtiesAllUniHealthPlan(
                                clientKey,
                                filter.getStartDate(),
                                filter.getEndDate(),
                                filter.getSpecialties()
                        );
                    }
                } else if (filter.getSpecialties() == null || filter.getSpecialties().isEmpty()) {
                    // Consulta 4: consultar com insurance, mas sem especialidade
                    monthlyPrescriptionsIndicators = prescriptionDashboardRepository.countMonthlyPrescriptionIndicatorsAndInsuranceAllUniHealthPlan(
                            clientKey,
                            filter.getStartDate(),
                            filter.getEndDate(),
                            filter.getInsurances()
                    );
                } else {
                    monthlyPrescriptionsIndicators = prescriptionDashboardRepository.countMonthlyPrescriptionIndicatorsAndInsuranceAndSpecialtiesAllUniHealthPlan(
                            clientKey,
                            filter.getStartDate(),
                            filter.getEndDate(),
                            filter.getInsurances(),
                            filter.getSpecialties()
                    );

                }

            }

        } else if (!allUnimeds) {
            if (clientKey == null || clientKey.isEmpty()) {
                // Consulta sem clientKey
                monthlyPrescriptionsIndicators = prescriptionDashboardRepository.countMonthlyPrescriptionIndicators(
                   null,
                    filter.getStartDate(),
                    filter.getEndDate()
                );
            } else {
                if (filter.getInsurances() == null || filter.getInsurances().isEmpty()) {
                    if (filter.getSpecialties() == null || filter.getSpecialties().isEmpty()) {
                        // Consulta 1: sem insurance e sem especialidade
                        monthlyPrescriptionsIndicators = prescriptionDashboardRepository.countMonthlyPrescriptionIndicators(
                            clientKey,
                            filter.getStartDate(),
                            filter.getEndDate()
                        );
                    } else {// Consulta 4: sem insurance, mas com especialidade
                        monthlyPrescriptionsIndicators = prescriptionDashboardRepository.countMonthlyPrescriptionIndicatorsAndSpecialties(
                            clientKey,
                            filter.getStartDate(),
                            filter.getEndDate(),
                            filter.getSpecialties()
                        );
                    }
                } else if (filter.getSpecialties() == null || filter.getSpecialties().isEmpty()) {
                    // Consulta 4: consultar com insurance, mas sem especialidade
                    monthlyPrescriptionsIndicators = prescriptionDashboardRepository.countMonthlyPrescriptionIndicatorsAndInsurance(
                        clientKey,
                        filter.getStartDate(),
                        filter.getEndDate(),
                        filter.getInsurances()
                    );
                } else {
                    monthlyPrescriptionsIndicators = prescriptionDashboardRepository.countMonthlyPrescriptionIndicatorsAndInsuranceAndSpecialties(
                        clientKey,
                        filter.getStartDate(),
                        filter.getEndDate(),
                        filter.getInsurances(),
                        filter.getSpecialties()
                    );
                }
            }
        }
        int startYear = Integer.MAX_VALUE;
        int endYear = Integer.MIN_VALUE;
        for (Object[] data : monthlyPrescriptionsIndicators) {
            int dataYear = ((Number) data[0]).intValue();
            if (dataYear < startYear) {
                startYear = dataYear;
            }
            if (dataYear > endYear) {
                endYear = dataYear;
            }
        }

        if (filter.getStartDate() == null || filter.getStartDate().isEmpty() ||
                filter.getEndDate() == null || filter.getEndDate().isEmpty()) {
            throwsException("error.dashboard.date-required");
        }

        LocalDate startDate = LocalDate.parse(filter.getStartDate());
        LocalDate endDate = LocalDate.parse(filter.getEndDate());

        Map<Integer, Map<Integer, PrescriptionIndicatorsDTO>> indicatorsMap = new HashMap<>();

        for (Object[] data : monthlyPrescriptionsIndicators) {
            int dataYear = ((Number) data[0]).intValue();
            int dataMonth = ((Number) data[1]).intValue();

            PrescriptionIndicatorsDTO indicatorsDTO = new PrescriptionIndicatorsDTO();
            indicatorsDTO.setTotalPrescriptions(((Number) data[2]).intValue());
            indicatorsDTO.setTotalPrescribingDoctors(((Number) data[3]).intValue());
            indicatorsDTO.setTotalPrescriptionItems(((Number) data[4]).intValue());
            indicatorsDTO.setTotalNonStandardItems(((Number) data[5]).intValue());
            indicatorsDTO.setTotalPMCItensPrescriptions((Double) data[6]);

            indicatorsMap.computeIfAbsent(dataYear, year -> new HashMap<>()).put(dataMonth, indicatorsDTO);
        }

        List<PrescriptionIndicatorsDTO> monthlyPrescriptionIndicators = new ArrayList<>();

        for (LocalDate date = endDate; !date.isBefore(startDate.minusMonths(1)); date = date.minusMonths(1)) {
            int year = date.getYear();
            int month = date.getMonthValue();

            PrescriptionIndicatorsDTO indicatorsDTO = indicatorsMap
                    .getOrDefault(year, Collections.emptyMap())
                    .getOrDefault(month, new PrescriptionIndicatorsDTO());

            indicatorsDTO.setYear(year);
            indicatorsDTO.setMonth(month);

            monthlyPrescriptionIndicators.add(indicatorsDTO);
        }

        List<PrescriptionIndicatorsDTO> sortedIndicators = monthlyPrescriptionIndicators;
        Collections.sort(sortedIndicators, (o1, o2) -> {
            int yearComparison = Integer.compare(o2.getYear(), o1.getYear());
            if (yearComparison == 0) {
                return Integer.compare(o2.getMonth(), o1.getMonth());
            }
            return yearComparison;
        });

        return sortedIndicators;

    }
    private List<GraphicPrescriptionDTO> calcularGraficoPrescricoes(List<PrescriptionIndicatorsDTO> monthlyPrescriptionIndicators) {

        List<GraphicPrescriptionDTO> graphicMonthyPrescriptionsIndicators = new ArrayList<>();
        for (PrescriptionIndicatorsDTO data : monthlyPrescriptionIndicators) {
            int year = data.getYear();
            int month = data.getMonth();
            int totalPrescriptions = data.getTotalPrescriptions();
            int totalPrescribingDoctors = data.getTotalPrescribingDoctors();
            int totalPrescriptionItems = data.getTotalPrescriptionItems();
            int totalNonStandardItems = data.getTotalNonStandardItems();
            double totalPMCItensPrescriptions = data.getTotalPMCItensPrescriptions();

            GraphicPrescriptionDTO graphicPrescriptionDTO = new GraphicPrescriptionDTO();
            graphicPrescriptionDTO.setYear(year);
            graphicPrescriptionDTO.setMonth(month);
            graphicPrescriptionDTO.setTotalPrescriptions(totalPrescriptions);
            graphicPrescriptionDTO.setTotalPrescribingDoctors(totalPrescribingDoctors);
            graphicPrescriptionDTO.setTotalPrescriptionItems(totalPrescriptionItems);
            graphicPrescriptionDTO.setTotalNonStandardItems(totalNonStandardItems);
            graphicPrescriptionDTO.setTotalPMCItensPrescriptions(totalPMCItensPrescriptions);

            graphicMonthyPrescriptionsIndicators.add(graphicPrescriptionDTO);
        }

        Collections.sort(graphicMonthyPrescriptionsIndicators, Comparator.comparingInt(GraphicPrescriptionDTO::getYear)
                .thenComparingInt(GraphicPrescriptionDTO::getMonth));

        return graphicMonthyPrescriptionsIndicators;
    }
    private List<ClientInfoDTO> calcularTotalPrescricoesClienteMv(String clientKey, PrescriptionDashboardFilter filter) throws IOException {

        this.buildFiltersDashboardFilter(filter);

        boolean allUnimeds = false;

        if (filter.getAllUnimeds() != null) {
            String allUnimedsString = String.valueOf(filter.getAllUnimeds());
            allUnimeds = Boolean.parseBoolean(allUnimedsString);
        }

        List<ClientInfoDTO> prescriptionsTotalClient = new ArrayList<>();

        String ClientKey = (clientKey != null && !clientKey.isEmpty() && !clientKey.equals("clinic_mv")) ? clientKey : null;

        List<Object[]> prescriptionsTotalClientMv = null;

        if (allUnimeds) {
            if (filter.getInsurances() == null || filter.getInsurances().isEmpty()) {
                if (filter.getSpecialties() == null || filter.getSpecialties().isEmpty()) {
                    // Consulta 1: sem insurance e sem especialidade
                    prescriptionsTotalClientMv = prescriptionDashboardRepository.countTotalIndicatorsByClientKeyMvAllUniHealthPlan(
                            ClientKey,
                            filter.getStartDate(),
                            filter.getEndDate()
                    );
                } else {// Consulta 4: sem insurance, mas com especialidade
                    prescriptionsTotalClientMv = prescriptionDashboardRepository.countTotalIndicatorsByClientKeyMvAndSpecialtiesAllUniHealthPlan(
                            ClientKey,
                            filter.getStartDate(),
                            filter.getEndDate(),
                            filter.getSpecialties()
                    );
                }
            } else {
                if (filter.getSpecialties() == null || filter.getSpecialties().isEmpty()) {
                    // Consulta 4: consultar com insurance, mas sem especialidade
                    prescriptionsTotalClientMv = prescriptionDashboardRepository.countTotalIndicatorsByClientKeyMvAndInsuranceAllUniHealthPlan(
                            ClientKey,
                            filter.getStartDate(),
                            filter.getEndDate(),
                            filter.getInsurances()
                    );
                } else {
                    prescriptionsTotalClientMv = prescriptionDashboardRepository.countTotalIndicatorsByClientKeyMvAndInsuranceAndSpecialtiesAllUniHealthPlan(
                            ClientKey,
                            filter.getStartDate(),
                            filter.getEndDate(),
                            filter.getInsurances(),
                            filter.getSpecialties()
                    );
                }
            }
        }
        if (!allUnimeds) {
            if (filter.getInsurances() == null || filter.getInsurances().isEmpty()) {
                if (filter.getSpecialties() == null || filter.getSpecialties().isEmpty()) {
                    // Consulta 1: sem insurance e sem especialidade
                    prescriptionsTotalClientMv = prescriptionDashboardRepository.countTotalIndicatorsByClientKeyMv(
                            ClientKey,
                            filter.getStartDate(),
                            filter.getEndDate()
                    );
                } else {// Consulta 4: sem insurance, mas com especialidade
                    prescriptionsTotalClientMv = prescriptionDashboardRepository.countTotalIndicatorsByClientKeyMvAndSpecialties(
                            ClientKey,
                            filter.getStartDate(),
                            filter.getEndDate(),
                            filter.getSpecialties()
                    );
                }
            } else {
                if (filter.getSpecialties() == null || filter.getSpecialties().isEmpty()) {
                    // Consulta 4: consultar com insurance, mas sem especialidade
                    prescriptionsTotalClientMv = prescriptionDashboardRepository.countTotalIndicatorsByClientKeyMvAndInsurance(
                            ClientKey,
                            filter.getStartDate(),
                            filter.getEndDate(),
                            filter.getInsurances()
                    );
                } else {
                    prescriptionsTotalClientMv = prescriptionDashboardRepository.countTotalIndicatorsByClientKeyMvAndInsuranceAndSpecialties(
                            ClientKey,
                            filter.getStartDate(),
                            filter.getEndDate(),
                            filter.getInsurances(),
                            filter.getSpecialties()
                    );
                }
            }
        }
        for (Object[] data : prescriptionsTotalClientMv) {
                clientKey = (String) data[0];
                int totalPrescriptions = ((Number) data[1]).intValue();
                int totalPrescribingDoctors = ((Number) data[2]).intValue();
                int totalPrescriptionItems = ((Number) data[3]).intValue();
                int totalNonStandardItems = ((Number) data[4]).intValue();
                double totalPMCItensPrescriptions = (Double) data[5];

                ClientInfoDTO clientInfoDTO = new ClientInfoDTO();
                clientInfoDTO.setClientKey(clientKey);
                clientInfoDTO.setTotalPrescriptions(totalPrescriptions);
                clientInfoDTO.setTotalPrescribingDoctors(totalPrescribingDoctors);
                clientInfoDTO.setTotalPrescriptionItems(totalPrescriptionItems);
                clientInfoDTO.setTotalNonStandardItems(totalNonStandardItems);
                clientInfoDTO.setTotalPMC20ItensPrescriptions(totalPMCItensPrescriptions);

                prescriptionsTotalClient.add(clientInfoDTO);
            }
        return prescriptionsTotalClient;
    }
        private void buildFiltersDashboardFilter (PrescriptionDashboardFilter filter) throws IOException {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            if (Objects.nonNull(filter.getInterval())) {
                LocalDate instant = LocalDate.now();

                filter.setStartDate(dateFormatter.format(instant.minusDays(filter.getInterval())));
                filter.setEndDate(dateFormatter.format(instant));
            }
            if (Objects.isNull(filter.getStartDate()) || Objects.isNull(filter.getEndDate())) {
                throwsException("error.dashboard.date-required");
            }
        }
    }