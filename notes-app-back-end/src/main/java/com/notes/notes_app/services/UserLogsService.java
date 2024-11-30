package com.notes.notes_app.services;

import com.notes.notes_app.dtos.UserLogsDto;
import com.notes.notes_app.models.User;
import com.notes.notes_app.models.UserLogs;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserLogsService {
    List<UserLogsDto> getAllLogs();

    List<UserLogsDto> getAllLogsByAUser(Long userId);

    List<UserLogsDto> getAllLogsOnAUser(Long userId);

    void createAChangeLogAndSave(Long changedOn, Map<String, String> changes);
}
