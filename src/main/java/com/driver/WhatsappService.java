package com.driver;

import com.driver.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WhatsappService {
    WhatsappRepository whatsappRepository  = new WhatsappRepository();
    public Boolean createUser(String name, String mobile) {
        if(whatsappRepository.createUser(name, mobile)){
            return true;
        }
        return false;
    }

    public Group createGroup(List<User> users) {
       return whatsappRepository.createGroup(users);
    }

    public int createMessage(String content) {

    }
//
//    public int sendMessage(Message message, User sender, Group group) {
//    }
//
//    public String changeAdmin(User approver, User user, Group group) {
//    }
//
//    public int removeUser(User user) {
//    }
//
//    public String findMessage(Date start, Date end, int k) {
//    }
}
