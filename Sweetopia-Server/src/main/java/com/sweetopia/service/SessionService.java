package com.sweetopia.service;

import com.sweetopia.entity.User;
import com.sweetopia.exception.SessionsException;

public interface SessionService {
    public boolean isSessionValid(String uuid) throws SessionsException;
    public String createSession(User user);
    public boolean deleteSession(String uuid) throws SessionsException;
    public User getUserByUUID(String uuid)throws SessionsException;
    public boolean isAdmin(String uuid)throws SessionsException;
    public boolean isCustomer(String uuid)throws SessionsException;
}
