package com.example.shop.repository;

import com.example.shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByOpenid(String openid);

    User findByEmail(String email);

    User findOneById(Long id);

    User findOneByUnifyUid(Long uuid);
}
