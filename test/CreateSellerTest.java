import entities.User;
import entities.containers.UserContainer;

import useCases.sellerUseCases.CreateSeller;
import org.junit.Test;
import static org.junit.Assert.*;


public class CreateSellerTest {
    @Test
    public void testCreatingABuyer() {
        UserContainer<String, User> uc = new UserContainer<>();

        CreateSeller createSeller = new CreateSeller(uc);
        createSeller.createNewSeller("TestSeller", "TestPW");

        assertFalse(createSeller.getUserContainer().isEmpty());
        assertFalse(createSeller.getCreatedSellers().isEmpty());
    }
}
