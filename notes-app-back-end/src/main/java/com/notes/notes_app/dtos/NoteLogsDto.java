package com.notes.notes_app.dtos;


import com.notes.notes_app.models.Note;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class NoteLogsDto {
    private Long noteLogId;

    private String noteOwner;

    private String changedBy;

    private String oldHeading;
    private String newHeading;
    //    Newly added data
    private String newContent;

    //    Changed-data
    private String oldContent;

    private String createdDate;

}
