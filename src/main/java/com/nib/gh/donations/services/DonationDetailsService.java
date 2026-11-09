package com.nib.gh.donations.services;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nib.gh.donations.model.DonationDetails;
import com.nib.gh.donations.model.DonationDetailsObj;
import com.nib.gh.donations.repository.DonationDetailsRepository;

@Service
public class DonationDetailsService {
    static Random rad = new Random();
    Logger logger = LogManager.getLogger(DonationDetailsService.class);

    @Autowired
    private DonationDetailsRepository donationDetailsRepository;

    public List<DonationDetails> findAll() {
        return donationDetailsRepository.findAll();
    }

    public String saveDonationDetails(DonationDetails donationDetails) {
        String respMsg;

        try {

            if (donationDetails.getName() != null) {
                donationDetails.setDate(Timestamp.valueOf(LocalDateTime.now()));
                donationDetailsRepository.save(donationDetails);

                respMsg = "00#Successful";
            } else {
                respMsg = "06#Failed, Not aded";
            }
            // Save the image file to a desired location on the server

        } catch (Exception e) {

            respMsg = "06#Failed, Not aded";
            System.out.println("Sorry");
            e.printStackTrace();
        }

        return respMsg;
    }

    public String updateDonationDetails(DonationDetailsObj donationDetailsObj) {
        Optional<DonationDetails> donationDetailsOpt = donationDetailsRepository.findById(donationDetailsObj.getId());
        if (donationDetailsOpt.isPresent()) {
            DonationDetails donationDetails = donationDetailsOpt.get();
            donationDetails.setName(donationDetailsObj.getNameD());
            donationDetails.setPicLocation(donationDetailsObj.getPoster());
            donationDetails.setPurpose(donationDetailsObj.getPurpose());

            donationDetailsRepository.save(donationDetails);
            return "00#Successful";
        } else {
            return "06#Failed, user not present";
        }
    }

    public static String getRef(int size) {
        String value = "";
        for (int t = 0; t < size; t++) {
            value = value + rad.nextInt(9);
        }
        return "NIBFC" + value;
    }

    public DonationDetails findDetailsById(long id) {
        DonationDetails donationDetails = donationDetailsRepository.findById(id).get();

        return donationDetails;
    }

    public List<DonationDetails> findNotMoreThanFourDays() {
        Duration duration = Duration.ofDays(4); // Example duration
        Timestamp timestamp = Timestamp.from(Instant.now().minus(duration));
        return donationDetailsRepository.findLatestDonationDetailsForSelect(timestamp);
    }

    public List<DonationDetails> findAllByEnabled() {
        return donationDetailsRepository.findAllByEnabled(true);
    }

    public Optional<DonationDetails> findById(long id) {
        return donationDetailsRepository.findById(id);
    }

    public boolean deleteById(long id) {
        try {

            Optional<DonationDetails> donateOptional = donationDetailsRepository.findById(id);
            // logger.info("USER DATA TO DELETE:: " + userOptional);
            if (donateOptional.isPresent()) {
                DonationDetails donateeDetails = donateOptional.get();

                donationDetailsRepository
                        .deleteById(donateeDetails.getId());

                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }

    public boolean enableById(long id) {
        try {

            Optional<DonationDetails> donateOptional = donationDetailsRepository.findById(id);
            logger.info("DETAILS DATA TO ENABLE:: " + donateOptional);
            if (donateOptional.isPresent()) {
                DonationDetails donationDetails = donateOptional.get();

                donationDetailsRepository
                        .updateDataById(donationDetails.getId(), true);

                return true;
            } else {
                logger.info("DETAILS WAS NOT FOUND");
                return false;
            }

        } catch (Exception e) {
            logger.error("Exception occurred... " + e.getMessage());
            return false;
        }

    }

    public boolean disableById(long id) {
        try {

            Optional<DonationDetails> donateOptional = donationDetailsRepository.findById(id);
            logger.info("DETAILS DATA TO ENABLE:: " + donateOptional);
            if (donateOptional.isPresent()) {
                DonationDetails donationDetails = donateOptional.get();

                donationDetailsRepository
                        .updateDataById(donationDetails.getId(), false);

                return true;
            } else {
                logger.info("DETAILS WAS NOT FOUND");
                return false;
            }

        } catch (Exception e) {
            logger.error("Exception occurred... " + e.getMessage());
            return false;
        }

    }
}
