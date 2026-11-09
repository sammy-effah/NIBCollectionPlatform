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
import com.nib.gh.donations.model.DonationDetailsObj;
import com.nib.gh.donations.model.Institution;
import com.nib.gh.donations.repository.InstitutionRepository;


@Service
public class InstitutionService {

      static Random rad = new Random();
    Logger logger = LogManager.getLogger(DonationDetailsService.class);

    @Autowired
    private InstitutionRepository institutionRepository;

    public List<Institution> findAll() {
        return institutionRepository.findAll();
    }

    public String saveDonationDetails(Institution institution) {
        String respMsg;

        try {

            if (institution.getName() != null) {
                institution.setDate(Timestamp.valueOf(LocalDateTime.now()));
                institutionRepository.save(institution);

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

    public String updateInstitution(DonationDetailsObj donationDetailsObj) {
        Optional<Institution> institionOpt = institutionRepository.findById(donationDetailsObj.getId());
        if (institionOpt.isPresent()) {
            Institution donationDetails = institionOpt.get();
            donationDetails.setName(donationDetailsObj.getNameD());
            donationDetails.setLogoLocation(donationDetailsObj.getPoster());

            institutionRepository.save(donationDetails);
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

    public Institution findInstitutionById(long id) {
        Institution institution = institutionRepository.findById(id).get();

        return institution;
    }

    public List<Institution> findNotMoreThanFourDays() {
        Duration duration = Duration.ofDays(4); // Example duration
        Timestamp timestamp = Timestamp.from(Instant.now().minus(duration));
        return institutionRepository.findLatestInstitutionForSelect(timestamp);
    }

    public List<Institution> findAllByEnabled() {
        return institutionRepository.findAllByEnabled(true);
    }

    public Optional<Institution> findById(long id) {
        return institutionRepository.findById(id);
    }

    public boolean deleteById(long id) {
        try {

            Optional<Institution> donateOptional = institutionRepository.findById(id);
            // logger.info("USER DATA TO DELETE:: " + userOptional);
            if (donateOptional.isPresent()) {
                Institution donateeDetails = donateOptional.get();

                institutionRepository
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

            Optional<Institution> donateOptional = institutionRepository.findById(id);
            logger.info("DETAILS DATA TO ENABLE:: " + donateOptional);
            if (donateOptional.isPresent()) {
                Institution donationDetails = donateOptional.get();

                institutionRepository
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

            Optional<Institution> donateOptional = institutionRepository.findById(id);
            logger.info("DETAILS DATA TO ENABLE:: " + donateOptional);
            if (donateOptional.isPresent()) {
                Institution donationDetails = donateOptional.get();

                institutionRepository
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
