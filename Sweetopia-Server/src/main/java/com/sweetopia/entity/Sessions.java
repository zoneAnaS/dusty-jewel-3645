package com.sweetopia.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Sessions {
    @Id
    private String session_id;
    @OneToOne
    @JoinColumn(name="user_id")
    private User user;
    private LocalDateTime session_time=LocalDateTime.now();
}
