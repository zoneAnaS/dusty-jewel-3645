package com.sweetopia.service.implementation;

import com.sweetopia.entity.Role;
import com.sweetopia.entity.Sessions;
import com.sweetopia.entity.User;
import com.sweetopia.exception.SessionsException;
import com.sweetopia.repository.SessionRepository;
import com.sweetopia.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionServiceImpl implements SessionService {
    @Autowired
    private SessionRepository sessionRepository;
    @Override
    public boolean isSessionValid(String uuid) throws SessionsException {
        Optional<Sessions> sessions=sessionRepository.findById(uuid);
        if(sessions.isEmpty())throw new SessionsException("No session found");
        Sessions sessions1=sessions.get();
        if(sessions1.getSession_time().plusHours(8).isBefore(LocalDateTime.now())){
            sessionRepository.deleteById(uuid);
            throw new SessionsException("Session expired");
        }
        return true;
    }

    @Override
    public String createSession(User user) {
        String uuid= UUID.randomUUID().toString().replaceAll("-","").substring(0,7);
        Sessions sessions=new Sessions();
        sessions.setSession_id(uuid);
        sessions.setUser(user);
        sessionRepository.save(sessions);
        return uuid;
    }

    @Override
    public boolean deleteSession(String uuid) throws SessionsException {
        Optional<Sessions> sessions=sessionRepository.findById(uuid);
        if(sessions.isEmpty())throw new SessionsException("No session found");
        sessionRepository.deleteById(uuid);
        return true;
    }

    @Override
    public User getUserByUUID(String uuid) throws SessionsException {
        this.isSessionValid(uuid);
        Optional<Sessions> sessions=sessionRepository.findById(uuid);
        if(sessions.get().getUser()==null)throw new SessionsException("User not found");
        return sessions.get().getUser();
    }

    @Override
    public boolean isAdmin(String uuid) throws SessionsException {
        User user = this.getUserByUUID(uuid);
        Role role= Role.Admin;
        if(user.getRole().equals(role))return true;
        return false;
    }

    @Override
    public boolean isCustomer(String uuid) throws SessionsException {
        User user = this.getUserByUUID(uuid);
        Role role= Role.Customer;
        if(user.getRole().equals(role))return true;
        return false;
    }
}
