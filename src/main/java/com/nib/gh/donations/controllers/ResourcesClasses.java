package com.nib.gh.donations.controllers;

// import com.nib.gh.donations.esb.T_24_FundsTransfer;
// import com.nib.gh.donations.esbservice.T24FundsTransferService;
import com.nib.gh.donations.model.*;
import com.nib.gh.donations.services.DonationCollectionService;
import com.nib.gh.donations.services.DonationDetailsService;
import com.nib.gh.donations.services.InstitutionPayCollectionsService;
import com.nib.gh.donations.services.InstitutionService;
import com.nib.gh.donations.services.RoleService;
import com.nib.gh.donations.services.UserService;
import com.nib.gh.donations.utility.DoRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Base64;

@RestController
@RequestMapping("/nib/api")
public class ResourcesClasses {

    Logger logger = LogManager.getLogger(ResourcesClasses.class);

    @Autowired
    private DonationCollectionService donationCollectionService;

    @Value("${upload.url}")
    private String uploadURL;

    @Value("${upload.location}")
    private String uploadLocation;

    @Autowired
    private UserService userService;

    @Autowired
    private DonationDetailsService donationDetailsService;

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    private InstitutionPayCollectionsService institutionPayCollectionsService;

    @Autowired
    private RoleService roleService;

    // @Autowired
    // private T24FundsTransferService t24FundsTransferService;

    @GetMapping("/get-data/{donationDetailsId}")
    // @ResponseBody
    public List<DonationCollection> getCollectionData(@PathVariable("donationDetailsId") Long id) {
        logger.info("DONATEE ID:: " + id);
        return donationCollectionService.findAllByDonationDetails(id);
    }

    @GetMapping("/get-payment-records/{institutionId}")
    // @ResponseBody
    public List<InstitutionPayCollections> getPaymentCollectionData(@PathVariable("institutionId") Long id) {
        logger.info("DONATEE ID:: " + id);
        return institutionPayCollectionsService.findAllByInstitutionAndCreateDate(id);
    }


    @GetMapping("/get-data/{user}/{donationDetailsId}")
    // @ResponseBody
    public List<DonationCollection> getCollectionDataByUser(@PathVariable("user") String user,
            @PathVariable("donationDetailsId") Long id) {
        return donationCollectionService.findAllByUser(user, id);
    }

    @GetMapping("/get-payment-records/{user}/{institutionId}")
    // @ResponseBody
    public List<InstitutionPayCollections> getPaymentCollectionDataByUser(@PathVariable("user") String user,
            @PathVariable("institutionId") Long id) {
        return institutionPayCollectionsService.findAllByUser(user, id);
    }

    @GetMapping("/get-data/details-active/{user}/{donationDetailsId}")
    public List<DonationCollection> getCollectionDataByUserAndDetailsEnabled(@PathVariable("user") String user,
            @PathVariable("donationDetailsId") Long id) {
        return donationCollectionService.getAllDonationCollectionsWithEnabledDetails(user, id);
    }

    @GetMapping("/get-payment-records/institution/{user}/{institutionId}")
    public List<InstitutionPayCollections> getCollectionDataByUserAndInstitutionEnabled(
            @PathVariable("user") String user, @PathVariable("institutionId") Long id) {
        return institutionPayCollectionsService.findAllByUserForToday(user, id);
    }

