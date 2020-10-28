package com.boot.smartcontactapp.Repo;

import com.boot.smartcontactapp.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
}
