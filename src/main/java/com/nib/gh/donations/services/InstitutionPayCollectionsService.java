package com.nib.gh.donations.services;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.nib.gh.donations.model.Institution;
import com.nib.gh.donations.model.InstitutionPayCollections;
import com.nib.gh.donations.repository.InstitutionPayCollectionsRepository;
import com.nib.gh.donations.repository.InstitutionRepository;
import com.nib.gh.donations.utility.DoRequest;
import jakarta.transaction.Transactional;

@Service
public class InstitutionPayCollectionsService {

    @Autowired
    private InstitutionPayCollectionsRepository institutionPayCollectionsRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    static Random rad = new Random();

    @Value("${SMS.URL}")
    private String smsURL;

    public List<InstitutionPayCollections> findAllByInstitution(Long id) {
        List<InstitutionPayCollections> institutionPayCollectionsList = null;
        Institution institution = institutionRepository.findById(id).orElse(null);
        if (institution != null) {
            institutionPayCollectionsList = institutionPayCollectionsRepository
                    .findAllByInstitution(institution);
        }

        @SuppressWarnings("null")
        List<InstitutionPayCollections> filteredList = institutionPayCollectionsList.stream()
                .filter(dc -> hasEnabledInstitution(dc))
                .collect(Collectors.toList());

        return filteredList;
    }

    public List<InstitutionPayCollections> findAllByInstitutionAndCreateDate(Long id) {
        List<InstitutionPayCollections> institutionPayCollectionsList = null;
        Institution institution = institutionRepository.findById(id).orElse(null);
        if (institution != null) {

            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

            institutionPayCollectionsList = institutionPayCollectionsRepository.findTodayCollections(institution,
                    startOfDay, endOfDay);
        }

        @SuppressWarnings("null")
        List<InstitutionPayCollections> filteredList = institutionPayCollectionsList.stream()
                .filter(dc -> hasEnabledInstitution(dc))
                .collect(Collectors.toList());

        return filteredList;
    }

    public List<InstitutionPayCollections> getAllInstitutionWithEnabledDetails(String user, Long id) {
        List<InstitutionPayCollections> institutionPayCollectionsList = null;
        Institution institution = institutionRepository.findById(id).orElse(null);

        if (institution != null) {
            institutionPayCollectionsList = institutionPayCollectionsRepository
                    .findAllByEnteredByAndInstitution(user, institution);
        }
        // List<DonationCollection> donationCollectionsList =
        // donationCollectionRepository
        // .findAllByWhoIsEnteringAndDonationDetails(user, id);

        // Filter donationCollectionsList to include only those with enabled details
        @SuppressWarnings("null")
        List<InstitutionPayCollections> filteredList = institutionPayCollectionsList.stream()
                .filter(dc -> hasEnabledInstitution(dc))
                .collect(Collectors.toList());

        return filteredList;
    }

    private boolean hasEnabledInstitution(InstitutionPayCollections institutionPayCollections) {
        Institution institution = institutionPayCollections.getInstitution();

        // Check if there is at least one enabled detail

        if (institution.isEnabled()) {
            return true; // Found an enabled detail, return true
        }

        return false; // No enabled details found
    }

    public List<InstitutionPayCollections> findAllByUser(String user, Long id) {

        List<InstitutionPayCollections> institutionPayCollectionsList = null;
        Institution institution = institutionRepository.findById(id).orElse(null);

        if (institution != null) {
            institutionPayCollectionsList = institutionPayCollectionsRepository
                    .findAllByEnteredByAndInstitution(user, institution);
        }
        return institutionPayCollectionsList;
    }

    public List<InstitutionPayCollections> findAllByUserForToday(String user, Long id) {

        List<InstitutionPayCollections> institutionPayCollectionsList = null;
        Institution institution = institutionRepository.findById(id).orElse(null);

        if (institution != null) {

            LocalDate today = LocalDate.now();
            LocalDateTime start = today.atStartOfDay();
            LocalDateTime end = today.plusDays(1).atStartOfDay();

            institutionPayCollectionsList = institutionPayCollectionsRepository.findAllForUserForToday(user,
                    institution, start, end);
        }
        return institutionPayCollectionsList;
    }

    public Optional<InstitutionPayCollections> findByVoucher(String voucherNumber) {
        return institutionPayCollectionsRepository.findAllByVoucherNumber(voucherNumber);
    }

