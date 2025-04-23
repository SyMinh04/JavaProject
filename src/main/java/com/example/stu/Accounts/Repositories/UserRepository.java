package com.example.stu.Accounts.Repositories;

import com.example.stu.Accounts.Models.User;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CassandraRepository<User, UUID> {
    @Query("SELECT * FROM users WHERE gmail = ?0 ALLOW FILTERING")
    Optional<User> findByGmail(String gmail);
}
