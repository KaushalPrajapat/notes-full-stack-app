package com.notes.notes_app.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@Entity
@Table(name = "notes_notes_")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;

    @Column(name = "note_heading")
    private String noteHeading;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String content;


    private String noteOwner;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp(source = SourceType.DB)
    private LocalDateTime updatedDate;
}