    @PostMapping("/add-data")
    public ResponseEntity<?> addCollectionData(@RequestBody CollectionDTO collectionDTO) {
        logger.info("REQUEST DATA:: " + collectionDTO);
        try {
            if (collectionDTO != null) {
                String respMsg = donationCollectionService.saveCollectionData(collectionDTO);
                logger.info("RESPONSE:: " + respMsg);

                // Assuming `respMsg.split("#")[0]` checks the status code
                if (respMsg.split("#")[0].equals("00")) {

                    return ResponseEntity.ok(new ResponseData("00", "Added Successfully"));

                } else {
                    return ResponseEntity.ok(new ResponseData("06", "Failed"));

                }

            } else {

                return ResponseEntity.badRequest().body(new ResponseData("06", "Collection data is empty"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseData("06", "Exception has occurred!"));

        }

    }

    @PostMapping("/add-payment-record")
    public ResponseEntity<?> addPaymentCollectionData(@RequestBody InstitutionPayObj collectionDTO) {
        logger.info("REQUEST DATA:: " + collectionDTO);
        try {
            if (collectionDTO != null) {

                InstitutionPayCollections institutionPayCollections = new InstitutionPayCollections();
                institutionPayCollections.setPurpose(collectionDTO.getPurpose());
                institutionPayCollections.setAmount(collectionDTO.getAmount());
                institutionPayCollections.setEnteredBy(collectionDTO.getEnteredBy());
                institutionPayCollections.setIndexNumber(collectionDTO.getIndexNumber());
                institutionPayCollections.setInstitution(
                        institutionService.findInstitutionById(Long.parseLong(collectionDTO.getInstitution())));
                institutionPayCollections.setLevel(collectionDTO.getLevel());
                institutionPayCollections.setVoucherNumber(collectionDTO.getVoucherNumber());
                institutionPayCollections.setPhoneNumber(collectionDTO.getPhoneNumber());
                institutionPayCollections.setName(collectionDTO.getName());

                String respMsg = institutionPayCollectionsService.saveInstitutionPaymentData(institutionPayCollections);
                logger.info("RESPONSE:: " + respMsg);

                // Assuming `respMsg.split("#")[0]` checks the status code
                if (respMsg.split("#")[0].equals("00")) {

                    return ResponseEntity.ok(new ResponseData("00", "Added Successfully"));

                } else {
                    return ResponseEntity.ok(new ResponseData("06", "Failed"));

                }

            } else {

                return ResponseEntity.badRequest().body(new ResponseData("06", "Collection data is empty"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseData("06", "Exception has occurred!"));

        }

    }

    @PutMapping("/update-data")
    public ResponseData updateDataByTicket(@RequestBody CollectionDTO collectionDTO) {
        logger.info("RECEIPT:: " + collectionDTO.getReceiptNum());
        String respMsg = donationCollectionService.updateCollectionData(collectionDTO);
        logger.info("RESPONSE:: " + respMsg);
        if (respMsg.split("#")[0].equals("00"))
            return new ResponseData("00", "Added Successfully");
        else
            return new ResponseData("06", "Failed");
    }

    @PutMapping("/update-payment-data")
    public ResponseData updateDataByVoucher(@RequestBody InstitutionPayObj collectionDTO) {
        logger.info("VOUCHER:: {}" + collectionDTO.getVoucherNumber());

        InstitutionPayCollections institutionPayCollections = new InstitutionPayCollections();

        institutionPayCollections.setName(collectionDTO.getName());
        institutionPayCollections.setAmount(collectionDTO.getAmount());
        institutionPayCollections.setIndexNumber(collectionDTO.getIndexNumber());
        institutionPayCollections.setPhoneNumber(collectionDTO.getPhoneNumber());
        institutionPayCollections.setLevel(collectionDTO.getLevel());
        institutionPayCollections.setVoucherNumber(collectionDTO.getVoucherNumber());

        String respMsg = institutionPayCollectionsService.updateCollectionData(institutionPayCollections);
        logger.info("RESPONSE:: " + respMsg);
        if (respMsg.split("#")[0].equals("00"))
            return new ResponseData("00", "Added Successfully");
        else
            return new ResponseData("06", "Failed");
    }

    @GetMapping("/get-data-by-ticket/{receipt_num}")
    public DonationCollection getCollectionDataByTicket(@PathVariable("receipt_num") String receiptNum) {
        logger.info("RECEIPT:: " + receiptNum);
        Optional<DonationCollection> optionalDonationCollection = donationCollectionService.findByReceipt(receiptNum);
        DonationCollection donationCollection = null;
        if (optionalDonationCollection.isPresent())
            donationCollection = optionalDonationCollection.get();
        return donationCollection;
    }

    @GetMapping("/get-data-by-voucher/{voucher}")
    public InstitutionPayCollections getCollectionDataByVoucher(@PathVariable("voucher") String voucher) {
        logger.info("VOUCHER:: " + voucher);
        Optional<InstitutionPayCollections> optionalDonationCollection = institutionPayCollectionsService
                .findByVoucher(voucher);
        InstitutionPayCollections donationCollection = null;
        if (optionalDonationCollection.isPresent())
            donationCollection = optionalDonationCollection.get();
        return donationCollection;
    }

    @GetMapping("/remove-data/{receipt_num}")
    public ResponseData removeDataByTicket(@PathVariable("receipt_num") String receiptNum) {
        logger.info("RECEIPT:: " + receiptNum);
        boolean response = donationCollectionService.removeByReceipt(receiptNum);
        logger.info("RESPONSE:: " + response);
        if (response) {
            return new ResponseData("00", "Reversed successfully");
        } else {
            return new ResponseData("06", "Reversal failed");
        }

    }

    @GetMapping("/remove-payment-data/{voucher}")
    public ResponseData removeDataByVoucher(@PathVariable("voucher") String voucher) {
        logger.info("RECEIPT:: " + voucher);
        boolean response = institutionPayCollectionsService.removeByVoucher(voucher);
        logger.info("RESPONSE:: " + response);
        if (response) {
            return new ResponseData("00", "Reversed successfully");
        } else {
            return new ResponseData("06", "Reversal failed");
        }

    }

    @GetMapping("/get-users")
    public List<Users> getUsers() {

        return userService.findAll();
    }

    @PostMapping("/add-user")
    public ResponseData addUsers(@RequestBody UserDTO user) {
        logger.info("REQUEST:: " + user);
        String respMsg = userService.saveUser(user);
        logger.info("RESPONSE:: " + respMsg);
        if (respMsg.split("#")[0].equals("00"))
            return new ResponseData("00", "Added successfully");
        else
            return new ResponseData("06", "Failed");

    }

    @PostMapping("/update-user")
    public ResponseData editUsers(@RequestBody UserDTO user) {
        logger.info("REQUEST:: " + user);
        String respMsg = userService.updateUser(user);
        logger.info("RESPONSE:: " + respMsg);
        if (respMsg.split("#")[0].equals("00"))
            return new ResponseData("00", "Updated successfully");
        else
            return new ResponseData("06", "Failed");

    }

    @GetMapping("/delete-user/{user_id}")
    public ResponseData deleteUsers(@PathVariable("user_id") long id) {
        logger.info("REQUESTED ID:: " + id);

        if (userService.deleteById(id))
            return new ResponseData("00", "Added successfully");
        else
            return new ResponseData("06", "Failed");

    }

    @GetMapping("/enable-user/{user_id}")
    public ResponseData enableUsers(@PathVariable("user_id") long id) {
        logger.info("REQUESTED ID:: " + id);

        if (userService.enableById(id))
            return new ResponseData("00", "Added successfully");
        else
            return new ResponseData("06", "Failed");

    }

    @GetMapping("/disable-user/{user_id}")
    public ResponseData disableUsers(@PathVariable("user_id") long id) {
        logger.info("REQUESTED ID:: " + id);

        if (userService.disableById(id))
            return new ResponseData("00", "Added successfully");
        else
            return new ResponseData("06", "Failed");

    }

    @GetMapping("/total-users")
    public ResponseData countUsers() {

        try {
            String numOfUsers = userService.countUsers();
            if (numOfUsers != null) {
                return new ResponseData("00", numOfUsers);
            } else {
                return new ResponseData("06", null);
            }

        } catch (Exception e) {
            logger.error("Exception, " + e.getMessage());
            return new ResponseData("07", "0");
        }

    }

    @GetMapping("/total-collections/{donation_details_id}")
    public ResponseData countCollections(@PathVariable("donation_details_id") long donation_details_id) {

        try {
            String numOfCollection = donationCollectionService.countCollections(donation_details_id);
            if (numOfCollection != null) {
                return new ResponseData("00", numOfCollection);
            } else {
                return new ResponseData("06", null);
            }

        } catch (Exception e) {
            logger.error("Exception, " + e.getMessage());
            return new ResponseData("07", "0");
        }

    }

    @GetMapping("/total-collections-institution/{institutionId}")
    public ResponseData countPaymentForInstitution(@PathVariable("institutionId") long institutionId) {

        try {
            String numOfCollection = institutionPayCollectionsService.countCollections(institutionId);
            if (numOfCollection != null) {
                return new ResponseData("00", numOfCollection);
            } else {
                return new ResponseData("06", null);
            }

        } catch (Exception e) {
            logger.error("Exception, " + e.getMessage());
            return new ResponseData("07", "0");
        }

    }

    @GetMapping("/get-donation-details/{donation_details_id}")
    public ResponseData donationDetailsById(@PathVariable("donation_details_id") long donation_details_id) {
        DonationDetails donationDetails;
        try {
            donationDetails = donationDetailsService.findDetailsById(donation_details_id);
            return new ResponseData("00", new JSONObject(donationDetails).toString());
        } catch (Exception e) {
            logger.error("Exception:: " + e.getMessage());
            return new ResponseData("06", "0");
        }
    }

    @GetMapping("/get-donation-details-for-selection")
    public ResponseData getLatestFuneralDetails() {
        List<DonationDetails> donationDetails;
        try {
            donationDetails = donationDetailsService.findNotMoreThanFourDays();
            return new ResponseData("00", new JSONArray(donationDetails).toString());
        } catch (Exception e) {
            logger.error("Exception:: " + e.getMessage());
            return new ResponseData("06", "0");
        }
    }

    @GetMapping("/donation-details/all")
    public List<DonationDetails> getLatestDonationDetails() {
        List<DonationDetails> donationDetails;
        try {
            donationDetails = donationDetailsService.findAll();
            return donationDetails;
        } catch (Exception e) {
            logger.error("Exception:: " + e.getMessage());
            return null;
        }
    }

    @GetMapping("/donation-details/by-enabled")
    public ResponseData getDonationDetailsByEnabled() {
        List<DonationDetails> donationDetails;
        try {
            donationDetails = donationDetailsService.findAllByEnabled();
            return new ResponseData("00", new JSONArray(donationDetails).toString());
        } catch (Exception e) {
            logger.error("Exception:: " + e.getMessage());
            return new ResponseData("06", null);
        }
    }

    @GetMapping("/delete-donatee/{donate_details_id}")
    public ResponseData deleteDonateDetails(@PathVariable("donate_details_id") long id) {
        logger.info("REQUESTED ID:: " + id);

        if (donationDetailsService.deleteById(id))
            return new ResponseData("00", "Added successfully");
        else
            return new ResponseData("06", "Failed");

    }

    @GetMapping("/enable-donatee/{donate_details_id}")
    public ResponseData enableDonateDetails(@PathVariable("donate_details_id") long id) {
        logger.info("REQUESTED ID:: " + id);

        if (donationDetailsService.enableById(id))
            return new ResponseData("00", "Added successfully");
        else
            return new ResponseData("06", "Failed");

    }

    @GetMapping("/disable-donatee/{donate_details_id}")
    public ResponseData disableDonateDetails(@PathVariable("donate_details_id") long id) {
        logger.info("REQUESTED ID:: " + id);

        if (donationDetailsService.disableById(id))
            return new ResponseData("00", "Added successfully");
        else
            return new ResponseData("06", "Failed");

    }

    @PostMapping("/edit-funeral-details")
    public ResponseData editFuneralDetails(@RequestBody DonationDetailsObj donationDetails) {
        logger.info("REQUEST:: " + donationDetails);
        String respMsg = donationDetailsService.updateDonationDetails(donationDetails);
        logger.info("RESPONSE:: " + respMsg);
        if (respMsg.split("#")[0].equals("00"))
            return new ResponseData("00", "Updated successfully");
        else
            return new ResponseData("06", "Failed");

    }

    @GetMapping("/roles")
    public ResponseData getRoles() {
        List<Role> roles;
        try {
            roles = roleService.findAll();
            return new ResponseData("00", new JSONArray(roles).toString());
        } catch (Exception e) {
            logger.error("Exception:: " + e.getMessage());
            return new ResponseData("06", "0");
        }
    }

    @GetMapping("/total-amount/{currency}/{donationDetailsId}")
    public ResponseData sumOfAmountInCurrency(@PathVariable("currency") String currency,
            @PathVariable("donationDetailsId") long id) {
        double sumOfAmount = 0;
        try {

            // sumOfAmount = donationCollectionService.sumCollectionsByCurrency(currency);
            sumOfAmount = donationCollectionService.sumCollectionsByCurrencyAndDetailsId(currency, id);
            return new ResponseData("00", String.valueOf(sumOfAmount));
        } catch (Exception e) {
            logger.error("Exception:: " + e.getMessage());
            return new ResponseData("06", "0");
        }
    }

    @GetMapping("/total-amount-institution/{currency}/{institutionId}")
    public ResponseData sumOfAmountInstitutionInCurrency(
            @PathVariable("institutionId") long institutionId) {
        double sumOfAmount = 0;
        try {

            sumOfAmount = institutionPayCollectionsService.sumCollectionsByInstitutionId(institutionId);
            return new ResponseData("00", String.valueOf(sumOfAmount));
        } catch (Exception e) {
            logger.error("Exception:: " + e.getMessage());
            return new ResponseData("06", "0");
        }
    }

    @GetMapping("/sum-amount/teller/{whoseEntering}/{donationDetailsId}")
    public ResponseData sumOfAmountInGHSForWhoseEntering(@PathVariable("whoseEntering") String whoseEntering,
            @PathVariable("donationDetailsId") long id) {
        double sumOfAmount = 0;
        try {
            sumOfAmount = donationCollectionService.sumCollectionsInGHSForWhoseEntering(whoseEntering, id);
            return new ResponseData("00", String.valueOf(Math.round(sumOfAmount)));
        } catch (Exception e) {
            logger.error("Exception:: " + e.getMessage());
            return new ResponseData("06", "0");
        }
    }

    @GetMapping("/sum-amount-institution/teller/{enteredBy}/{institutionId}")
    public ResponseData sumOfAmountInstitutionInGHSForWhoseEntering(@PathVariable("enteredBy") String enteredBy,
            @PathVariable("institutionId") long institutionId) {
        Double sumOfAmount = 0.0;
        try {
            sumOfAmount = institutionPayCollectionsService.sumCollectionsInGHSForWhoseEntering(enteredBy,
                    institutionId);
            return new ResponseData("00", String.valueOf(Math.round(sumOfAmount)));
        } catch (Exception e) {
            logger.error("Exception:: " + e.getMessage());
            return new ResponseData("06", "0");
        }
    }

    @PostMapping("/add-donation-details")
    public ResponseData addDonationDetails(@RequestParam("poster") MultipartFile imageFile,
            @RequestParam("nameD") String nameD, @RequestParam("purpose") String purpose,
            @RequestParam("location") String location) {

        if (imageFile.isEmpty()) {
            return new ResponseData("06", "Failed to upload. Please select a file.");
        }

        logger.info("NAME:: " + nameD);
        try {
            DonationDetails donationDetails = new DonationDetails();
            byte[] imageBytes = imageFile.getBytes();

            String encodedImage = ResourcesClasses.encodeImage(imageBytes);

            String urlToSave = new ResourcesClasses().imageURL(uploadURL, encodedImage);
            logger.info("URL TO SAVE:: " + urlToSave);
            if (urlToSave != null) {
                donationDetails.setName(nameD);
                donationDetails.setPicLocation(urlToSave);
                donationDetails.setPurpose(purpose);
                donationDetails.setLocation(location);

                String respMsg = donationDetailsService.saveDonationDetails(donationDetails);

                if (respMsg.split("#")[0].equals("00"))
                    return new ResponseData("00", "Added successfully");
                else
                    return new ResponseData("06", "Failed");
            } else {
                return new ResponseData("06", "Couldn't get image url");
            }

        } catch (Exception e) {
            logger.error("Exception " + e.getMessage());
            return new ResponseData("07", "Failed, exception occurred, + " + e.getMessage());
        }
    }

    public static String encodeImage(byte[] imageBytes) throws IOException {

        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        return base64Image;
    }

    public String imageURL(String url, String encodedImageString) {
        String imageURL = null;
        try {
            logger.info("UPLOAD URL::: " + url);
            String response = new DoRequest().PostURLEncode(url, encodedImageString);
            logger.info("RESPONSE RECEIVED FROM API:: " + response);
            if (response != null) {
                JSONObject respJsonObject = new JSONObject(response);
                if (respJsonObject.optString("status_code").equals("200"))
                    imageURL = respJsonObject.optJSONObject("image").optString("url");
            }
        } catch (Exception e) {
            logger.error("Exception occurred, " + e.getMessage());
        }
        return imageURL;
    }

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("Passw0rdS@m|"));
    }

    @GetMapping("/get-institutions/{institution_id}")
    public ResponseData institutionById(@PathVariable("institution_id") long institution_id) {
        Institution institution;
        try {
            institution = institutionService.findInstitutionById(institution_id);
            return new ResponseData("00", new JSONObject(institution).toString());
        } catch (Exception e) {
            logger.error("Exception:: " + e.getMessage());
            return new ResponseData("06", "0");
        }
    }

    @GetMapping("/get-institution-for-selection")
    public ResponseData getInstitutionForSelect() {
        List<Institution> institutions;
        try {
            institutions = institutionService.findNotMoreThanFourDays();
            return new ResponseData("00", new JSONArray(institutions).toString());
        } catch (Exception e) {
            logger.error("Exception:: " + e.getMessage());
            return new ResponseData("06", "0");
        }
    }

    @GetMapping("/institutions/all")
    public List<Institution> getAllInstitutions() {
        List<Institution> institutions;
        try {
            institutions = institutionService.findAll();
            return institutions;
        } catch (Exception e) {
            logger.error("Exception:: " + e.getMessage());
            return null;
        }
    }

    @GetMapping("/institution/by-enabled")
    public ResponseData getInstititutionsByEnabled() {
        List<Institution> institutions;
        try {
            institutions = institutionService.findAllByEnabled();
            return new ResponseData("00", new JSONArray(institutions).toString());
        } catch (Exception e) {
            logger.error("Exception:: " + e.getMessage());
            return new ResponseData("06", null);
        }
    }

    @GetMapping("/delete-institution/{institution_id}")
    public ResponseData deleteInstitution(@PathVariable("institution_id") long id) {
        logger.info("REQUESTED ID:: " + id);

        if (institutionService.deleteById(id))
            return new ResponseData("00", "Added successfully");
        else
            return new ResponseData("06", "Failed");

    }

    @GetMapping("/enable-institution/{institution_id}")
    public ResponseData enableIns(@PathVariable("institution_id") long id) {
        logger.info("REQUESTED ID:: " + id);

        if (institutionService.enableById(id))
            return new ResponseData("00", "Added successfully");
        else
            return new ResponseData("06", "Failed");

    }

    @GetMapping("/disable-institution/{institution_id}")
    public ResponseData disableInstitution(@PathVariable("institution_id") long id) {
        logger.info("REQUESTED ID:: " + id);

        if (institutionService.disableById(id))
            return new ResponseData("00", "Added successfully");
        else
            return new ResponseData("06", "Failed");

    }

    @PostMapping("/edit-institution")
    public ResponseData editInstitution(@RequestBody DonationDetailsObj institution) {
        logger.info("REQUEST:: " + institution);
        String respMsg = institutionService.updateInstitution(institution);
        logger.info("RESPONSE:: " + respMsg);
        if (respMsg.split("#")[0].equals("00"))
            return new ResponseData("00", "Updated successfully");
        else
            return new ResponseData("06", "Failed");

    }

}
