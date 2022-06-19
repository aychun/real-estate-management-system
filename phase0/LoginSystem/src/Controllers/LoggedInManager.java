package Controllers;

import Exceptions.UserCannotBeBannedException;
import databaseManagers.UsernamePasswordFileManager;
import useCases.AuthenticateUser;
import useCases.RestrictUser;
import useCases.UpdateUserHistory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class LoggedInManager {

    private final InputHandler input;
    private final UserInterface ui;
    private final AuthenticateUser auth;
    private final UpdateUserHistory history;
    private final RestrictUser restrict;
    private final UserManager userManager;
    private final UsernamePasswordFileManager file;

    public LoggedInManager(InputHandler input, UserInterface ui, AuthenticateUser auth, UpdateUserHistory history,
                           RestrictUser restrict, UserManager userManager, UsernamePasswordFileManager file) {
        this.input = input;
        this.ui = ui;
        this.auth = auth;
        this.history = history;
        this.restrict = restrict;
        this.userManager = userManager;
        this.file = file;
    }

    public boolean userScreen(String username) {
        if (!auth.checkUserAdmin(username)) {
            return nonAdminScreen(username);
        } else {
            return adminScreen(username);
        }
    }

    public boolean nonAdminScreen(String username) {
        ui.printNonAdminLogInMenu();

        ArrayList<Integer> allowedInputs = new ArrayList<>();
        Collections.addAll(allowedInputs, 1, 2);

        int select = input.intInput(allowedInputs);
        switch (select) {
            case 1: {
                // View login history
                ArrayList<String> userHistory = history.getLoginHistory(username);
                ui.printLoginHistory(userHistory);
                break;
            }

            case 2: {
                // Logout user
                auth.logoutUser(username);
                ui.printLogOutSuccess();
                return false;
            }
        }

        return true;
    }

    public boolean adminScreen(String username) {
        ui.printAdminLoginMenu();

        ArrayList<Integer> allowedInputs = new ArrayList<>();
        Collections.addAll(allowedInputs, 1, 2, 3, 4, 5);

        int select = input.intInput(allowedInputs);
        switch (select) {
            case 1: {
                // View login history
                ArrayList<String> userHistory = history.getLoginHistory(username);
                ui.printLoginHistory(userHistory);
                break;
            }

            case 2: {
                // Create new admin user
                userManager.createNewUser(true);
                break;
            }

            case 3: {
                // Ban or unban user
                ui.printRestrictUsernameInput();
                String restrictUser = input.strInput();
                boolean isBanned = false;
                try {
                    isBanned = restrict.isUserBanned(restrictUser);
                } catch (UserCannotBeBannedException e) {
                    ui.printArbitraryException(e);
                }
                ui.printRestrictUserConfirmation(restrictUser, isBanned);
                ArrayList<String> allowedStrings = new ArrayList<>();
                Collections.addAll(allowedStrings, "Y", "N");
                String choice = input.strInput(allowedStrings);
                if (choice.equals("Y")) {
                    if (!isBanned) {
                        restrict.banNonAdminUser(restrictUser);
                    } else {
                        restrict.unbanNonAdminUser(restrictUser);
                    }
                }
                break;
            }

            case 4: {
                // Delete user
                ui.printDeleteUsernameInput();
                String deleteUser = input.strInput();
                boolean deleted = restrict.deleteNonAdminUser(deleteUser);
                if (deleted) {
                    try {
                        file.createUsernamePasswordFile();
                    } catch (IOException e) {
                        ui.printArbitraryException(e);
                    }

                    history.overwriteUserHistories();
                    ui.printDeleteUserSuccess(deleteUser);
                } else {
                    ui.printDeleteUserFail(deleteUser);
                }
                break;
            }

            case 5: {
                // Logout user
                auth.logoutUser(username);
                ui.printLogOutSuccess();
                return false;
            }
        }

        return true;
    }
}