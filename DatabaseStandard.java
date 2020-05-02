import java.security.Key;
import java.util.HashMap;

public class DatabaseStandard implements DatabaseInterface {
    private HashMap<String,String> map;

    // this constructor must create the initial hash map
    public DatabaseStandard() {
        map = new HashMap<>();
    }
    
    // Stores plainPassword and corresponding encryptedPassword in a map.
    // if there was a value associated with this key, it is replaced, 
    // and previous value returned; otherwise, null is returned
    // The key is the encryptedPassword the value is the plainPassword

    public String save(String plainPassword, String encryptedPassword) {
        if (map.containsKey(encryptedPassword)) {
            String previousValue = map.get(encryptedPassword);
            map.replace(encryptedPassword, map.get(encryptedPassword), plainPassword);
            return previousValue;
        } else {
            map.put(encryptedPassword, plainPassword);
            return null;
        }    
    }

  // returns plain password corresponding to encrypted password
   public String decrypt(String encryptedPassword) {
       return map.get(encryptedPassword);
   }
   
   // returns the number of password pairs stored in the database
   public int size() {
       return map.size();
   }


           
       


   // print statistics based on type of Database
   public void printStatistics() {
       System.out.println("*** DatabaseStandard Statistics ***");
       System.out.println("Size is "+size()+" passwords");
       System.out.println("Initial Number of Indexes when Created "); //have to figure out what
       System.out.println("*** End DatabaseStandard Statistics ***");
   }
   
    }