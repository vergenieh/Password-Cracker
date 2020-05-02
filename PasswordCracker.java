import java.util.ArrayList;

public class PasswordCracker {
    // receives list of passwords and populates database with entries consisting
    // of (key,value) pairs where the value is the password and the key is the
    // encrypted password (encrypted using Sha1)
    // in addition to passwords in commonPasswords list, this class is
    // responsible to add mutated passwords according to rules 1-5.
    public void createDatabase(ArrayList<String> commonPasswords, DatabaseInterface database) {
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<String> passwordListRule5 = new ArrayList<>();
        /**
         * On crée une liste temporaire qui contient tous les passwords initialement
         * On appelle les méthodes de chaque rule pour ajouter les passwords générés à cette liste
         */
        for (int i = 0; i < commonPasswords.size(); i++) {
            temp.add(commonPasswords.get(i));
        }

        for(int i = 0; i < temp.size(); i++) {
            String s = temp.get(i);
            passwordListRule5.addAll(rule5(s));
        }
        temp.addAll(passwordListRule5);

        ArrayList<String> passwordListRule4 = new ArrayList<>();
        for (int i = 0; i < temp.size(); i++) {
            String s = temp.get(i);
            passwordListRule4.addAll(rule4(s));
        }
        temp.addAll(passwordListRule4);

        ArrayList<String> passwordListRule3 = new ArrayList<>();
        for(int i = 0; i < temp.size(); i++) {
            String s = temp.get(i);
            passwordListRule3.addAll(rule3(s));
        }
        temp.addAll(passwordListRule3);

        ArrayList<String> passwordListRule2 = rule2(temp);
        for(int i = 0; i < passwordListRule2.size(); i++) {
            temp.add(passwordListRule2.get(i));
        }

        ArrayList<String> passwordListRule1 = rule1(temp);
        for(int i = 0; i < passwordListRule1.size(); i++) {
            temp.add(passwordListRule1.get(i));
        }

        /**
         * Ici, on ajoute tout de la liste dans le database et on génère aussi un encryptedPassword pour le key
         */
        for(int i = 0; i < temp.size(); i++) {
            String value = temp.get(i);
            String key = "";
            try {
                 key = Sha1.hash(temp.get(i));
            } catch (Exception e) {
                System.out.println("Could not hash key.");
            }
            database.save(value, key);
        }
    }

    public static ArrayList rule2(ArrayList<String> temp) {
        ArrayList<String> list = new ArrayList<>();
        String year = "2018";
        for(int i = 0; i < temp.size(); i++) {
            String newPassword = temp.get(i)+year;
            list.add(newPassword);
        }
        return list;
    }

    public static ArrayList rule1(ArrayList<String> temp) {
        ArrayList<String> list = new ArrayList<>(); 
        for(int i = 0; i < temp.size(); i++) {
            char [] s = temp.get(i).toCharArray();
            if(Character.isLetter(s[0])) {
                list.add(temp.get(i).substring(0,1).toUpperCase()+temp.get(i).substring(1));
            }
        }
        return list;
    }

    public static ArrayList charCounter(String temp, char check) {
        ArrayList<Integer> indexes = new ArrayList<>();
        char [] s = temp.toCharArray();
        for (int j = 0; j < s.length; j++) {
            if (s[j] == check) {
                 indexes.add(j);
            }
        }
        return indexes;
    }

    public static String charChange(String currentPassword, int index, char change) {
        char [] s = currentPassword.toCharArray();
        s[index] = change;
        return new String(s);
    }

    public static ArrayList rule3(String temp) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<Integer> indexes = charCounter(temp, 'a');
        for (int i = 0; i < indexes.size(); i++) {
            String currentPassword = temp;
            for (int j = 0; j < i; j++) {
                currentPassword = charChange(currentPassword, indexes.get(j), '@');
            }
            for (int j = i; j < indexes.size(); j++) {
                list.add(charChange(currentPassword, indexes.get(j), '@'));
            }
        }
        return list;
    }
    public static ArrayList rule4(String temp) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<Integer> indexes = charCounter(temp, 'e');
        for (int i = 0; i < indexes.size(); i++) {
            String currentPassword = temp;
            for (int j = 0; j < i; j++) {
                currentPassword = charChange(currentPassword, indexes.get(j), '3');
            }
            for (int j = i; j < indexes.size(); j++) {
                list.add(charChange(currentPassword, indexes.get(j), '3'));
            }
        }
        return list;
    }

    
    public static ArrayList rule5(String temp) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<Integer> indexes = charCounter(temp, 'i');
        for (int i = 0; i < indexes.size(); i++) {
            String currentPassword = temp;
            for (int j = 0; j < i; j++) {
                currentPassword = charChange(currentPassword, indexes.get(j), '1');
            }
            for (int j = i; j < indexes.size(); j++) {
                list.add(charChange(currentPassword, indexes.get(j), '1'));
            }
        }
        return list;
    }
    
    String crackPassword(String encryptedPassword, DatabaseInterface database) {
    //uses database to crack encrypted password, returning the original password 
        return database.decrypt(encryptedPassword);
    }
}