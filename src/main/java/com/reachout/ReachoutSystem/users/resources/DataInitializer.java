package com.reachout.ReachoutSystem.users.resources;

import com.reachout.ReachoutSystem.users.entity.Document;
import com.reachout.ReachoutSystem.users.entity.DocumentType;
import com.reachout.ReachoutSystem.users.entity.Role;
import com.reachout.ReachoutSystem.users.entity.User;
import com.reachout.ReachoutSystem.users.repository.DocumentRepository;
import com.reachout.ReachoutSystem.users.repository.UserRepository;
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
        User existingADMINUser = this.userRepository.findByName("ADMIN");
        User existingSYSADMINUser = this.userRepository.findByName("SYSADMIN");

        return args -> {
            if (existingSYSADMINUser == null && existingADMINUser == null) {
                User sysadmin = new User();
                User admin = new User();
                Document documentSysadmin = new Document();
                Document documentAdmin = new Document();

                documentAdmin.setDocumentType(DocumentType.CPF);
                documentAdmin.setDocumentNumber("12345");
                documentAdmin.setCreatedAt(LocalDateTime.now());
                documentAdmin.setUpdatedAt(LocalDateTime.now());

                documentSysadmin.setDocumentType(DocumentType.CPF);
                documentSysadmin.setDocumentNumber("123456789");
                documentSysadmin.setCreatedAt(LocalDateTime.now());
                documentSysadmin.setUpdatedAt(LocalDateTime.now());

                documentRepository.save(documentAdmin);
                documentRepository.save(documentSysadmin);

                admin.setName("ADMIN");
                admin.setUid("ADMIN");
                admin.setRole(Role.ADMIN);
                admin.setEmail("admin@reachout.com");
                admin.setStatus(true);
                admin.setCreatedAt(LocalDateTime.now());
                admin.setUpdatedAt(LocalDateTime.now());
                admin.setAddress("Rua teste");
                admin.setPhotoPath("http:bucket");
                admin.setPhone("000000000000");
                admin.setDocument(documentAdmin);
                admin.setBirthday(LocalDate.of(2024, 9, 28));

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
                userRepository.save(admin);
                userRepository.save(sysadmin);
                System.out.println("USUÁRIO SYSADMIN ADICIONADO E INICIALIZADO.");
            } else {
                System.out.println("O usuário SYSADMIN ou ADMIN já existem.");
            }
        };
    }
}
