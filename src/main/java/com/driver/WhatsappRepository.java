package com.driver;

import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WhatsappRepository {
    //User Database
    Map<String, User> userDb = new HashMap<>();
    Map<User, Group> groupDb = new HashMap<>();

    Map<Integer, Message> messages = new HashMap<>();

    Map<User,List<Message>> userMessagesDb  = new HashMap<>();

    Map<Group,List<Message>> groupMessagesDb = new HashMap<>();

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
            groupDb.put(users.get(0),group);
        }
        if(users.size()>2){
            String name = "Group "+group.getCount()+1;
            group.setName(name);
            group.setCount(group.getCount()+1);
            group.setAdmin(users.get(0));
            group.setNumberOfParticipants(users.size());
            group.setUsers(users);
            groupDb.put(users.get(0),group);
        }
        return group;
    }

    public int createMessage(String content) {
        Message msg = new Message();
        msg.setId(msg.getId()+1);
        msg.setContent(content);
        Date currDate = new Date();
        msg.setTimestamp(currDate);
        messages.put(msg.getId(), msg);
        return msg.getId();
    }

    public int sendMessgae(Message message, User sender, Group group) {
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "You are not allowed to send message" if the sender is not a member of the group
        //If the message is sent successfully, return the final number of messages in that group.
        if(!groupDb.containsValue(group)){
            throw new RuntimeException("Group does not exist");
        }
        List<User> users = group.getUsers();
        if(!users.contains(sender)){
            throw new RuntimeException("You are not allowed to send message");
        }
        List<Message> messageList = groupMessagesDb.get(group);
        messageList.add(message);
        groupMessagesDb.put(group,messageList);
        List<Message> usermsg =  userMessagesDb.get(sender);
        usermsg.add(message);
        userMessagesDb.put(sender,usermsg);
        return groupMessagesDb.get(group).size();
    }

    public String changeAdmin(User approver, User user, Group group) {
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "Approver does not have rights" if the approver is not the current admin of the group
        //Throw "User is not a participant" if the user is not a part of the group
        //Change the admin of the group to "user" and return "SUCCESS". Note that at one time there is only one admin and the admin rights are transferred from approver to user.

        if(!groupDb.containsKey(group)){
            throw new RuntimeException("Group does not exist");
        }
        List<User> users = group.getUsers();
        if(!groupDb.containsKey(approver)){
            throw new RuntimeException("Approver does not have rights");
        }
        if(!users.contains(user)){
            throw new RuntimeException("User is not a participant");
        }
        Group grp = groupDb.get(approver);
        groupDb.remove(approver);
        groupDb.put(user,grp);
        return "SUCCESS";

    }

    public int removeUser(User user) {
        //A user belongs to exactly one group
        //If user is not found in any group, throw "User not found" exception
        //If user is found in a group and it is the admin, throw "Cannot remove admin" exception
        //If user is not the admin, remove the user from the group, remove all its messages from all the databases, and update relevant attributes accordingly.
        //If user is removed successfully, return (the updated number of users in the group + the updated number of messages in group + the updated number of overall messages)
        int res = 0;
        Group group = new Group();
        for (Map.Entry<User,Group> set: groupDb.entrySet() ) {
           if(!set.getValue().getUsers().contains(user)){
               throw new RuntimeException("User not found");
           }if(set.getKey()==user){
               throw new RuntimeException("Cannot remove admin");
            }
           // Removing User and Updating DB
            User us = user;
            group = set.getValue();
            set.getValue().getUsers().remove(user);
            groupDb.put(us,set.getValue());
            res = groupDb.get(user).getUsers().size();
        }
        // Removing Msgs of User and Updating Attribute
        int size = userMessagesDb.get(user).size();
        List<Message> msgs = userMessagesDb.get(user);
        for (Message msg :msgs ) {
            messages.remove(msg.getId());
            groupMessagesDb.get(group).remove(msg.getId());
        }
        res+=groupMessagesDb.get(group).size()+messages.size();
    return res;
    }
}
