package com.driver;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WhatsappRepository {
    //User Database
    Map<String, User> userDb = new HashMap<>();
    Map<User, Group> groupDb = new HashMap<>();

    public boolean createUser(String name, String mobileNo){
        if(!userDb.containsKey(mobileNo)){
            userDb.put(mobileNo, new User(name, mobileNo));
            return true;
        }
        return false;
    }

    public Group createGroup(List<User> users) {
        Group group =  new Group();
        if(users.size()==2){
            group.setName(users.get(1).getName());
            group.setAdmin(users.get(0));
            group.setNumberOfParticipants(users.size());
            group.setUsers(users);
            groupDb.put(users.get(0), group);
        }
        if(users.size()>2){
            String name = "Group "+group.getCount()+1;
            group.setName(name);
            group.setCount(group.getCount()+1);
            group.setAdmin(users.get(0));
            group.setNumberOfParticipants(users.size());
            group.setUsers(users);
            groupDb.put(users.get(0), group);
        }
        return group;
    }
}
