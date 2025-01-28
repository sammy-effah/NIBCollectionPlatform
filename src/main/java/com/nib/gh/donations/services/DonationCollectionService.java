package com.nib.gh.donations.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.net.URLEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.nib.gh.donations.model.CollectionDTO;
import com.nib.gh.donations.model.DonationCollection;
import com.nib.gh.donations.model.DonationDetails;
import com.nib.gh.donations.repository.DonationCollectionRepository;
import com.nib.gh.donations.repository.DonationDetailsRepository;
import com.nib.gh.donations.utility.DoRequest;
import java.util.stream.Collectors;
import jakarta.transaction.Transactional;

@Service
public class DonationCollectionService {
    static Random rad = new Random();
    Logger logger = LogManager.getLogger(DonationCollectionService.class);

    @Autowired
    private DonationCollectionRepository donationCollectionRepository;

    @Autowired
    DonationDetailsRepository donationDetailsRepository;

    @Value("${SMS.URL}")
    private String smsURL;

    public List<DonationCollection> findAll() {

        List<DonationCollection> donationCollectionsList = donationCollectionRepository.findAll();

        // for (DonationCollection donationCollection : donationCollectionsList) {
        //     donationCollection.getDonationDetails().isEnabled();
        // }

        List<DonationCollection> filteredList = donationCollectionsList.stream()
                .filter(dc -> hasEnabledDetails(dc))
                .collect(Collectors.toList());
                
        return filteredList;
    }

    public List<DonationCollection> getAllDonationCollectionsWithEnabledDetails(String user) {
        List<DonationCollection> donationCollectionsList = donationCollectionRepository.findAllByWhoIsEntering(user);

        // Filter donationCollectionsList to include only those with enabled details
        List<DonationCollection> filteredList = donationCollectionsList.stream()
                .filter(dc -> hasEnabledDetails(dc))
                .collect(Collectors.toList());

        return filteredList;
    }

    private boolean hasEnabledDetails(DonationCollection donationCollection) {
        DonationDetails donationDetail = donationCollection.getDonationDetails();

        // Check if there is at least one enabled detail

        if (donationDetail.isEnabled()) {
            return true; // Found an enabled detail, return true
        }

        return false; // No enabled details found
    }

    public List<DonationCollection> findAllByUser(String user) {
        return donationCollectionRepository.findAllByWhoIsEntering(user);
    }

    public Optional<DonationCollection> findByReceipt(String ticketNumber) {
        return donationCollectionRepository.findAllByReceiptNum(ticketNumber);
    }

