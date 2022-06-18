package useCases;

import Entities.AdminUser;
import Entities.NonAdminUser;
import Entities.User;
import Entities.UserNameAndPasswordContainer;
import Exceptions.UserNotFoundException;
import Exceptions.UsernameAlreadyExistsException;

public class CreateUser {
    private UserNameAndPasswordContainer<String, User> interface_users;

    public CreateUser(UserNameAndPasswordContainer<String, User> interface_users) {
        this.interface_users = interface_users;
    }

    public void createNonAdminUser(String username, String password) {
        if(UsernameuniqueExists(username)){
            User user = new NonAdminUser(username, password);
            interface_users.put(username, user);
        }else{
            throw new UsernameAlreadyExistsException();
        }
    }

    public void createAdminUser(String username, String password) {
        if(UsernameuniqueExists(username)){
            User user = new AdminUser(username, password);
            interface_users.put(username, user);
        }else{
            throw new UsernameAlreadyExistsException();
        }
    }

    public boolean UsernameuniqueExists(String username) {
        try {
            User u = interface_users.get(username);
            return false;
        } catch (UserNotFoundException e) {
            return true;
        }
    }
}
