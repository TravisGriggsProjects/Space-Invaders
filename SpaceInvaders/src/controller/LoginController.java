package controller;

import dao.GameSettingsDAOText;
import dao.UserDetailsDAOText;
import model.GameSettings;
import model.UserDetails;
import view.JDialogLogin;

/**
 * Handles login logic
 *
 * @author Santi Ruiz
 */
public class LoginController {

    private UserDetails user;
//  private Connection con;
    private JDialogLogin dlgLogin;
    private boolean databaseError = false;
    private boolean successfulLogin = false;
    private boolean loginCancelled = false;

    /**
     * Constructs a new object
     *
     * @param user This UserDetails object will be populated with logged in user's details when log in is successful
     * @param dlg The dialogue (view) this controller works with
     */
    public LoginController(UserDetails user, JDialogLogin dlg) {
        this.user = user;
        this.dlgLogin = dlg;
    }

    /**
     * Logs user in. There are four possible outcomes
     *
     * 1. User exists and succesfully logs in 2. User is new so is added to the database and successfully logs in 3. The
     * login is cancelled. 4. A critical database error occurs
     *
     * This method makes use of methods verifyLogin for existing users and createNewUser for new users
     */
    public void login() {

        do {
            // reset error
            databaseError = false;

            // show dialog
            dlgLogin.setVisible(true);

            // lets see what the user did
            if (dlgLogin.isLoginCancelled()) {
                loginCancelled = true;
            } else if (dlgLogin.getPlayerType() == JDialogLogin.NEW) {
                createNewUser();
            } else {
                verifyLogin();
            }
        } while (!loginCancelled && !successfulLogin && !databaseError);
    }

    /**
     * handles case when user indicates they are an existing user
     */
    private void verifyLogin() {
        // try to log in
        try {
            user.setUserName(dlgLogin.getUsername());
            user.loadData();
            // If the password returned from the database is null it means the use
            // does not exist
            if (user.getPassword() == null) {
                successfulLogin = false;
                dlgLogin.displayUserDoesNotExistError();
            } else {
                successfulLogin = user.getPassword().equals(dlgLogin.getPassword());
                if (!successfulLogin) {
                    dlgLogin.displayWrongPasswordError();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            databaseError = true;
            dlgLogin.displayDatabaseError();
        }

    }

    /**
     * handles case when user indicates they are a new user
     */
    private void createNewUser() {
        try {
            user.setUserName(dlgLogin.getUsername());
            // First check if the user already exists by trying to load an existing
            // password. Of all is right )ie the user doe not already exist)
            // the password should be null.
            user.loadData();
            //UserDetailsDAO.loadData(user);
            if (user.getPassword() != null) {
                successfulLogin = false;
                dlgLogin.displayUserAlreadyExistsError();
            } else {
                user.setPassword(dlgLogin.getPassword());
                UserDetailsDAOText.saveData(user);
                GameSettingsDAOText.saveData(user, new GameSettings());
                successfulLogin = true;
            }
        } catch (Exception ex) {
            databaseError = true;
            dlgLogin.displayDatabaseError();
        }
    }

    /**
     * Client may be interested to know whether an SQL error occurred creating a new user
     *
     * @return true if SQL error did occur otherwise false
     */
    public boolean isDatabaseError() {
        return databaseError;
    }

    /**
     *
     * @return true if login was successful
     */
    public boolean isSuccessfulLogin() {
        return successfulLogin;
    }

    /**
     *
     * @return if the login was cancelled
     */
    public boolean isLoginCancelled() {
        return loginCancelled;
    }

    public UserDetails getUser() {
        return user;
    }
}
