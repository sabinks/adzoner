package com.adzoner.api.service.user;

import com.adzoner.api.dto.ReceiverDto;
import com.adzoner.api.dto.user.UserCanPublishDto;
import com.adzoner.api.entity.Role;
import com.adzoner.api.entity.User;
import com.adzoner.api.mail.user.UserCanPublishAdvertisementMail;
import com.adzoner.api.repository.RoleRepository;
import com.adzoner.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PartnerService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserCanPublishAdvertisementMail partnerCanPublishAdvertisementMail;

    public PartnerService(UserRepository userRepository, RoleRepository roleRepository, UserCanPublishAdvertisementMail partnerCanPublishAdvertisementMail) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.partnerCanPublishAdvertisementMail = partnerCanPublishAdvertisementMail;
    }

    public Page<User> index(Integer offset, Integer pageSize, String search, String sort, String sortBy) {
        Role role = roleRepository.findByName("PARTNER");
        if (role == null) {
            throw new EntityNotFoundException("Partner role not found");
        }
        return userRepository.findAllUserByRole(role.getName(), PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy));
    }

    public ResponseEntity<String> partnerCanPublishStatus(UserCanPublishDto userCanPublishDto, Long id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Partner record not found!");
        }
        user.get().setCanPublish(userCanPublishDto.getStatus());
        Map<String, String> data = new HashMap<>();
        ReceiverDto receiverDto = new ReceiverDto();
        userRepository.save(user.get());

        if (userCanPublishDto.getStatus()) {
            receiverDto.setSubject("Documents verification completed");
            data.put("message", "Your uploaded documents are verified. Please login again and upload the advertisements." );
        } else {
            receiverDto.setSubject("Documents validation failed");
            data.put("message", "Your uploaded documents are not valid, please upload valid documents. Our admin will recheck your documents, you will get notification soon.");
        }

        receiverDto.setName(user.get().getName());
        receiverDto.setEmail(user.get().getEmail());
        receiverDto.setData(data);
        partnerCanPublishAdvertisementMail.sendMail(receiverDto);

        return new ResponseEntity<>("Partner publish status changed!", HttpStatus.OK);
    }
}
