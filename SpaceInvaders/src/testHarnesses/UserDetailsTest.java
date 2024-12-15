package testHarnesses;

import model.UserDetails;

/**
 * <p>
 * Title:UserDetailsTest</p>
 * <p>
 * Description: UserDetails Class Test</p>
 * <p>
 * Copyright: Copyright (c) 2006</p>
 * <p>
 * Company: TAFE SA</p>
 *
 * @author Travis Griggs 19/08/2024
 * @version 2.0

 */

public class UserDetailsTest {

    public static void main(String[] args) {
        // Testing all-arg constructor UserDetails(String userName, String password, boolean wantingIntroInfo)
        System.out.println("Testing all-arg constructor UserDetails(String userName, String password, boolean wantingIntroInfo), expected outcome: userName='Alice', password='password123', wantingIntroInfo=false");
        UserDetails user1 = new UserDetails("Alice", "password123", false);
        System.out.println("Actual outcome: " + user1 +"\n");

        // Testing other-arg constructor UserDetails(String userName, String password)
        System.out.println("Testing other-arg constructor UserDetails(String userName, String password), expected outcome: userName='Bob', password='cat123', wantingIntroInfo=true");
        UserDetails user2 = new UserDetails("Bob", "cat123");
        System.out.println("Actual outcome: " + user2 + "\n");

        // Testing other-arg constructor UserDetails(String userName)
        System.out.println("Testing other-arg constructor UserDetails(String userName), expected outcome: userName='Charlie', password=null, wantingIntroInfo=true");
        UserDetails user3 = new UserDetails("Charlie");
        System.out.println("Actual outcome: " + user3 + "\n");

        // Testing no-arg constructor UserDetails()
        System.out.println("Testing no-arg constructor UserDetails(), expected outcome: userName='NO NAME', password=null, wantingIntroInfo=true");
        UserDetails user4 = new UserDetails();
        System.out.println("Actual outcome: " + user4 + "\n");

        // Testing setters and getters
        System.out.println("Testing setters and getters, expected outcome: userName='David', password='Origin123', wantingIntroInfo=false");
        user4.setUserName("David");
        user4.setPassword("Origin123");
        user4.setWantingIntroInfo(false);
        System.out.println("Actual outcome" + "\n" + "userName: " + user4.getUserName() + ", password: " + user4.getPassword() + ", wantingIntroInfo: " + user4.isWantingIntroInfo()+ "\n");

        // Testing toString()
        System.out.println("Testing toString(), expected outcome: UserDetails{userName='David', password='Origin123', wantingIntroInfo=false}");
        System.out.println(user4.toString()+ "\n");        
        
        // Testing equals method
        System.out.println("Testing equals method, expected outcome: user1.equals(user5) == true, user1.equals(user3) == false");
        UserDetails user5 = new UserDetails("Alice", "Unique123", true);
        System.out.println("Actual outcome" + "\n" + "user1.equals(user5): " + user1.equals(user5)); // should be true
        System.out.println("user1.equals(user3): " + user1.equals(user3)); // should be false
    }
}
