package com.reachout.ReachoutSystem.user.resources;

import com.reachout.ReachoutSystem.user.entity.Document;
import com.reachout.ReachoutSystem.user.entity.DocumentType;
import com.reachout.ReachoutSystem.user.entity.Role;
import com.reachout.ReachoutSystem.user.entity.User;
import com.reachout.ReachoutSystem.user.repository.DocumentRepository;
import com.reachout.ReachoutSystem.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Bean
    public ApplicationRunner initializeData() {
        User existingSYSADMINUser = this.userRepository.findByName("SYSADMIN");

        return args -> {
            if (existingSYSADMINUser == null) {
                User sysadmin = new User();
                Document documentSysadmin = new Document();

                documentSysadmin.setDocumentType(DocumentType.CPF);
                documentSysadmin.setDocumentNumber("123456789");
                documentSysadmin.setCreatedAt(LocalDateTime.now());
                documentSysadmin.setUpdatedAt(LocalDateTime.now());

                documentRepository.save(documentSysadmin);

                sysadmin.setName("SYSADMIN");
                sysadmin.setUid("SYSADMIN");
                sysadmin.setRole(Role.SYSADMIN);
                sysadmin.setEmail("sysadmin@reachout.com");
                sysadmin.setStatus(true);
                sysadmin.setCreatedAt(LocalDateTime.now());
                sysadmin.setUpdatedAt(LocalDateTime.now());
                sysadmin.setAddress("Rua teste");
                sysadmin.setPhotoPath("http:bucket");
                sysadmin.setPhone("000000000000");
                sysadmin.setDocument(documentSysadmin);
                sysadmin.setBirthday(LocalDate.of(2024, 9, 28));
                userRepository.save(sysadmin);
                System.out.println("USUÁRIO SYSADMIN ADICIONADO E INICIALIZADO.");
            } else {
                System.out.println("O usuário SYSADMIN já existe.");
            }
        };
    }
}
