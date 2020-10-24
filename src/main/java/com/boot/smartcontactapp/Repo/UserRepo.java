package com.boot.smartcontactapp.Repo;

import com.boot.smartcontactapp.Entities.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserTable,Integer> {
}
