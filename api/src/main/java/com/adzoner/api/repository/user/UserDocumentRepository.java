package com.adzoner.api.repository.user;

import com.adzoner.api.entity.user.Document;
import com.adzoner.api.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserDocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByDocumentName(String filename);

    List<Document> findByUserId(Long id);

    @Query(value = "SELECT d FROM Document d WHERE d.user = ?1")
    List<Document> findByUserId(User user, PageRequest pageable);

    @Query(value = "DELETE FROM Document d where d.documentName = ?1")
    void deleteByDocumentName(String documentName);
}
