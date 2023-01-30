package com.gksoft.application.repository;

import com.gksoft.application.domain.Notification;
import com.gksoft.application.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

     Page<Notification> findAllByTargetUserId(Long targetUserId , Pageable pageable);

 }