    public boolean removeByReceipt(String ticketNumber) {
        try {
            DonationCollection donationCollection;
            Optional<DonationCollection> optionalFuneralCollection = donationCollectionRepository
                    .findAllByReceiptNum(ticketNumber);
            if (optionalFuneralCollection.isPresent()) {
                donationCollection = optionalFuneralCollection.get();

                donationCollection.setDonationDetails(null);

                donationCollectionRepository
                        .deleteById(donationCollection.getId());
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }

    public String saveCollectionData(CollectionDTO collectionDTO) {
        String respMsg;

        try {

            if (isValidCollectionDTO(collectionDTO)) {

                DonationCollection collectionData = new DonationCollection();

                Optional<DonationDetails> optionalFuneralDetails = donationDetailsRepository
                        .findById(collectionDTO.getWhoseDonation());

                if (optionalFuneralDetails.isPresent()) {
                    DonationDetails donationDetails = optionalFuneralDetails.get();
                    collectionData.setContributorName(collectionDTO.getContributorName());
                    collectionData.setPhone(collectionDTO.getPhone());
                    collectionData.setDescription(collectionDTO.getDescription());
                    collectionData.setDateData(collectionDTO.getDateData());
                    collectionData.setBeneficiary(collectionDTO.getBeneficiary());
                    collectionData.setAmount(collectionDTO.getAmount());
                    collectionData.setWhoIsEntering(collectionDTO.getWhoIsEntering());
                    collectionData.setDonationDetails(donationDetails);
                    collectionData.setItemsAndQuantity(collectionDTO.getItemsAndQuantity());
                    // collectionData.setReceiptNum(getRef(8));
                    collectionData.setReceiptNum(collectionDTO.getReceiptNum());
                    collectionData.setCurrency(collectionDTO.getCurrency());
                    collectionData.setCheque(collectionDTO.getCheque());

                    donationCollectionRepository.save(collectionData);
                    String fundsDonated = Double.compare(collectionDTO.getAmount(), 0.0) == 0 ? ""
                            : collectionDTO.getCurrency()
                                    + collectionDTO.getAmount();
                    String otherItemsAdded = collectionDTO.getItemsAndQuantity() != null
                            ? (!fundsDonated.isEmpty() ? " and " + collectionDTO.getItemsAndQuantity()
                                    : "" + collectionDTO.getItemsAndQuantity())
                            : "";
                    // sending sms
                    String textMsg = URLEncoder.encode("Dear " + collectionDTO.getContributorName()
                            + ", thank you for donating " + fundsDonated + otherItemsAdded + "  towards the "
                            + donationDetails.getPurpose() + " of "
                            + donationDetails.getName(), "UTF-8");
                    logger.info("TEXT MESSAGE:: " + textMsg);
                    String urlForSMS = String.format(smsURL.replace("#MSG_ID#", getMsgId(8))
                            .replace("#PHONE#", "233" + collectionDTO.getPhone().substring(1))
                            .replace("#TEXT#", "%s"), textMsg);
                    logger.info("SMS URL SENT: " + urlForSMS);
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
            logger.error("Sorry, Funeral Collection not saved!");
            e.printStackTrace();
        }

        return respMsg;
    }

    @Transactional
    public String updateCollectionData(CollectionDTO collectionDTO) {
        String respMsg;

        try {
            Optional<DonationCollection> existingFuneralCollection = donationCollectionRepository
                    .findAllByReceiptNum(collectionDTO.getReceiptNum());

            if (isValidCollectionDTO(collectionDTO) && existingFuneralCollection.isPresent()) {

                DonationCollection collectionData = existingFuneralCollection.get();

                collectionData.setContributorName(collectionDTO.getContributorName());
                collectionData.setPhone(collectionDTO.getPhone());
                collectionData.setDescription(collectionDTO.getDescription());
                collectionData.setBeneficiary(collectionDTO.getBeneficiary());
                collectionData.setAmount(collectionDTO.getAmount());
                collectionData.setItemsAndQuantity(collectionDTO.getItemsAndQuantity());
                collectionData.setReceiptNum(collectionDTO.getReceiptNum());
                collectionData.setCurrency(collectionDTO.getCurrency());
                collectionData.setCheque(collectionDTO.getCheque());

                donationCollectionRepository.save(collectionData);

                respMsg = "00#Successful";

            } else {
                respMsg = "06#Failed, Invalid request";
            }

        } catch (Exception e) {

            respMsg = "06#Failed, Not aded";
            System.out.println("Sorry, Funeral Collection not saved!");
            e.printStackTrace();
        }

        return respMsg;
    }

    public static String getRef(int size) {
        String value = "";
        for (int t = 0; t < size; t++) {
            value = value + rad.nextInt(9);
        }
        return "NIBFC" + value;
    }

    public static String getMsgId(int size) {
        String value = "";
        for (int t = 0; t < size; t++) {
            value = value + rad.nextInt(9);
        }
        return value;
    }

    public double sumCollectionsByCurrency(String currency) {

        double sum = 0;

        try {

            sum = donationCollectionRepository.sumAmountByCurrency(currency) != null
                    ? Double.parseDouble(donationCollectionRepository.sumAmountByCurrency(currency))
                    : 0.0;

        } catch (Exception e) {
            logger.error("Exception occurred " + e.getMessage());
        }

        return sum;
    }

    public double sumCollectionsByCurrencyAndDetailsId(String currency) {

        double sum = 0;

        try {
            sum = donationCollectionRepository.sumAmountByCurrencyAndDetails(currency) != null
                    ? Double.parseDouble(
                            donationCollectionRepository.sumAmountByCurrencyAndDetails(currency))
                    : 0.0;

        } catch (Exception e) {
            logger.error("Exception occurred " + e.getMessage());
        }

        return sum;
    }

    public double sumCollectionsInGHSForWhoseEntering(String whoseEntering) {

        double sum = 0;

        try {

            sum = donationCollectionRepository.sumAmountInGHSForWhoseEntering(whoseEntering, "GHS") != null
                    ? Double.parseDouble(
                            donationCollectionRepository.sumAmountInGHSForWhoseEntering(whoseEntering, "GHS"))
                    : 0.0;

        } catch (Exception e) {
            logger.error("Exception occurred " + e.getMessage());
        }

        return sum;
    }

    public String countCollections() {
        long totalNoOfCollections = 0;

        try {
            totalNoOfCollections = donationCollectionRepository.count();
            // logger.info("NO OF USERS:: " + totalNoOfCollections);
        } catch (Exception e) {
            logger.error("Exception has occurred, " + e.getMessage());
        }
        logger.info(Math.round(totalNoOfCollections));

        return String.valueOf(totalNoOfCollections);
    }

    private boolean isValidCollectionDTO(CollectionDTO collectionDTO) {
        return collectionDTO.getContributorName() != null &&
                collectionDTO.getPhone() != null;
    }
}
