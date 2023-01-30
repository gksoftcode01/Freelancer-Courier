package com.gksoft.application.web.rest;

import com.gksoft.application.domain.Notification;
import com.gksoft.application.domain.User;
import com.gksoft.application.repository.NotificationRepository;
import com.gksoft.application.service.NotificationService;
import com.gksoft.application.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NotificationResource {
    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

    private static final String ENTITY_NAME = "Notification";

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    private final NotificationService notificationService;
    public NotificationResource(NotificationRepository notificationRepository, UserService userService, NotificationService notificationService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @GetMapping("/notification")
    public ResponseEntity<List<Notification>> getAllCargoRequests(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable ){

        User user = userService.getCurrentUser().get();
        log.debug("get notification of user ",user.getLogin());
        Page<Notification> notificationPage = notificationRepository.findAllByTargetUserId(user.getId(), pageable);
        notificationService.updateSeen(notificationPage.getContent());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), notificationPage);
        return ResponseEntity.ok().headers(headers).body(notificationPage.getContent());
    }
}
