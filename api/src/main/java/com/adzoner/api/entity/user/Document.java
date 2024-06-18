package com.adzoner.api.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.adzoner.api.entity.DocumentType;
import com.adzoner.api.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;

    private String documentName;

    private String originalDocumentName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
