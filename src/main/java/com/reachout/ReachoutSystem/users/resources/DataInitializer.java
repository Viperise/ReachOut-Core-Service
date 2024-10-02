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
        User existingUser = this.userRepository.findByName("SYSADMIN");

        return args -> {
            if (existingUser == null) {
                User sysadmin = new User();
                Document documentSysadmin = new Document();

                documentSysadmin.setDocumentType(DocumentType.CPF);
                documentSysadmin.setDocumentNumber("123456789");
                documentSysadmin.setCreatedAt(LocalDateTime.now());
                documentSysadmin.setUpdatedAt(LocalDateTime.now());

                documentRepository.save(documentSysadmin);

                sysadmin.setName("SYSADMIN");
                sysadmin.setUid("02mXUusynjegtTl7IhQFUVqyQy82");
                sysadmin.setRole(Role.SYSADMIN);
                sysadmin.setEmail("admin@reachout.com");
                sysadmin.setStatus(true);
                sysadmin.setCreatedAt(LocalDateTime.now());
                sysadmin.setUpdatedAt(LocalDateTime.now());
                sysadmin.setAddress("Rua teste");
                sysadmin.setProfilePhotoPath("http:bucket");
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
