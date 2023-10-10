package com.Hindol.SpringSecurity.Repository;

import com.Hindol.SpringSecurity.Model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP,Integer> {
    @Query("SELECT o FROM OTP o WHERE o.email = :email ORDER BY o.createdDate DESC")
    Optional<OTP> findLatestByEmail(@Param("email") String email);
}
