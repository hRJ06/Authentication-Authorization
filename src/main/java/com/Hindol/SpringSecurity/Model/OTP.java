package com.Hindol.SpringSecurity.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
@Entity
@Data
@Table(name = "otp")
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String OTP;
    private String email;
    @CreationTimestamp
    private Timestamp createdDate;
    @UpdateTimestamp
    private Timestamp updatedDate;

}
