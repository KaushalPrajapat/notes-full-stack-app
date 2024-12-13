package com.notes.notes_app.services.Impl;

import com.notes.notes_app.dtos.UserLogsDto;
import com.notes.notes_app.exceptions.CustomException;
import com.notes.notes_app.models.User;
import com.notes.notes_app.models.UserLogs;
import com.notes.notes_app.repos.UserLogsRepository;
import com.notes.notes_app.repos.UserRepository;
import com.notes.notes_app.services.UserLogsService;
import com.notes.notes_app.utils.AuthUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserLogsServiceImpl implements UserLogsService {

    @Autowired
    private UserLogsRepository userLogsRepository;
    @Autowired
    private AuthUtils authUtils;
    //    This Dependency making cycle so using userRepo to find user by userId
//    @Autowired
//    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserLogsDto> getAllLogs() {
        List<UserLogs> allUserLogs = userLogsRepository.findAll();
        List<UserLogsDto> results = new ArrayList<>();
        for (var userLog : allUserLogs) {
            UserLogsDto result = new UserLogsDto();
            BeanUtils.copyProperties(userLog, result);
            result.setChangedBy(userLog.getChangedBy() != null ? userLog.getChangedBy().getUsername() : "User Deleted");
            result.setUpdatedDate(userLog.getUpdatedDate().toString().substring(0, 10) + " at " + userLog.getUpdatedDate().toString().substring(11, 19));
            results.add(result);
        }
        return results;
    }

    @Override
    public List<UserLogsDto> getAllLogsByAUser(Long userId) {
        List<UserLogs> allLogsByUser = userLogsRepository.findByChangedBy(getMeUser(userId));
        List<UserLogsDto> results = new ArrayList<>();
        for (var userLog : allLogsByUser) {
            UserLogsDto result = new UserLogsDto();
            BeanUtils.copyProperties(userLog, result);
            result.setChangedBy(userLog.getChangedBy().getUsername());
            result.setUpdatedDate(userLog.getUpdatedDate().toString().substring(0, 10) + " at " + userLog.getUpdatedDate().toString().substring(11, 19));
            results.add(result);
        }
        return results;
    }

    private User getMeUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException("user doesn't exists", "USER_NOT_EXISTS"));
    }

    @Override
    public List<UserLogsDto> getAllLogsOnAUser(Long userId) {
        List<UserLogs> allLogsOnUser = userLogsRepository.findByChangedOn(userId);
        List<UserLogsDto> results = new ArrayList<>();
        for (var userLog : allLogsOnUser) {
            UserLogsDto result = new UserLogsDto();
            BeanUtils.copyProperties(userLog, result);
            result.setChangedBy(userLog.getChangedBy().getUsername());
            result.setUpdatedDate(userLog.getUpdatedDate().toString().substring(0, 10) + " at " + userLog.getUpdatedDate().toString().substring(11, 19));
            results.add(result);
        }
        return results;
    }

    @Override
    public void createAChangeLogAndSave(Long changedOn, Map<String, String> changes) {
//        Create a change log
//        new UserLogs Object
        UserLogs userLogs = new UserLogs();
        User loggedInUser = null;
        if (!changes.containsKey("oauth2"))
            loggedInUser = authUtils.loggedInUser();
        if (loggedInUser == null) {
            loggedInUser = userRepository.findByUsername("self")
                    .orElseThrow(() -> new CustomException("Super user doesn't exists", "SU_NOT_FOUND"));
        }
        //  Derive user who is logged in
        userLogs.setChangedBy(loggedInUser);
        userLogs.setChangedOn(changedOn);
        String operations = String.valueOf(changes.keySet()).substring(3, String.valueOf(changes.keySet()).length() - 1);
        userLogs.setOperation(operations);

        if (changes.containsKey("isPasswordUpdated"))
            userLogs.setPasswordUpdated(changes.get("isPasswordUpdated").equals("true"));
        if (changes.containsKey("isAccountNonLockedStatusUpdated")) //--locked
            userLogs.setAccountNonLockedStatusUpdated(changes.get("isAccountNonLockedStatusUpdated").equals("true"));
        if (changes.containsKey("isAccountNonExpiredStatusUpdated"))  //--expire
            userLogs.setAccountNonExpiredStatusUpdated(changes.get("isAccountNonExpiredStatusUpdated").equals("true"));
        if (changes.containsKey("isCredentialsNonExpiredStatusUpdated"))
            userLogs.setCredentialsNonExpiredStatusUpdated(changes.get("isCredentialsNonExpiredStatusUpdated").equals("true"));
        if (changes.containsKey("isEnabledStatusUpdated"))
            userLogs.setEnabledStatusUpdated(changes.get("isEnabledStatusUpdated").equals("true"));
        if (changes.containsKey("isTwoFactorEnabledStatusUpdated"))
            userLogs.setTwoFactorEnabledStatusUpdated(changes.get("isTwoFactorEnabledStatusUpdated").equals("true"));
        if (changes.containsKey("isUserDeleted"))
            userLogs.setUserDeleted(changes.get("isUserDeleted").equals("true"));
        if (changes.containsKey("isRoleUpdated"))
            userLogs.setRole(changes.get("isRoleUpdated"));

        userLogsRepository.save(userLogs);
    }
}
