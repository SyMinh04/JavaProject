package com.example.stu.Accounts.Models;

import com.example.stu.Core.GenerateUid;
import com.example.stu.Core.Utils.HashUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Table("users")
public class User {
    @PrimaryKey
    private UUID uid = GenerateUid.generate();
    private String gmail;
    private String password;
    @Column("user_type")
    private String userType;


    public void makePassword(String plainPassword) {
        this.password = HashUtility.hashData(plainPassword);
    }

    public boolean comparePassword(String hashedPassword) {
        if(hashedPassword == null || hashedPassword.isEmpty()) return false;
        String hashedInput = HashUtility.hashData(hashedPassword);
        return this.password.equals(hashedInput);
    }


}
