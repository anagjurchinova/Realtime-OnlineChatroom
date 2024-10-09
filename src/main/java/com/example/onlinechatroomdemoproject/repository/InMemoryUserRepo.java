package com.example.onlinechatroomdemoproject.repository;

import com.example.onlinechatroomdemoproject.bootstrap.DataHolder;
import com.example.onlinechatroomdemoproject.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryUserRepo {

    public List<User> listAll(){
        return DataHolder.users;
    }

    public Optional<User> searchById(Long id){
        return DataHolder.users
                .stream()
                .filter(u -> u.getId() == id)
                .findFirst();
    }

    public Optional<User> searchByUsername(String username){
        return DataHolder.users
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public Optional<User> searchByEmail(String email){
        return DataHolder.users
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    public Optional<User> search(String usernameOrEmail, String password){
        return DataHolder.users
                .stream()
                .filter(u -> u.getUsername().equals(usernameOrEmail) || u.getEmail().equals(usernameOrEmail))
                .filter(u -> u.getPassword().equals(password))
                .findFirst();
    }

    // TODO: 15.7.2024 search for phone number

    public void addOrUpdate(User user){
        DataHolder.users.removeIf(u -> u.getId() == user.getId());
        DataHolder.users.add(user);
    }

    public void deleteById(Long id){
        DataHolder.users.removeIf(u -> u.getId() == id);
    }
}
