package br.com.mv.clinic.repository;

import br.com.mv.clinic.domain.prescription.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrescriptionDashboardRepository extends JpaRepository<Prescription, Long>, JpaSpecificationExecutor<Prescription> {

    @Query(value = "SELECT\n" +
            "   COUNT(DISTINCT prescription.id) AS total_prescriptions,\n" +
            "   COUNT(DISTINCT em.id) AS totalPrescribingDoctors,\n" +
            "   COUNT(DISTINCT drug_prescription.id) AS totalPrescriptionItems,\n" +
            "   COUNT(DISTINCT drug.id) AS TotalNonStandardizedItems,\n" +
            "   SUM(drug.pmc_20) AS sumPMC20\n" +
            "FROM subscription\n" +
            "INNER JOIN attendance ON subscription.id = attendance.id_subscription\n" +
            "INNER JOIN prescription ON prescription.id_attendance = attendance.id\n" +
            "INNER JOIN drug_prescription ON drug_prescription.id_prescription = prescription.id\n" +
            "INNER JOIN drug ON drug_prescription.id_drug = drug.id\n" +
            "INNER JOIN employee em ON attendance.id_employee = em.id\n" +
            "WHERE\n" +
            "   (:clientKey is null or subscription.client_key = :clientKey)\n" +
            "   AND prescription.created_date BETWEEN :startDate AND :endDate\n" +
            "   AND prescription.deleted = 0\n" +
            "   AND attendance.status != 0\n" +
            "   AND drug_prescription.deleted = 0\n", nativeQuery = true)
    List<Object[]> countTotalPrescriptionsByClientKey(@Param("clientKey") String clientKey,
                                                      @Param("startDate") String startDate,
                                                      @Param("endDate") String endDate);

    @Query(value = "SELECT\n" +
            "   COUNT(DISTINCT prescription.id) AS total_prescriptions,\n" +
            "   COUNT(DISTINCT em.id) AS totalPrescribingDoctors,\n" +
            "   COUNT(DISTINCT drug_prescription.id) AS totalPrescriptionItems,\n" +
            "   COUNT(DISTINCT drug.id) AS TotalNonStandardizedItems,\n" +
            "   SUM(drug.pmc_20) AS sumPMC20\n" +
            "FROM subscription\n" +
            "INNER JOIN attendance ON subscription.id = attendance.id_subscription\n" +
            "INNER JOIN prescription ON prescription.id_attendance = attendance.id\n" +
            "INNER JOIN drug_prescription ON drug_prescription.id_prescription = prescription.id\n" +
            "INNER JOIN drug ON drug_prescription.id_drug = drug.id\n" +
            "INNER JOIN employee em ON attendance.id_employee = em.id\n" +
            "INNER JOIN insurance AS insu ON insu.id = attendance.insurance_id\n" +
            "INNER JOIN health_plan AS hp ON hp.id = insu.health_plan_id\n" +
            "INNER JOIN healthplan_interchange AS hpi ON hpi.ans_health_plan_origin = hp.ans_code\n" +
            "WHERE\n" +
            "   (:clientKey is null or subscription.client_key = :clientKey)\n" +
            "   AND prescription.created_date BETWEEN :startDate AND :endDate\n" +
            "   AND prescription.deleted = 0\n" +
            "   AND attendance.status != 0\n" +
            "   AND drug_prescription.deleted = 0\n", nativeQuery = true)
    List<Object[]> countTotalPrescriptionsByClientKeyAllUniHealthPlan(@Param("clientKey") String clientKey,
                                                      @Param("startDate") String startDate,
                                                      @Param("endDate") String endDate);

    @Query(value = "SELECT\n" +
            "   COUNT(DISTINCT prescription.id) AS total_prescriptions,\n" +
            "   COUNT(DISTINCT em.id) AS totalPrescribingDoctors,\n" +
            "   COUNT(DISTINCT drug_prescription.id) AS totalPrescriptionItems,\n" +
            "   COUNT(DISTINCT drug.id) AS TotalNonStandardizedItems,\n" +
            "   SUM(drug.pmc_20) AS sumPMC20\n" +
            "FROM subscription\n" +
            "INNER JOIN attendance ON subscription.id = attendance.id_subscription\n" +
            "INNER JOIN prescription ON prescription.id_attendance = attendance.id\n" +
            "INNER JOIN drug_prescription ON drug_prescription.id_prescription = prescription.id\n" +
            "INNER JOIN drug ON drug_prescription.id_drug = drug.id\n" +
            "INNER JOIN employee em ON attendance.id_employee = em.id\n" +
            "WHERE\n" +
            "   (:clientKey is null or subscription.client_key = :clientKey)\n" +
            "   AND prescription.created_date BETWEEN :startDate AND :endDate\n" +
            "   AND attendance.id_term_cbo IN (:specialties)\n" +
            "   AND prescription.deleted = 0\n" +
            "   AND attendance.status != 0\n" +
            "   AND drug_prescription.deleted = 0\n", nativeQuery = true)
    List<Object[]> countTotalPrescriptionsBySpecialties(@Param("clientKey") String clientKey,
                                                      @Param("startDate") String startDate,
                                                      @Param("endDate") String endDate,
                                                      @Param("specialties") List<Long> specialties);

    @Query(value = "SELECT\n" +
            "   COUNT(DISTINCT prescription.id) AS total_prescriptions, " +
            "   COUNT(DISTINCT em.id) AS totalPrescribingDoctors, " +
            "   COUNT(DISTINCT drug_prescription.id) AS totalPrescriptionItems, " +
            "   COUNT(DISTINCT drug.id) AS TotalNonStandardizedItems, " +
            "   SUM(drug.pmc_20) AS sumPMC20 " +
            "FROM subscription " +
            "INNER JOIN attendance ON subscription.id = attendance.id_subscription " +
            "INNER JOIN prescription ON prescription.id_attendance = attendance.id " +
            "INNER JOIN drug_prescription ON drug_prescription.id_prescription = prescription.id " +
            "INNER JOIN drug ON drug_prescription.id_drug = drug.id " +
            "INNER JOIN employee em ON attendance.id_employee = em.id " +
            "INNER JOIN insurance AS insu ON insu.id = attendance.insurance_id " +
            "INNER JOIN health_plan AS hp ON hp.id = insu.health_plan_id " +
            "WHERE " +
            "   (:clientKey is null or subscription.client_key = :clientKey) " +
            "   AND prescription.created_date BETWEEN :startDate AND :endDate " +
            "   AND insu.health_plan_id IN (:insurances) " +
            "   AND prescription.deleted = 0 " +
            "   AND attendance.status != 0 " +
            "   AND drug_prescription.deleted = 0 ", nativeQuery = true)
    List<Object[]> countTotalPrescriptionsByInsurances(@Param("clientKey") String clientKey,
                                                        @Param("startDate") String startDate,
                                                        @Param("endDate") String endDate,
                                                        @Param("insurances") List<Long> insurances);

    @Query(value = "SELECT " +
            "   COUNT(DISTINCT prescription.id) AS total_prescriptions, " +
            "   COUNT(DISTINCT em.id) AS totalPrescribingDoctors, " +
            "   COUNT(DISTINCT drug_prescription.id) AS totalPrescriptionItems, " +
            "   COUNT(DISTINCT drug.id) AS TotalNonStandardizedItems, " +
            "   SUM(drug.pmc_20) AS sumPMC20 " +
            "FROM subscription " +
            "INNER JOIN attendance ON subscription.id = attendance.id_subscription " +
            "INNER JOIN prescription ON prescription.id_attendance = attendance.id " +
            "INNER JOIN drug_prescription ON drug_prescription.id_prescription = prescription.id " +
            "INNER JOIN drug ON drug_prescription.id_drug = drug.id " +
            "INNER JOIN employee em ON attendance.id_employee = em.id " +
            "INNER JOIN insurance AS insu ON insu.id = attendance.insurance_id " +
            "INNER JOIN health_plan AS hp ON hp.id = insu.health_plan_id " +
            "INNER JOIN healthplan_interchange AS hpi ON hpi.ans_health_plan_origin = hp.ans_code " +
            "WHERE " +
            "   (:clientKey is null or subscription.client_key = :clientKey) " +
            "   AND prescription.created_date BETWEEN :startDate AND :endDate " +
            "   AND insu.health_plan_id IN (:insurances) " +
            "   AND prescription.deleted = 0 " +
            "   AND attendance.status != 0 " +
            "   AND drug_prescription.deleted = 0 ", nativeQuery = true)
    List<Object[]> countTotalPrescriptionsByInsurancesAllUniHealthPlan(@Param("clientKey") String clientKey,
                                                       @Param("startDate") String startDate,
                                                       @Param("endDate") String endDate,
                                                       @Param("insurances") List<Long> insurances);

    @Query(value = "SELECT\n" +
            "   COUNT(DISTINCT prescription.id) AS total_prescriptions, " +
            "   COUNT(DISTINCT em.id) AS totalPrescribingDoctors, " +
            "   COUNT(DISTINCT drug_prescription.id) AS totalPrescriptionItems, " +
            "   COUNT(DISTINCT drug.id) AS TotalNonStandardizedItems, " +
            "   SUM(drug.pmc_20) AS sumPMC20 " +
            "FROM subscription " +
            "INNER JOIN attendance ON subscription.id = attendance.id_subscription " +
            "INNER JOIN prescription ON prescription.id_attendance = attendance.id " +
            "INNER JOIN drug_prescription ON drug_prescription.id_prescription = prescription.id " +
            "INNER JOIN drug ON drug_prescription.id_drug = drug.id " +
            "INNER JOIN employee em ON attendance.id_employee = em.id " +
            "INNER JOIN insurance AS insu ON insu.id = attendance.insurance_id " +
            "INNER JOIN health_plan AS hp ON hp.id = insu.health_plan_id " +
            "WHERE " +
            "   (:clientKey is null or subscription.client_key = :clientKey) " +
            "   AND prescription.created_date BETWEEN :startDate AND :endDate " +
            "   AND insu.health_plan_id IN (:insurances) " +
            "   AND attendance.id_term_cbo IN (:specialties) " +
            "   AND prescription.deleted = 0 " +
            "   AND attendance.status != 0 " +
            "   AND drug_prescription.deleted = 0 ", nativeQuery = true)
    List<Object[]> countTotalPrescriptionsByInsurancesAndSpecialties(@Param("clientKey") String clientKey,
                                                       @Param("startDate") String startDate,
                                                       @Param("endDate") String endDate,
                                                       @Param("insurances") List<Long> insurances,
                                                       @Param("specialties") List<Long> specialties);

    @Query(value = "SELECT\n" +
            "   COUNT(DISTINCT prescription.id) AS total_prescriptions, " +
            "   COUNT(DISTINCT em.id) AS totalPrescribingDoctors, " +
            "   COUNT(DISTINCT drug_prescription.id) AS totalPrescriptionItems, " +
            "   COUNT(DISTINCT drug.id) AS TotalNonStandardizedItems, " +
            "   SUM(drug.pmc_20) AS sumPMC20 " +
            "FROM subscription " +
            "INNER JOIN attendance ON subscription.id = attendance.id_subscription " +
            "INNER JOIN prescription ON prescription.id_attendance = attendance.id " +
            "INNER JOIN drug_prescription ON drug_prescription.id_prescription = prescription.id " +
            "INNER JOIN drug ON drug_prescription.id_drug = drug.id " +
            "INNER JOIN employee em ON attendance.id_employee = em.id " +
            "INNER JOIN insurance AS insu ON insu.id = attendance.insurance_id " +
            "INNER JOIN health_plan AS hp ON hp.id = insu.health_plan_id " +
            "INNER JOIN healthplan_interchange AS hpi ON hpi.ans_health_plan_origin = hp.ans_code " +
            "WHERE " +
            "   (:clientKey is null or subscription.client_key = :clientKey) " +
            "   AND prescription.created_date BETWEEN :startDate AND :endDate " +
            "   AND insu.health_plan_id IN (:insurances) " +
            "   AND attendance.id_term_cbo IN (:specialties) " +
            "   AND prescription.deleted = 0 " +
            "   AND attendance.status != 0 " +
            "   AND drug_prescription.deleted = 0 ", nativeQuery = true)
    List<Object[]> countTotalPrescriptionsByInsurancesAndSpecialtiesAllUniHealthPlan(@Param("clientKey") String clientKey,
                                                                     @Param("startDate") String startDate,
                                                                     @Param("endDate") String endDate,
                                                                     @Param("insurances") List<Long> insurances,
                                                                     @Param("specialties") List<Long> specialties);

    @Query(value = "SELECT\n" +
            "   COUNT(DISTINCT prescription.id) AS total_prescriptions, " +
            "   COUNT(DISTINCT em.id) AS totalPrescribingDoctors, " +
            "   COUNT(DISTINCT drug_prescription.id) AS totalPrescriptionItems, " +
            "   COUNT(DISTINCT drug.id) AS TotalNonStandardizedItems, " +
            "   SUM(drug.pmc_20) AS sumPMC20 " +
            "FROM subscription " +
            "INNER JOIN attendance ON subscription.id = attendance.id_subscription " +
            "INNER JOIN prescription ON prescription.id_attendance = attendance.id " +
            "INNER JOIN drug_prescription ON drug_prescription.id_prescription = prescription.id " +
            "INNER JOIN drug ON drug_prescription.id_drug = drug.id " +
            "INNER JOIN employee em ON attendance.id_employee = em.id " +
            "INNER JOIN insurance AS insu ON insu.id = attendance.insurance_id " +
            "INNER JOIN health_plan AS hp ON hp.id = insu.health_plan_id " +
            "INNER JOIN healthplan_interchange AS hpi ON hpi.ans_health_plan_origin = hp.ans_code " +
            "WHERE " +
            "   (:clientKey is null or subscription.client_key = :clientKey) " +
            "   AND prescription.created_date BETWEEN :startDate AND :endDate " +
            "   AND attendance.id_term_cbo IN (:specialties) " +
            "   AND prescription.deleted = 0 " +
            "   AND attendance.status != 0 " +
            "   AND drug_prescription.deleted = 0 ", nativeQuery = true)
    List<Object[]> countTotalPrescriptionsByAndSpecialtiesAllUniHealthPlan(@Param("clientKey") String clientKey,
                                                                     @Param("startDate") String startDate,
                                                                     @Param("endDate") String endDate,
                                                                     @Param("specialties") List<Long> specialties);

    @Query(value = "SELECT " +
            "YEAR(p.created_date) AS year, " +
            "MONTH(p.created_date) AS month, " +
            "COUNT(DISTINCT p.id) AS totalPrescriptions, " +
            "COUNT(DISTINCT em.id) AS totalPrescribingDoctors, " +
            "COUNT(DISTINCT dp.id) AS totalPrescriptionItems, " +
            "COUNT(DISTINCT dp.id_drug) AS TotalNonStandardizedItems, " +
            "IFNULL(SUM(d.pmc_20), 0) AS sumPMC20 " +
            "FROM " +
            "subscription s " +
            "INNER JOIN attendance a ON s.id = a.id_subscription " +
            "INNER JOIN prescription p ON p.id_attendance = a.id " +
            "INNER JOIN drug_prescription dp ON dp.id_prescription = p.id " +
            "INNER JOIN drug d ON dp.id_drug = d.id " +
            "INNER JOIN employee em ON a.id_employee = em.id " +
            "INNER JOIN insurance i ON a.insurance_id = i.id " +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id " +
            "INNER JOIN term_cbo tc ON a.id_term_cbo = tc.id " +
            "INNER JOIN patient pa ON a.id_patient = pa.id " +
            "WHERE (:clientKey is null or s.client_key = :clientKey) " +
            "AND p.created_date BETWEEN :startDate AND :endDate " +
            "AND i.health_plan_id IN (:insurances) " +
            "AND a.id_term_cbo IN (:specialties) " +
            "GROUP BY YEAR(p.created_date), MONTH(p.created_date) ", nativeQuery = true)
            //"ORDER BY year DESC, month DESC\n", nativeQuery = true)
    List<Object[]> countMonthlyPrescriptionIndicatorsAndInsuranceAndSpecialties(@Param("clientKey") String clientKey,
                                                      @Param("startDate") String startDate,
                                                      @Param("endDate") String endDate,
                                                      @Param("insurances") List<Long> insurances,
                                                      @Param("specialties") List<Long> specialties);

    @Query(value = "SELECT " +
            "YEAR(p.created_date) AS year, " +
            "MONTH(p.created_date) AS month, " +
            "COUNT(DISTINCT p.id) AS totalPrescriptions, " +
            "COUNT(DISTINCT em.id) AS totalPrescribingDoctors, " +
            "COUNT(DISTINCT dp.id) AS totalPrescriptionItems, " +
            "COUNT(DISTINCT dp.id_drug) AS TotalNonStandardizedItems, " +
            "IFNULL(SUM(d.pmc_20), 0) AS sumPMC20 " +
            "FROM " +
            "subscription s " +
            "INNER JOIN attendance a ON s.id = a.id_subscription " +
            "INNER JOIN prescription p ON p.id_attendance = a.id " +
            "INNER JOIN drug_prescription dp ON dp.id_prescription = p.id " +
            "INNER JOIN drug d ON dp.id_drug = d.id " +
            "INNER JOIN employee em ON a.id_employee = em.id " +
            "INNER JOIN insurance i ON a.insurance_id = i.id " +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id " +
            "INNER JOIN healthplan_interchange AS hpi ON hpi.ans_health_plan_origin = hp.ans_code " +
            "INNER JOIN term_cbo tc ON a.id_term_cbo = tc.id " +
            "INNER JOIN patient pa ON a.id_patient = pa.id " +
            "WHERE (:clientKey is null or s.client_key = :clientKey) " +
            "AND p.created_date BETWEEN :startDate AND :endDate " +
            "AND i.health_plan_id IN (:insurances) " +
            "AND a.id_term_cbo IN (:specialties) " +
            "GROUP BY YEAR(p.created_date), MONTH(p.created_date) ", nativeQuery = true)
        //"ORDER BY year DESC, month DESC\n", nativeQuery = true)
    List<Object[]> countMonthlyPrescriptionIndicatorsAndInsuranceAndSpecialtiesAllUniHealthPlan(@Param("clientKey") String clientKey,
                                                                                @Param("startDate") String startDate,
                                                                                @Param("endDate") String endDate,
                                                                                @Param("insurances") List<Long> insurances,
                                                                                @Param("specialties") List<Long> specialties);

    @Query(value = "SELECT " +
            "YEAR(p.created_date) AS year, " +
            "MONTH(p.created_date) AS month, " +
            "COUNT(DISTINCT p.id) AS totalPrescriptions, " +
            "COUNT(DISTINCT em.id) AS totalPrescribingDoctors, " +
            "COUNT(DISTINCT dp.id) AS totalPrescriptionItems, " +
            "COUNT(DISTINCT dp.id_drug) AS TotalNonStandardizedItems, " +
            "IFNULL(SUM(d.pmc_20), 0) AS sumPMC20 " +
            "FROM " +
            "subscription s " +
            "INNER JOIN attendance a ON s.id = a.id_subscription " +
            "INNER JOIN prescription p ON p.id_attendance = a.id " +
            "INNER JOIN drug_prescription dp ON dp.id_prescription = p.id " +
            "INNER JOIN drug d ON dp.id_drug = d.id " +
            "INNER JOIN employee em ON a.id_employee = em.id " +
            "INNER JOIN term_cbo tc ON a.id_term_cbo = tc.id " +
            "INNER JOIN patient pa ON a.id_patient = pa.id " +
            "WHERE (:clientKey is null or s.client_key = :clientKey) " +
            "AND p.created_date BETWEEN :startDate AND :endDate " +
            "GROUP BY YEAR(p.created_date), MONTH(p.created_date) ", nativeQuery = true)
            //"ORDER BY year DESC, month DESC\n", nativeQuery = true)
    List<Object[]> countMonthlyPrescriptionIndicators(@Param("clientKey") String clientKey,
                                                      @Param("startDate") String startDate,
                                                      @Param("endDate") String endDate);

    @Query(value = "SELECT " +
            "YEAR(p.created_date) AS year, " +
            "MONTH(p.created_date) AS month, " +
            "COUNT(DISTINCT p.id) AS totalPrescriptions, " +
            "COUNT(DISTINCT em.id) AS totalPrescribingDoctors, " +
            "COUNT(DISTINCT dp.id) AS totalPrescriptionItems, " +
            "COUNT(DISTINCT dp.id_drug) AS TotalNonStandardizedItems, " +
            "IFNULL(SUM(d.pmc_20), 0) AS sumPMC20 " +
            "FROM " +
            "subscription s " +
            "INNER JOIN attendance a ON s.id = a.id_subscription " +
            "INNER JOIN prescription p ON p.id_attendance = a.id " +
            "INNER JOIN drug_prescription dp ON dp.id_prescription = p.id " +
            "INNER JOIN drug d ON dp.id_drug = d.id " +
            "INNER JOIN employee em ON a.id_employee = em.id " +
            "INNER JOIN insurance i ON a.insurance_id = i.id " +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id " +
            "INNER JOIN healthplan_interchange AS hpi ON hpi.ans_health_plan_origin = hp.ans_code " +
            "INNER JOIN term_cbo tc ON a.id_term_cbo = tc.id " +
            "INNER JOIN patient pa ON a.id_patient = pa.id " +
            "WHERE (:clientKey is null or s.client_key = :clientKey) " +
            "AND p.created_date BETWEEN :startDate AND :endDate " +
            "GROUP BY YEAR(p.created_date), MONTH(p.created_date) ", nativeQuery = true)
        //"ORDER BY year DESC, month DESC\n", nativeQuery = true)
    List<Object[]> countMonthlyPrescriptionIndicatorsAllUniHealthPlan(@Param("clientKey") String clientKey,
                                                      @Param("startDate") String startDate,
                                                      @Param("endDate") String endDate);

    @Query(value = "SELECT " +
            "YEAR(p.created_date) AS year, " +
            "MONTH(p.created_date) AS month, " +
            "COUNT(DISTINCT p.id) AS totalPrescriptions, " +
            "COUNT(DISTINCT em.id) AS totalPrescribingDoctors, " +
            "COUNT(DISTINCT dp.id) AS totalPrescriptionItems, " +
            "COUNT(DISTINCT dp.id_drug) AS TotalNonStandardizedItems, " +
            "IFNULL(SUM(d.pmc_20), 0) AS sumPMC20 " +
            "FROM " +
            "subscription s " +
            "INNER JOIN attendance a ON s.id = a.id_subscription " +
            "INNER JOIN prescription p ON p.id_attendance = a.id " +
            "INNER JOIN drug_prescription dp ON dp.id_prescription = p.id " +
            "INNER JOIN drug d ON dp.id_drug = d.id " +
            "INNER JOIN employee em ON a.id_employee = em.id " +
            "INNER JOIN term_cbo tc ON a.id_term_cbo = tc.id " +
            "INNER JOIN patient pa ON a.id_patient = pa.id " +
            "WHERE (:clientKey is null or s.client_key = :clientKey) " +
            "AND p.created_date BETWEEN :startDate AND :endDate " +
            "AND a.id_term_cbo IN (:specialties) " +
            "GROUP BY YEAR(p.created_date), MONTH(p.created_date) ", nativeQuery = true)
            //"ORDER BY year DESC, month DESC\n", nativeQuery = true)
    List<Object[]> countMonthlyPrescriptionIndicatorsAndSpecialties(@Param("clientKey") String clientKey,
                                                      @Param("startDate") String startDate,
                                                      @Param("endDate") String endDate,
                                                      @Param("specialties") List<Long> specialties);

    @Query(value = "SELECT " +
            "YEAR(p.created_date) AS year, " +
            "MONTH(p.created_date) AS month, " +
            "COUNT(DISTINCT p.id) AS totalPrescriptions, " +
            "COUNT(DISTINCT em.id) AS totalPrescribingDoctors, " +
            "COUNT(DISTINCT dp.id) AS totalPrescriptionItems, " +
            "COUNT(DISTINCT dp.id_drug) AS TotalNonStandardizedItems, " +
            "IFNULL(SUM(d.pmc_20), 0) AS sumPMC20 " +
            "FROM " +
            "subscription s " +
            "INNER JOIN attendance a ON s.id = a.id_subscription " +
            "INNER JOIN prescription p ON p.id_attendance = a.id " +
            "INNER JOIN drug_prescription dp ON dp.id_prescription = p.id " +
            "INNER JOIN drug d ON dp.id_drug = d.id " +
            "INNER JOIN employee em ON a.id_employee = em.id " +
            "INNER JOIN insurance i ON a.insurance_id = i.id " +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id " +
            "INNER JOIN healthplan_interchange AS hpi ON hpi.ans_health_plan_origin = hp.ans_code " +
            "INNER JOIN term_cbo tc ON a.id_term_cbo = tc.id " +
            "INNER JOIN patient pa ON a.id_patient = pa.id " +
            "WHERE (:clientKey is null or s.client_key = :clientKey) " +
            "AND p.created_date BETWEEN :startDate AND :endDate " +
            "AND a.id_term_cbo IN (:specialties) " +
            "GROUP BY YEAR(p.created_date), MONTH(p.created_date) ", nativeQuery = true)
    List<Object[]> countMonthlyPrescriptionIndicatorsAndSpecialtiesAllUniHealthPlan(@Param("clientKey") String clientKey,
                                                                    @Param("startDate") String startDate,
                                                                    @Param("endDate") String endDate,
                                                                    @Param("specialties") List<Long> specialties);

    @Query(value = "SELECT " +
            "YEAR(p.created_date) AS year, " +
            "MONTH(p.created_date) AS month, " +
            "COUNT(DISTINCT p.id) AS totalPrescriptions, " +
            "COUNT(DISTINCT em.id) AS totalPrescribingDoctors, " +
            "COUNT(DISTINCT dp.id) AS totalPrescriptionItems,\n" +
            "COUNT(DISTINCT dp.id_drug) AS TotalNonStandardizedItems,\n" +
            "IFNULL(SUM(d.pmc_20), 0) AS sumPMC20\n" +
            "FROM\n" +
            "subscription s\n" +
            "INNER JOIN attendance a ON s.id = a.id_subscription\n" +
            "INNER JOIN prescription p ON p.id_attendance = a.id\n" +
            "INNER JOIN drug_prescription dp ON dp.id_prescription = p.id\n" +
            "INNER JOIN drug d ON dp.id_drug = d.id\n" +
            "INNER JOIN employee em ON a.id_employee = em.id\n" +
            "INNER JOIN insurance i ON a.insurance_id = i.id\n" +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id\n" +
            "INNER JOIN term_cbo tc ON a.id_term_cbo = tc.id\n" +
            "INNER JOIN patient pa ON a.id_patient = pa.id\n" +
            "WHERE (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "AND i.health_plan_id IN (:insurances)\n" +
            "GROUP BY YEAR(p.created_date), MONTH(p.created_date)\n", nativeQuery = true)
            //"ORDER BY year DESC, month DESC\n", nativeQuery = true)
    List<Object[]> countMonthlyPrescriptionIndicatorsAndInsurance(@Param("clientKey") String clientKey,
                                                                  @Param("startDate") String startDate,
                                                                  @Param("endDate") String endDate,
                                                                  @Param("insurances") List<Long> insurances);

    @Query(value = "SELECT " +
            "YEAR(p.created_date) AS year, " +
            "MONTH(p.created_date) AS month, " +
            "COUNT(DISTINCT p.id) AS totalPrescriptions, " +
            "COUNT(DISTINCT em.id) AS totalPrescribingDoctors, " +
            "COUNT(DISTINCT dp.id) AS totalPrescriptionItems,\n" +
            "COUNT(DISTINCT dp.id_drug) AS TotalNonStandardizedItems,\n" +
            "IFNULL(SUM(d.pmc_20), 0) AS sumPMC20\n" +
            "FROM\n" +
            "subscription s\n" +
            "INNER JOIN attendance a ON s.id = a.id_subscription\n" +
            "INNER JOIN prescription p ON p.id_attendance = a.id\n" +
            "INNER JOIN drug_prescription dp ON dp.id_prescription = p.id\n" +
            "INNER JOIN drug d ON dp.id_drug = d.id\n" +
            "INNER JOIN employee em ON a.id_employee = em.id\n" +
            "INNER JOIN insurance i ON a.insurance_id = i.id\n" +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id\n" +
            "INNER JOIN healthplan_interchange AS hpi ON hpi.ans_health_plan_origin = hp.ans_code " +
            "INNER JOIN term_cbo tc ON a.id_term_cbo = tc.id\n" +
            "INNER JOIN patient pa ON a.id_patient = pa.id\n" +
            "WHERE (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "AND i.health_plan_id IN (:insurances)\n" +
            "GROUP BY YEAR(p.created_date), MONTH(p.created_date)\n", nativeQuery = true)
        //"ORDER BY year DESC, month DESC\n", nativeQuery = true)
    List<Object[]> countMonthlyPrescriptionIndicatorsAndInsuranceAllUniHealthPlan(@Param("clientKey") String clientKey,
                                                                  @Param("startDate") String startDate,
                                                                  @Param("endDate") String endDate,
                                                                  @Param("insurances") List<Long> insurances);
    @Query(value = "SELECT\n" +
            "YEAR(dp.created_date) AS year,\n" +
            "MONTH(dp.created_date) AS month,\n" +
            "d.product AS drugName,\n" +
            "d.presentation,\n" +
            "COUNT(*) AS total_drug_prescription,\n" +
            "COALESCE(SUM(d.pmc_20), 0) AS totalMedicationCost\n" +
            "FROM drug_prescription AS dp\n" +
            "INNER JOIN drug AS d ON dp.id_drug = d.id\n" +
            "INNER JOIN prescription AS p ON dp.id_prescription = p.id\n" +
            "INNER JOIN attendance AS a ON p.id_attendance = a.id\n" +
            "INNER JOIN subscription AS s ON a.id_subscription = s.id\n" +
            "INNER JOIN insurance i ON a.insurance_id = i.id\n" +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id\n" +
            "WHERE (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "AND i.health_plan_id IN (:insurances)\n" +
            "AND a.id_term_cbo IN (:specialties)\n" +
            "GROUP BY YEAR(dp.created_date), MONTH(dp.created_date), d.product, d.presentation\n" +
            "ORDER BY YEAR(dp.created_date) desc, MONTH(dp.created_date) desc", nativeQuery = true)
    List<Object[]> countTotalMedicationsByMonthAndYearAndInsuranceAndSpecialties(@Param("clientKey") String clientKey,
                                                                                 @Param("startDate") String startDate,
                                                                                 @Param("endDate") String endDate,
                                                                                 @Param("insurances") List<Long> insurances,
                                                                                 @Param("specialties") List<Long> specialties);
    @Query(value = "SELECT\n" +
            "YEAR(dp.created_date) AS year,\n" +
            "MONTH(dp.created_date) AS month,\n" +
            "d.product AS drugName,\n" +
            "d.presentation,\n" +
            "COUNT(*) AS total_drug_prescription,\n" +
            "COALESCE(SUM(d.pmc_20), 0) AS totalMedicationCost\n" +
            "FROM drug_prescription AS dp\n" +
            "INNER JOIN drug AS d ON dp.id_drug = d.id\n" +
            "INNER JOIN prescription AS p ON dp.id_prescription = p.id\n" +
            "INNER JOIN attendance AS a ON p.id_attendance = a.id\n" +
            "INNER JOIN subscription AS s ON a.id_subscription = s.id\n" +
            "INNER JOIN insurance i ON a.insurance_id = i.id\n" +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id\n" +
            "INNER JOIN healthplan_interchange AS hpi ON hpi.ans_health_plan_origin = hp.ans_code " +
            "WHERE (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "AND i.health_plan_id IN (:insurances)\n" +
            "AND a.id_term_cbo IN (:specialties)\n" +
            "GROUP BY YEAR(dp.created_date), MONTH(dp.created_date), d.product, d.presentation\n" +
            "ORDER BY YEAR(dp.created_date) desc, MONTH(dp.created_date) desc", nativeQuery = true)
    List<Object[]> countTotalMedicationsByMonthAndYearAndInsuranceAndSpecialtiesAllUniHealthPlan(@Param("clientKey") String clientKey,
                                                                                 @Param("startDate") String startDate,
                                                                                 @Param("endDate") String endDate,
                                                                                 @Param("insurances") List<Long> insurances,
                                                                                 @Param("specialties") List<Long> specialties);

    @Query(value = "SELECT\n" +
            "YEAR(dp.created_date) AS year,\n" +
            "MONTH(dp.created_date) AS month,\n" +
            "d.product AS drugName,\n" +
            "d.presentation,\n" +
            "COUNT(*) AS total_drug_prescription,\n" +
            "COALESCE(SUM(d.pmc_20), 0) AS totalMedicationCost\n" +
            "FROM drug_prescription AS dp\n" +
            "INNER JOIN drug AS d ON dp.id_drug = d.id\n" +
            "INNER JOIN prescription AS p ON dp.id_prescription = p.id\n" +
            "INNER JOIN attendance AS a ON p.id_attendance = a.id\n" +
            "INNER JOIN subscription AS s ON a.id_subscription = s.id\n" +
            "WHERE (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "AND a.id_term_cbo IN (:specialties)\n" +
            "GROUP BY YEAR(dp.created_date), MONTH(dp.created_date), d.product, d.presentation\n" +
            "ORDER BY YEAR(dp.created_date) desc, MONTH(dp.created_date) desc", nativeQuery = true)
    List<Object[]> countTotalMedicationsByMonthAndYearAndSpecialties(@Param("clientKey") String clientKey,
                                                                     @Param("startDate") String startDate,
                                                                     @Param("endDate") String endDate,
                                                                     @Param("specialties") List<Long> specialties);

    @Query(value = "SELECT\n" +
            "YEAR(dp.created_date) AS year,\n" +
            "MONTH(dp.created_date) AS month,\n" +
            "d.product AS drugName,\n" +
            "d.presentation,\n" +
            "COUNT(*) AS total_drug_prescription,\n" +
            "COALESCE(SUM(d.pmc_20), 0) AS totalMedicationCost\n" +
            "FROM drug_prescription AS dp\n" +
            "INNER JOIN drug AS d ON dp.id_drug = d.id\n" +
            "INNER JOIN prescription AS p ON dp.id_prescription = p.id\n" +
            "INNER JOIN attendance AS a ON p.id_attendance = a.id\n" +
            "INNER JOIN subscription AS s ON a.id_subscription = s.id\n" +
            "INNER JOIN insurance i ON a.insurance_id = i.id\n" +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id\n" +
            "INNER JOIN healthplan_interchange AS hpi ON hpi.ans_health_plan_origin = hp.ans_code " +
            "WHERE (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "AND a.id_term_cbo IN (:specialties)\n" +
            "GROUP BY YEAR(dp.created_date), MONTH(dp.created_date), d.product, d.presentation\n" +
            "ORDER BY YEAR(dp.created_date) desc, MONTH(dp.created_date) desc", nativeQuery = true)
    List<Object[]> countTotalMedicationsByMonthAndYearAndSpecialtiesAllUniHealthPlan(@Param("clientKey") String clientKey,
                                                                     @Param("startDate") String startDate,
                                                                     @Param("endDate") String endDate,
                                                                     @Param("specialties") List<Long> specialties);

    @Query(value = "SELECT\n" +
            "YEAR(dp.created_date) AS year,\n" +
            "MONTH(dp.created_date) AS month,\n" +
            "d.product AS drugName,\n" +
            "d.presentation,\n" +
            "COUNT(*) AS total_drug_prescription,\n" +
            "COALESCE(SUM(d.pmc_20), 0) AS totalMedicationCost\n" +
            "FROM drug_prescription AS dp\n" +
            "INNER JOIN drug AS d ON dp.id_drug = d.id\n" +
            "INNER JOIN prescription AS p ON dp.id_prescription = p.id\n" +
            "INNER JOIN attendance AS a ON p.id_attendance = a.id\n" +
            "INNER JOIN subscription AS s ON a.id_subscription = s.id\n" +
            "INNER JOIN insurance i ON a.insurance_id = i.id\n" +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id\n" +
            "WHERE (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "AND i.health_plan_id IN (:insurances)\n" +
            "GROUP BY YEAR(dp.created_date), MONTH(dp.created_date), d.product, d.presentation\n" +
            "ORDER BY YEAR(dp.created_date) desc, MONTH(dp.created_date) desc", nativeQuery = true)
    List<Object[]> countTotalMedicationsByMonthAndYearAndInsurance(@Param("clientKey") String clientKey,
                                                                   @Param("startDate") String startDate,
                                                                   @Param("endDate") String endDate,
                                                                   @Param("insurances") List<Long> insurances);
    @Query(value = "SELECT\n" +
            "YEAR(dp.created_date) AS year,\n" +
            "MONTH(dp.created_date) AS month,\n" +
            "d.product AS drugName,\n" +
            "d.presentation,\n" +
            "COUNT(*) AS total_drug_prescription,\n" +
            "COALESCE(SUM(d.pmc_20), 0) AS totalMedicationCost\n" +
            "FROM drug_prescription AS dp\n" +
            "INNER JOIN drug AS d ON dp.id_drug = d.id\n" +
            "INNER JOIN prescription AS p ON dp.id_prescription = p.id\n" +
            "INNER JOIN attendance AS a ON p.id_attendance = a.id\n" +
            "INNER JOIN subscription AS s ON a.id_subscription = s.id\n" +
            "INNER JOIN insurance i ON a.insurance_id = i.id\n" +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id\n" +
            "WHERE (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "AND i.health_plan_id IN (:insurances)\n" +
            "GROUP BY YEAR(dp.created_date), MONTH(dp.created_date), d.product, d.presentation\n" +
            "ORDER BY YEAR(dp.created_date) desc, MONTH(dp.created_date) desc", nativeQuery = true)
    List<Object[]> countTotalMedicationsByMonthAndYearAndInsuranceAllUniHealthPlan(@Param("clientKey") String clientKey,
                                                                   @Param("startDate") String startDate,
                                                                   @Param("endDate") String endDate,
                                                                   @Param("insurances") List<Long> insurances);

    @Query(value = "SELECT\n" +
            "YEAR(dp.created_date) AS year,\n" +
            "MONTH(dp.created_date) AS month,\n" +
            "d.product AS drugName,\n" +
            "d.presentation,\n" +
            "COUNT(*) AS total_drug_prescription,\n" +
            "COALESCE(SUM(d.pmc_20), 0) AS totalMedicationCost\n" +
            "FROM drug_prescription AS dp\n" +
            "INNER JOIN drug AS d ON dp.id_drug = d.id\n" +
            "INNER JOIN prescription AS p ON dp.id_prescription = p.id\n" +
            "INNER JOIN attendance AS a ON p.id_attendance = a.id\n" +
            "INNER JOIN subscription AS s ON a.id_subscription = s.id\n" +
            "WHERE (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "GROUP BY YEAR(dp.created_date), MONTH(dp.created_date), d.product, d.presentation\n" +
            "ORDER BY YEAR(dp.created_date) desc, MONTH(dp.created_date) desc", nativeQuery = true)
    List<Object[]> countTotalMedicationsByMonthAndYear(@Param("clientKey") String clientKey,
                                                       @Param("startDate") String startDate,
                                                       @Param("endDate") String endDate);
    @Query(value = "SELECT\n" +
            "YEAR(dp.created_date) AS year,\n" +
            "MONTH(dp.created_date) AS month,\n" +
            "d.product AS drugName,\n" +
            "d.presentation,\n" +
            "COUNT(*) AS total_drug_prescription,\n" +
            "COALESCE(SUM(d.pmc_20), 0) AS totalMedicationCost\n" +
            "FROM drug_prescription AS dp\n" +
            "INNER JOIN drug AS d ON dp.id_drug = d.id\n" +
            "INNER JOIN prescription AS p ON dp.id_prescription = p.id\n" +
            "INNER JOIN attendance AS a ON p.id_attendance = a.id\n" +
            "INNER JOIN subscription AS s ON a.id_subscription = s.id\n" +
            "INNER JOIN insurance i ON a.insurance_id = i.id " +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id " +
            "INNER JOIN healthplan_interchange AS hpi ON hpi.ans_health_plan_origin = hp.ans_code " +
            "WHERE (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "GROUP BY YEAR(dp.created_date), MONTH(dp.created_date), d.product, d.presentation\n" +
            "ORDER BY YEAR(dp.created_date) desc, MONTH(dp.created_date) desc", nativeQuery = true)
    List<Object[]> countTotalMedicationsByMonthAndYearAllUniHealthPlan(@Param("clientKey") String clientKey,
                                                       @Param("startDate") String startDate,
                                                       @Param("endDate") String endDate);

    @Query(value = "SELECT s.client_key AS clientKey,\n" +
            "       COUNT(DISTINCT p.id) AS totalPrescriptions,\n" +
            "       COUNT(DISTINCT em.id) AS totalPrescribingDoctors,\n" +
            "       COUNT(DISTINCT dp.id) AS totalPrescriptionItems,\n" +
            "       COUNT(DISTINCT dp.id_drug) AS TotalNonStandardizedItems,\n" +
            "       SUM(d.pmc_20) AS sumPMC20\n" +
            "FROM subscription s \n" +
            "INNER JOIN attendance a ON s.id = a.id_subscription\n" +
            "INNER JOIN prescription p ON p.id_attendance = a.id\n" +
            "INNER JOIN drug_prescription dp ON dp.id_prescription = p.id\n" +
            "INNER JOIN employee em ON a.id_employee = em.id\n" +
            "INNER JOIN patient pa ON a.id_patient = pa.id\n" +
            "INNER JOIN drug d ON dp.id_drug = d.id\n" +
            "WHERE p.deleted = 0 AND a.status != 0 AND dp.deleted = 0\n" +
            "AND (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "GROUP BY s.client_key\n" +
            "ORDER BY s.client_key;\n", nativeQuery = true)
    List<Object[]> countTotalIndicatorsByClientKeyMv(@Param("clientKey") String clientKey,
                                                     @Param("startDate") String startDate,
                                                     @Param("endDate") String endDate);
    @Query(value = "SELECT s.client_key AS clientKey,\n" +
            "       COUNT(DISTINCT p.id) AS totalPrescriptions,\n" +
            "       COUNT(DISTINCT em.id) AS totalPrescribingDoctors,\n" +
            "       COUNT(DISTINCT dp.id) AS totalPrescriptionItems,\n" +
            "       COUNT(DISTINCT dp.id_drug) AS TotalNonStandardizedItems,\n" +
            "       SUM(d.pmc_20) AS sumPMC20\n" +
            "FROM subscription s \n" +
            "INNER JOIN attendance a ON s.id = a.id_subscription\n" +
            "INNER JOIN prescription p ON p.id_attendance = a.id\n" +
            "INNER JOIN drug_prescription dp ON dp.id_prescription = p.id\n" +
            "INNER JOIN employee em ON a.id_employee = em.id\n" +
            "INNER JOIN patient pa ON a.id_patient = pa.id\n" +
            "INNER JOIN drug d ON dp.id_drug = d.id\n" +
            "INNER JOIN insurance i ON a.insurance_id = i.id " +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id " +
            "INNER JOIN healthplan_interchange AS hpi ON hpi.ans_health_plan_origin = hp.ans_code " +
            "WHERE p.deleted = 0 AND a.status != 0 AND dp.deleted = 0\n" +
            "AND (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "GROUP BY s.client_key\n" +
            "ORDER BY s.client_key;\n", nativeQuery = true)
    List<Object[]> countTotalIndicatorsByClientKeyMvAllUniHealthPlan(@Param("clientKey") String clientKey,
                                                     @Param("startDate") String startDate,
                                                     @Param("endDate") String endDate);

    @Query(value = "SELECT s.client_key AS clientKey,\n" +
            "       COUNT(DISTINCT p.id) AS totalPrescriptions,\n" +
            "       COUNT(DISTINCT em.id) AS totalPrescribingDoctors,\n" +
            "       COUNT(DISTINCT dp.id) AS totalPrescriptionItems,\n" +
            "       COUNT(DISTINCT dp.id_drug) AS TotalNonStandardizedItems,\n" +
            "       SUM(d.pmc_20) AS sumPMC20\n" +
            "FROM subscription s \n" +
            "INNER JOIN attendance a ON s.id = a.id_subscription\n" +
            "INNER JOIN prescription p ON p.id_attendance = a.id\n" +
            "INNER JOIN drug_prescription dp ON dp.id_prescription = p.id\n" +
            "INNER JOIN employee em ON a.id_employee = em.id\n" +
            "INNER JOIN patient pa ON a.id_patient = pa.id\n" +
            "INNER JOIN drug d ON dp.id_drug = d.id\n" +
            "WHERE p.deleted = 0 AND a.status != 0 AND dp.deleted = 0\n" +
            "AND (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "AND a.id_term_cbo IN (:specialties)\n" +
            "GROUP BY s.client_key\n" +
            "ORDER BY s.client_key;\n", nativeQuery = true)
    List<Object[]> countTotalIndicatorsByClientKeyMvAndSpecialties(@Param("clientKey") String clientKey,
                                                     @Param("startDate") String startDate,
                                                     @Param("endDate") String endDate,
                                                     @Param("specialties") List<Long> specialties);
    @Query(value = "SELECT s.client_key AS clientKey,\n" +
            "       COUNT(DISTINCT p.id) AS totalPrescriptions,\n" +
            "       COUNT(DISTINCT em.id) AS totalPrescribingDoctors,\n" +
            "       COUNT(DISTINCT dp.id) AS totalPrescriptionItems,\n" +
            "       COUNT(DISTINCT dp.id_drug) AS TotalNonStandardizedItems,\n" +
            "       SUM(d.pmc_20) AS sumPMC20\n" +
            "FROM subscription s \n" +
            "INNER JOIN attendance a ON s.id = a.id_subscription\n" +
            "INNER JOIN prescription p ON p.id_attendance = a.id\n" +
            "INNER JOIN drug_prescription dp ON dp.id_prescription = p.id\n" +
            "INNER JOIN employee em ON a.id_employee = em.id\n" +
            "INNER JOIN patient pa ON a.id_patient = pa.id\n" +
            "INNER JOIN drug d ON dp.id_drug = d.id\n" +
            "INNER JOIN insurance i ON a.insurance_id = i.id " +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id " +
            "INNER JOIN healthplan_interchange AS hpi ON hpi.ans_health_plan_origin = hp.ans_code " +
            "WHERE p.deleted = 0 AND a.status != 0 AND dp.deleted = 0\n" +
            "AND (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "AND a.id_term_cbo IN (:specialties)\n" +
            "GROUP BY s.client_key\n" +
            "ORDER BY s.client_key;\n", nativeQuery = true)
    List<Object[]> countTotalIndicatorsByClientKeyMvAndSpecialtiesAllUniHealthPlan(@Param("clientKey") String clientKey,
                                                                   @Param("startDate") String startDate,
                                                                   @Param("endDate") String endDate,
                                                                   @Param("specialties") List<Long> specialties);

    @Query(value = "SELECT s.client_key AS clientKey,\n" +
            "       COUNT(DISTINCT p.id) AS totalPrescriptions,\n" +
            "       COUNT(DISTINCT em.id) AS totalPrescribingDoctors,\n" +
            "       COUNT(DISTINCT dp.id) AS totalPrescriptionItems,\n" +
            "       COUNT(DISTINCT dp.id_drug) AS TotalNonStandardizedItems,\n" +
            "       SUM(d.pmc_20) AS sumPMC20\n" +
            "FROM subscription s \n" +
            "INNER JOIN attendance a ON s.id = a.id_subscription\n" +
            "INNER JOIN prescription p ON p.id_attendance = a.id\n" +
            "INNER JOIN drug_prescription dp ON dp.id_prescription = p.id\n" +
            "INNER JOIN employee em ON a.id_employee = em.id\n" +
            "INNER JOIN patient pa ON a.id_patient = pa.id\n" +
            "INNER JOIN drug d ON dp.id_drug = d.id\n" +
            "INNER JOIN insurance i ON a.insurance_id = i.id\n" +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id\n" +
            "WHERE p.deleted = 0 AND a.status != 0 AND dp.deleted = 0\n" +
            "AND (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "AND i.health_plan_id IN (:insurances)\n" +
            "GROUP BY s.client_key\n" +
            "ORDER BY s.client_key;\n", nativeQuery = true)
    List<Object[]> countTotalIndicatorsByClientKeyMvAndInsurance(@Param("clientKey") String clientKey,
                                                                   @Param("startDate") String startDate,
                                                                   @Param("endDate") String endDate,
                                                                   @Param("insurances") List<Long> insurances);
    @Query(value = "SELECT s.client_key AS clientKey,\n" +
            "       COUNT(DISTINCT p.id) AS totalPrescriptions,\n" +
            "       COUNT(DISTINCT em.id) AS totalPrescribingDoctors,\n" +
            "       COUNT(DISTINCT dp.id) AS totalPrescriptionItems,\n" +
            "       COUNT(DISTINCT dp.id_drug) AS TotalNonStandardizedItems,\n" +
            "       SUM(d.pmc_20) AS sumPMC20\n" +
            "FROM subscription s \n" +
            "INNER JOIN attendance a ON s.id = a.id_subscription\n" +
            "INNER JOIN prescription p ON p.id_attendance = a.id\n" +
            "INNER JOIN drug_prescription dp ON dp.id_prescription = p.id\n" +
            "INNER JOIN employee em ON a.id_employee = em.id\n" +
            "INNER JOIN patient pa ON a.id_patient = pa.id\n" +
            "INNER JOIN drug d ON dp.id_drug = d.id\n" +
            "INNER JOIN insurance i ON a.insurance_id = i.id\n" +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id\n" +
            "INNER JOIN healthplan_interchange AS hpi ON hpi.ans_health_plan_origin = hp.ans_code " +
            "WHERE p.deleted = 0 AND a.status != 0 AND dp.deleted = 0\n" +
            "AND (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "AND i.health_plan_id IN (:insurances)\n" +
            "GROUP BY s.client_key\n" +
            "ORDER BY s.client_key;\n", nativeQuery = true)
    List<Object[]> countTotalIndicatorsByClientKeyMvAndInsuranceAllUniHealthPlan(@Param("clientKey") String clientKey,
                                                                 @Param("startDate") String startDate,
                                                                 @Param("endDate") String endDate,
                                                                 @Param("insurances") List<Long> insurances);

    @Query(value = "SELECT s.client_key AS clientKey,\n" +
            "       COUNT(DISTINCT p.id) AS totalPrescriptions,\n" +
            "       COUNT(DISTINCT em.id) AS totalPrescribingDoctors,\n" +
            "       COUNT(DISTINCT dp.id) AS totalPrescriptionItems,\n" +
            "       COUNT(DISTINCT dp.id_drug) AS TotalNonStandardizedItems,\n" +
            "       SUM(d.pmc_20) AS sumPMC20\n" +
            "FROM subscription s \n" +
            "INNER JOIN attendance a ON s.id = a.id_subscription\n" +
            "INNER JOIN prescription p ON p.id_attendance = a.id\n" +
            "INNER JOIN drug_prescription dp ON dp.id_prescription = p.id\n" +
            "INNER JOIN employee em ON a.id_employee = em.id\n" +
            "INNER JOIN patient pa ON a.id_patient = pa.id\n" +
            "INNER JOIN drug d ON dp.id_drug = d.id\n" +
            "INNER JOIN insurance i ON a.insurance_id = i.id\n" +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id\n" +
            "WHERE p.deleted = 0 AND a.status != 0 AND dp.deleted = 0\n" +
            "AND (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "AND i.health_plan_id IN (:insurances)\n" +
            "AND a.id_term_cbo IN (:specialties)\n" +
            "GROUP BY s.client_key\n" +
            "ORDER BY s.client_key;\n", nativeQuery = true)
    List<Object[]> countTotalIndicatorsByClientKeyMvAndInsuranceAndSpecialties(@Param("clientKey") String clientKey,
                                                                 @Param("startDate") String startDate,
                                                                 @Param("endDate") String endDate,
                                                                 @Param("insurances") List<Long> insurances,
                                                                 @Param("specialties") List<Long> specialties);
    @Query(value = "SELECT s.client_key AS clientKey,\n" +
            "       COUNT(DISTINCT p.id) AS totalPrescriptions,\n" +
            "       COUNT(DISTINCT em.id) AS totalPrescribingDoctors,\n" +
            "       COUNT(DISTINCT dp.id) AS totalPrescriptionItems,\n" +
            "       COUNT(DISTINCT dp.id_drug) AS TotalNonStandardizedItems,\n" +
            "       SUM(d.pmc_20) AS sumPMC20\n" +
            "FROM subscription s \n" +
            "INNER JOIN attendance a ON s.id = a.id_subscription\n" +
            "INNER JOIN prescription p ON p.id_attendance = a.id\n" +
            "INNER JOIN drug_prescription dp ON dp.id_prescription = p.id\n" +
            "INNER JOIN employee em ON a.id_employee = em.id\n" +
            "INNER JOIN patient pa ON a.id_patient = pa.id\n" +
            "INNER JOIN drug d ON dp.id_drug = d.id\n" +
            "INNER JOIN insurance i ON a.insurance_id = i.id\n" +
            "INNER JOIN health_plan hp ON hp.id = i.health_plan_id\n" +
            "INNER JOIN healthplan_interchange AS hpi ON hpi.ans_health_plan_origin = hp.ans_code " +
            "WHERE p.deleted = 0 AND a.status != 0 AND dp.deleted = 0\n" +
            "AND (:clientKey is null or s.client_key = :clientKey)\n" +
            "AND p.created_date BETWEEN :startDate AND :endDate\n" +
            "AND i.health_plan_id IN (:insurances)\n" +
            "AND a.id_term_cbo IN (:specialties)\n" +
            "GROUP BY s.client_key\n" +
            "ORDER BY s.client_key;\n", nativeQuery = true)
    List<Object[]> countTotalIndicatorsByClientKeyMvAndInsuranceAndSpecialtiesAllUniHealthPlan(@Param("clientKey") String clientKey,
                                                                               @Param("startDate") String startDate,
                                                                               @Param("endDate") String endDate,
                                                                               @Param("insurances") List<Long> insurances,
                                                                               @Param("specialties") List<Long> specialties);

}