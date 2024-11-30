package com.notes.notes_app.dtos;

import com.notes.notes_app.models.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto {
    private Long noteId;

    private String noteHeading;

    private String content;

    private String noteOwner;

    private String createdDate;

    private String updatedDate;
}
