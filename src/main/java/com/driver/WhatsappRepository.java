package com.driver;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class WhatsappRepository {
    //User Database
    Map<String, User> userDb = new HashMap<>();

    public boolean createUser(String name, String mobileNo){
        if(!userDb.containsKey(mobileNo)){
            userDb.put(mobileNo, new User(name, mobileNo));
            return true;
        }
        return false;
    }

}
