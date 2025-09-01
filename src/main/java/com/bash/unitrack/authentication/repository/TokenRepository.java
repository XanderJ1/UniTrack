package com.bash.unitrack.authentication.repository;

import com.bash.unitrack.authentication.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);
}
