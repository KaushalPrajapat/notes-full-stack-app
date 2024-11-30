package com.notes.notes_app.models;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notes_notes_logs")
public class NoteLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteLogId;

    private Long nodeWhichGotChanged;

    private String noteOwner;

    private String changedBy;

    //    Newly added data
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String newContent;
    //    Changed-data
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String oldContent;

    private String newHeading;
    private String oldHeading;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

}
