package dao;

import model.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final String FILE_PATH = "users.dat";

    public static void saveUsers(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<User> loadUsers() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static User getUserByUsername(String username) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public static boolean addUser(User user) {
        List<User> users = loadUsers();
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(user.getUsername())) {
                return false; // Username already exists
            }
        }
        users.add(user);
        saveUsers(users);
        return true;
    }

    public static void updateUser(User updatedUser) {
        List<User> users = loadUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equalsIgnoreCase(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                break;
            }
        }
        saveUsers(users);
    }
}