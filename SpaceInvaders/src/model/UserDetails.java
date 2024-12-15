package model;


import dao.UserDetailsDAOText;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * <p>
 * Title:UserDetails</p>
 * <p>
 * Description: UserDetails Class</p>
 * <p>
 * Copyright: Copyright (c) 2006</p>
 * <p>
 * Company: TAFE SA</p>
 *
 * @author Santi Ruiz
 * @author Julie Ruiz 
 * @author Travis Griggs 19/08/2024
 * @version 2.0

 */
public class UserDetails implements Serializable {

    // Variable declarations
        private String userName;
        private String password;
        private boolean wantingIntroInfo;

    // Default values
        private static final String DEFAULT_USERNAME = "NO NAME";
        private static final String DEFAULT_PASSWORD = null;
        private static final boolean DEFAULT_INFO = true;
    
    // Constructors

    /**
     *All-Arg Constructor
     * @param userName
     * @param password
     * @param wantingIntroInfo
     */
    public UserDetails(String userName, String password, boolean wantingIntroInfo) {
        this.userName = userName;
        this.password = password;
        this.wantingIntroInfo = wantingIntroInfo;
    }
    
    /**
     *2-Arg Constructor
     * @param userName
     * @param password
     */
    public UserDetails(String userName, String password) {
        this(userName, password, DEFAULT_INFO);
    }

    /**
     *1-Arg Constructor
     * @param userName
     */
    public UserDetails(String userName) {
        this(userName, DEFAULT_PASSWORD, DEFAULT_INFO);
    }

    /**
     *No-Arg Constructor
     */
    public UserDetails() {
        this(DEFAULT_USERNAME, DEFAULT_PASSWORD, DEFAULT_INFO);
    }

    // Getters

    /**
     *UserName getter
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     *Password getter
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     *WantingIntroInfo getter
     * @return wantingIntroInfo
     */
    public boolean isWantingIntroInfo() {
        return wantingIntroInfo;
    }

    // Setters

    /**
     *UserName setter
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *Password setter
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *WantingIntroInfo setter
     * @param wantingIntroInfo
     */
    public void setWantingIntroInfo(boolean wantingIntroInfo) {
        this.wantingIntroInfo = wantingIntroInfo;
    }

    // toString method

    /**
     *ToString method
     * @return UserDetials as String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("UserDetails{");
        sb.append("userName='");
        sb.append(userName).append('\'');
        sb.append(", password='");
        sb.append(password).append('\'');
        sb.append(", wantingIntroInfo=");
        sb.append(wantingIntroInfo);
        sb.append('}');
        return sb.toString();
    }
    
    // equals method

    /**
     *Equals method
     * @param obj
     * @return true if objects are equal, otherwise return false
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserDetails)) {
            return false;
        }
        UserDetails other = (UserDetails) obj;
        return other.getUserName().equals(this.getUserName());  
    }

    /**
     * saveData
     *
     * @param user UserDetails
     * @param con Connection
     * @throws SQLException
     */
    public void saveData() throws Exception {
        UserDetailsDAOText.saveData(this);
    }

    /**
     * loadData
     *
     * @param con Connection
     * @throws SQLException
     */
    public void loadData() throws Exception {
        UserDetailsDAOText.loadData(this);
    }

}
