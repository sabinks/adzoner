package com.adzoner.api.service;

import com.adzoner.api.dto.ReceiverDto;
import com.adzoner.api.dto.RegisterDto;
import com.adzoner.api.dto.user.*;
import com.adzoner.api.entity.Advertisement;
import com.adzoner.api.entity.Role;
import com.adzoner.api.entity.User;
import com.adzoner.api.entity.membership.SubscriptionType;
import com.adzoner.api.entity.membership.Subscription;
import com.adzoner.api.mail.ResetPassword;
import com.adzoner.api.mail.VerificationMail;
import com.adzoner.api.mail.user.DocumentSubmissionMail;
import com.adzoner.api.repository.AdvertisementRepository;
import com.adzoner.api.repository.RoleRepository;
import com.adzoner.api.repository.UserRepository;
import com.adzoner.api.repository.membership.SubscriptionRepository;
import com.adzoner.api.repository.membership.SubscriptionTypeRepository;
import com.adzoner.api.utility.MyUtilityClass;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;

import static com.adzoner.api.utility.MyUtilityClass.getExtensionFromFilename;

@Service
public class UserService {
    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionTypeRepository subscriptionTypeRepository;
    private final MyUtilityClass myUtilityClass;
    private final VerificationMail verificationMail;
    private final DocumentSubmissionMail documentSubmissionMail;
    private final ResetPassword resetPassword;
    @Value("${spring.app.url}")
    private String appUrl;

    @Value("${spring.app.dashboard_url}")
    private String dashboardUrl;
    @Value("${spring.upload.directory}")
    private String uploadDirectory;

    @Value("${spring.user.ads.limit}")
    private String adsLimit;
    public UserService(AdvertisementRepository advertisementRepository, UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository,
                       SubscriptionRepository subscriptionRepository,
                       SubscriptionTypeRepository subscriptionTypeRepository,
                       MyUtilityClass myUtilityClass,
                       VerificationMail verificationMail,
                       DocumentSubmissionMail documentSubmissionMail,
                       ResetPassword resetPassword) {
        this.advertisementRepository = advertisementRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.subscriptionTypeRepository = subscriptionTypeRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.myUtilityClass = myUtilityClass;
        this.verificationMail = verificationMail;
        this.documentSubmissionMail = documentSubmissionMail;
        this.resetPassword = resetPassword;
    }


    @Transactional
    public void registerUser(RegisterDto registerDto, String roleName) throws Exception {
        List<User> users = userRepository.findByEmail(registerDto.getEmail());
        if (!users.isEmpty()) {
            throw new EntityExistsException("User email already registered!");
        }
        //add user
        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setContactNumber(registerDto.getContactNumber());
        user.setActive(false);
        user.setCanPublish(false);
        user.setEmailVerifiedAt("");
        String verificationToken = myUtilityClass.getAlphaNumericString(10);
        user.setVerificationToken(verificationToken);
        user.setCreatedAt(MyUtilityClass.currentDateTime());
        user.setUpdatedAt(MyUtilityClass.currentDateTime());
        User savedUser = userRepository.save(user);

        //assign role to user
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new EntityNotFoundException("Role not found!");
        }
        user.addRole(role);

        Map<String, String> data = new HashMap<>();
        if (role.getName().matches("PARTNER")) {
            Optional<SubscriptionType> subscriptionType = subscriptionTypeRepository.findByName("Free Plan");
            if(subscriptionType.isEmpty()){
                throw new EntityNotFoundException("Membership type plan not found!");
            }
            Subscription subscription = new Subscription();
            subscription.setSubscriptionType(subscriptionType.get());
            subscription.setStatus(true);
            subscription.setStartedAt(MyUtilityClass.currentDateTime());
            subscription.setCreatedAt(MyUtilityClass.currentDateTime());
            subscription.setExpiresAt("Never");

            subscription.setUser(user);
            subscriptionRepository.save(subscription);

            data.put("planName", subscriptionType.get().getName());
            data.put("adsCount", subscriptionType.get().getAdsCount().toString());
        }

        //send verification mail to user
        String verify_url = dashboardUrl + "/email-verify?id=" + savedUser.getId() + "&token=" + verificationToken;

        //prepare receiver detail
        ReceiverDto receiverDto = new ReceiverDto();
        receiverDto.setEmail(user.getEmail());
        receiverDto.setName(user.getName());
        receiverDto.setUrl(verify_url);

        verificationMail.sendMail(receiverDto);

