package RealEstate.useCases;

import LoginSystem.entities.User;
import LoginSystem.entities.UserContainer;
import LoginSystem.exceptions.UserNotFoundException;
import LoginSystem.exceptions.UsernameAlreadyExistsException;
import LoginSystem.useCases.CreateUser;
import RealEstate.entities.Buyer;
import RealEstate.entities.Seller;
import RealEstate.entities.UserType;

import java.util.HashMap;

public class UserFactory {
    private final CreateUser createUser;
    private final CreateBuyer createBuyer;
    private final CreateSeller createSeller;
    private final UserContainer<String, User> userContainer;

    /**
     * Factory for creating users using default functions
     *
     * @param userContainer is a hash map that relates usernames to a User objects
     */
    public UserFactory(UserContainer<String, User> userContainer) {
        this.userContainer = userContainer;

        this.createUser = new CreateUser(userContainer);
        this.createBuyer = new CreateBuyer(userContainer);
        this.createSeller = new CreateSeller(userContainer);
    }

    /**
     * Factory for creating users
     *
     * @param createUser    use case for creating an Admin
     * @param createBuyer   use case for creating a Buyer
     * @param createSeller  use case for creating a Seller
     * @param userContainer is a hash map that relates usernames to a User objects
     */
    public UserFactory(CreateUser createUser, CreateBuyer createBuyer, CreateSeller createSeller,
                       UserContainer<String, User> userContainer) {
        this.createUser = createUser;
        this.createBuyer = createBuyer;
        this.createSeller = createSeller;
        this.userContainer = userContainer;
    }

    /**
     * Checks whether a given username already is associated with a User object
     *
     * @param username to be queried
     * @return whether username already exists in userContainer
     */
    private boolean uniqueUsernameExists(String username) {
        try {
            userContainer.get(username);
            return false;
        } catch (UserNotFoundException e) {
            return true;
        }
    }

    /**
     * @param type     of user to be created
     * @param username of the user to be created
     * @param password of the user to be created
     */
    public void createUser(String type, String username, String password) {
        UserType userType = UserType.fromString(type);

        if (uniqueUsernameExists(username)) {
            switch (userType) {
                case ADMIN:
                    createUser.createAdminUser(username, password);
                    break;
                case BUYER:
                    createBuyer.createNewBuyer(username, password);
                    break;
                case SELLER:
                    createSeller.createNewSeller(username, password);
                    break;
            }
        } else {
            throw new UsernameAlreadyExistsException();
        }
    }

    /**
     * Method to get the created buyers of createBuyer
     *
     * @return created buyers of createBuyer
     */
    public HashMap<String, Buyer> getCreatedBuyers() {
        return createBuyer.getCreatedBuyers();
    }

    /**
     * Method to get the created sellers of createSeller
     *
     * @return created sellers of createSeller
     */
    public HashMap<String, Seller> getCreatedSellers() {
        return createSeller.getCreatedSellers();
    }
}