    public boolean removeByVoucher(String voucherNumber) {
        try {
            InstitutionPayCollections institutionPayCollections;
            Optional<InstitutionPayCollections> optionalInstitutionPayCollections = institutionPayCollectionsRepository
                    .findAllByVoucherNumber(voucherNumber);
            if (optionalInstitutionPayCollections.isPresent()) {
                institutionPayCollections = optionalInstitutionPayCollections.get();

                institutionPayCollections.setInstitution(null);

                institutionPayCollectionsRepository
                        .deleteById(institutionPayCollections.getId());
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }

    public String saveInstitutionPaymentData(InstitutionPayCollections institutionPayCollections) {
        String respMsg;

        try {

            if (isValidPaymentData(institutionPayCollections)) {

                InstitutionPayCollections collectionData = new InstitutionPayCollections();

                Optional<Institution> optionalInstitution = institutionRepository
                        .findById(institutionPayCollections.getInstitution().getId());

                if (optionalInstitution.isPresent()) {
                    Institution institution = optionalInstitution.get();
                    collectionData.setName(institutionPayCollections.getName());
                    ;
                    collectionData.setPhoneNumber(institutionPayCollections.getPhoneNumber());
                    collectionData.setCreateDate(institutionPayCollections.getCreateDate());
                    collectionData.setEnteredBy(institutionPayCollections.getEnteredBy());
                    collectionData.setAmount(institutionPayCollections.getAmount());
                    collectionData.setInstitution(institution);
                    collectionData.setLevel(institutionPayCollections.getLevel());
                    collectionData.setIndexNumber(institutionPayCollections.getIndexNumber());
                    collectionData.setPurpose(institutionPayCollections.getPurpose());
                    collectionData.setVoucherNumber(institutionPayCollections.getVoucherNumber());

                    institutionPayCollectionsRepository.save(collectionData);

                    String fundsDonated = Double.compare(institutionPayCollections.getAmount(), 0.0) == 0 ? ""
                            : "GHS"
                                    + institutionPayCollections.getAmount();

                    // sending sms
                    String textMsg = URLEncoder.encode("Dear " + institutionPayCollections.getName()
                            + ", your payment of " + fundsDonated + "  for "
                            + institutionPayCollections.getPurpose() + " to "
                            + institution.getName() + " has being received. Payment voucher is "
                            + institutionPayCollections.getVoucherNumber(), "UTF-8");

                    String urlForSMS = String.format(smsURL.replace("#MSG_ID#", getMsgId(8))
                            .replace("#PHONE#", "233" + institutionPayCollections.getPhoneNumber().substring(1))
                            .replace("#TEXT#", "%s"), textMsg);

                    new DoRequest().sendGet(urlForSMS);

                    respMsg = "00#Successful";
                } else {
                    respMsg = "06#Failed, Funeral details not found.";
                }
            } else {
                respMsg = "06#Failed, Invalid request";
            }

        } catch (Exception e) {

            respMsg = "06#Failed, Not aded";
            e.printStackTrace();
        }

        return respMsg;
    }

    public double sumCollectionsByInstitutionId(Long institutionId) {
        try {

            LocalDate today = LocalDate.now();
            LocalDateTime start = today.atStartOfDay();
            LocalDateTime end = today.plusDays(1).atStartOfDay();

            BigDecimal sum = institutionPayCollectionsRepository.sumAmountByInstitutionToday(institutionId, start, end);
            // BigDecimal sum =
            // institutionPayCollectionsRepository.sumAmountByInstitution(institutionId);

            return sum != null ? sum.doubleValue() : 0.0;
        } catch (Exception e) {

            return 0.0;
        }
    }

    @Transactional
    public String updateCollectionData(InstitutionPayCollections collectionDTO) {
        String respMsg;

        try {
            // Validate voucher number first
            if (collectionDTO.getVoucherNumber() == null || collectionDTO.getVoucherNumber().trim().isEmpty()) {
                return "06#Failed, Voucher number missing";
            }

            Optional<InstitutionPayCollections> existingPaymentCollection = institutionPayCollectionsRepository
                    .findAllByVoucherNumber(collectionDTO.getVoucherNumber());

            // If no record exists
            if (existingPaymentCollection.isEmpty()) {
                return "06#Failed, Voucher not found";
            }

            InstitutionPayCollections collectionData = existingPaymentCollection.get();

            if (!isValidPaymentData(collectionDTO)) {
                return "06#Failed, Invalid request";
            }

            // Map fields
            collectionData.setName(collectionDTO.getName());
            collectionData.setPhoneNumber(collectionDTO.getPhoneNumber());
            collectionData.setIndexNumber(collectionDTO.getIndexNumber());
            collectionData.setAmount(collectionDTO.getAmount());
            collectionData.setLevel(collectionDTO.getLevel());

            // Keep original fields
            // VoucherNumber, CreateDate, EnteredBy, Institution remain unchanged

            institutionPayCollectionsRepository.save(collectionData);

            respMsg = "00#Successful";

        } catch (Exception e) {
            respMsg = "06#Failed, Not added";
            System.out.println("Sorry, Payment Collection not saved!");
            e.printStackTrace();
        }

        return respMsg;
    }

    public static String getRef(int size) {
        String value = "";
        for (int t = 0; t < size; t++) {
            value = value + rad.nextInt(9);
        }
        return "NIBSDA" + value;
    }

    public static String getMsgId(int size) {
        String value = "";
        for (int t = 0; t < size; t++) {
            value = value + rad.nextInt(9);
        }
        return value;
    }

    public double sumCollectionsByInstitution(Long institutionId) {
        try {
            BigDecimal sum = institutionPayCollectionsRepository.sumAmountByInstitution(institutionId);
            return sum != null ? sum.doubleValue() : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public Double sumCollectionsInGHSForWhoseEntering(String whoseEntering, Long id) {

        Double sum = null;

        try {

            LocalDate today = LocalDate.now();
            LocalDateTime start = today.atStartOfDay();
            LocalDateTime end = today.plusDays(1).atStartOfDay();

            sum = Optional.ofNullable(
                    institutionPayCollectionsRepository.sumAmountInForEnteredByToday(
                            whoseEntering, id, start, end))
                    .orElse(0.0);

        } catch (Exception e) {
            e.getStackTrace();
        }

        return sum;
    }

    public String countCollections(Long id) {
        long totalNoOfCollections = 0;

        try {

            LocalDate today = LocalDate.now();
            LocalDateTime start = today.atStartOfDay();
            LocalDateTime end = today.plusDays(1).atStartOfDay();

            totalNoOfCollections = institutionPayCollectionsRepository.countToday(id, start, end);

            // totalNoOfCollections = institutionPayCollectionsRepository.count(id);
            // logger.info("NO OF USERS:: " + totalNoOfCollections);
        } catch (Exception e) {
            e.getStackTrace();
        }

        return String.valueOf(totalNoOfCollections);
    }

    private boolean isValidPaymentData(InstitutionPayCollections collectionDTO) {
        return collectionDTO.getName() != null &&
                collectionDTO.getPhoneNumber() != null;
    }

}