        if (role.getName().matches("PARTNER")) {
            ReceiverDto receiverDto1 = new ReceiverDto();
            receiverDto1.setEmail(user.getEmail());
            receiverDto1.setName(user.getName());
            receiverDto1.setData(data);
            documentSubmissionMail.sendMail(receiverDto1);
        }
    }

    @Transactional
    public void verifyEmail(Long id, String verification_token) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        if (user.get().getVerificationToken().matches("")) {
            throw new Exception("Email verified, if issue, please reset password");
        }
        if (!user.get().getVerificationToken().matches(verification_token)) {
            throw new Exception("Verification token mismatch");
        }

        user.get().setVerificationToken("");
        user.get().setActive(true);
        user.get().setEmailVerifiedAt(MyUtilityClass.currentDateTime());
        user.get().setUpdatedAt(MyUtilityClass.currentDateTime());

        userRepository.save(user.get());
    }
    public void forgotPassword(ForgotPasswordDto forgotPasswordDto) throws Exception {
        String email = forgotPasswordDto.getEmail();
        List<User> users = userRepository.findByEmail(email);
        if (users.isEmpty()) {
            throw new EntityNotFoundException("User email not registered, please recheck email address.");
        }
        String resetToken = myUtilityClass.getAlphaNumericString(15);
        users.get(0).setResetToken(resetToken);
        users.get(0).setUpdatedAt(MyUtilityClass.currentDateTime());
        userRepository.save(users.get(0));

        String reset_url = dashboardUrl + "/reset-password?token=" + resetToken;
        ReceiverDto receiverDto = new ReceiverDto();
        receiverDto.setName(users.get(0).getName());
        receiverDto.setEmail(users.get(0).getEmail());
        receiverDto.setUrl(reset_url);

        resetPassword.sendMail(receiverDto);
    }

    public void resetPassword(ResetPasswordDto resetPasswordDto) throws Exception {
        List<User> user = userRepository.findByEmail(resetPasswordDto.getEmail());
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        if (!user.get(0).getResetToken().matches(resetPasswordDto.getResetToken())) {
            throw new Exception("Provided reset token mismatch");
        }
        user.get(0).setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
        user.get(0).setResetToken("");
        user.get(0).setUpdatedAt(MyUtilityClass.currentDateTime());

        userRepository.save(user.get(0));
    }

    public void changePassword(PasswordChangeDto passwordChangeDto) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getPrincipal().toString();
        List<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User record not found!");
        }
        if (!passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), user.get(0).getPassword())) {
            throw new Exception("Current password does not match");
        }
        user.get(0).setPassword(passwordEncoder.encode(passwordChangeDto.getPassword()));

        userRepository.save(user.get(0));
    }

    public void userStatusChange(Long id, ActiveStatusChangeDto userData) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found!");
        }
        user.get().setActive(userData.getStatus());
        userRepository.save(user.get());
    }

    public Map<String, String> getUser() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getPrincipal().toString();
        List<User> user = userRepository.findByEmail(userEmail);
        Map<String, String> userData = new HashMap<>();
        List<Advertisement> advertisementList = advertisementRepository.findByUserIdAndPublish(user.get(0).getId(), true);
        if(user.get(0).getRoles().get(0).getName().matches("USER")){
            userData.put("remainingAds", String.valueOf(Integer.parseInt(adsLimit) - advertisementList.size()));
        }
        if(user.get(0).getRoles().get(0).getName().matches("PARTNER")){
            List<Subscription> subscriptionList = subscriptionRepository.findLatestByUser(user.get(0));
            SubscriptionType subscriptionType = subscriptionList.get(0).getSubscriptionType();
            userData.put("remainingAds", String.valueOf(subscriptionType.getAdsCount() - advertisementList.size()));
        }
        if(Objects.equals(user.get(0).getEmailVerifiedAt(), "")){
            throw  new BadCredentialsException("Please check your email and click verify button/link for email verification");
        }
        if(!user.get(0).getActive()){
            throw new BadCredentialsException("Your account is disabled, please contact us at info@adzoner.com for further details.");
        }
        userData.put("email", user.get(0).getEmail());
        userData.put("role", user.get(0).getRoles().get(0).getName());
        userData.put("name", user.get(0).getName());
        userData.put("contactNumber", user.get(0).getContactNumber());
        userData.put("canPublish", user.get(0).getCanPublish().toString());
        userData.put("currentPublishedAds", String.valueOf((long) advertisementList.size()));
        userData.put("profileImage", user.get(0).getProfileImage());
        userData.put("profileImagePath", "");
        return userData;
    }

    public void setUser(UserDetailDto userDetailDto, MultipartFile profileImage) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getPrincipal().toString();
        List<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found!");
        }
        user.get(0).setName(userDetailDto.getName());
        user.get(0).setContactNumber(userDetailDto.getContactNumber());
        String oldProfileImage =user.get(0).getProfileImage();

        if(!profileImage.isEmpty()){
            String original_document_name = profileImage.getOriginalFilename();
            Optional<String> extension = getExtensionFromFilename(original_document_name);
            if (extension.isEmpty()) {
                throw new Exception("Profile image does not have extension!");
            }
            Random random = new Random();
            int randomNumber = random.nextInt(999999);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String document_name = timestamp.getTime() + randomNumber + "." + extension.get();
            Path filenameAndPath = Paths.get(uploadDirectory + "/profile_image", document_name);
            user.get(0).setProfileImage(document_name);
            Files.write(filenameAndPath, profileImage.getBytes());
        }

        userRepository.save(user.get(0));

        if(oldProfileImage != null){
            Path filenameAndPath = Paths.get(uploadDirectory + "/profile_image", oldProfileImage);
            Files.delete(filenameAndPath);
        }
    }

    public Map<String, String> currentPublishedAds() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getPrincipal().toString();
        List<User> user = userRepository.findByEmail(userEmail);

        Map<String, String> userData = new HashMap<>();
        List<Advertisement> advertisementList = advertisementRepository.findByUserIdAndPublish(user.get(0).getId(), true);
        userData.put("email", user.get(0).getEmail());
        userData.put("role", user.get(0).getRoles().get(0).getName());
        userData.put("name", user.get(0).getName());
        userData.put("contactNumber", user.get(0).getContactNumber());
        userData.put("canPublish", user.get(0).getCanPublish().toString());
        userData.put("currentPublishedAds", String.valueOf((long) advertisementList.size()));
        return userData;
    }
    public User loadUserByUsername(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getPrincipal().toString();
        List<User> user = userRepository.findByEmail(userEmail);
        return user.get(0);
    }
}
