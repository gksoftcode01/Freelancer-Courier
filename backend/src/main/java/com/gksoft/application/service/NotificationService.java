package com.gksoft.application.service;

import com.gksoft.application.domain.*;
import com.gksoft.application.domain.enumeration.TargetScreen;
import com.gksoft.application.domain.enumeration.TargetStack;
import com.gksoft.application.repository.NotificationRepository;
import com.gksoft.application.web.websocket.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class NotificationService {
    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private static final String ENTITY_NAME = "Notification";

    private final NotificationRepository notificationRepository;
    private final EntityManager entityManager;

    private final ActivityService activityService;
    public NotificationService(NotificationRepository notificationRepository, EntityManager entityManager, ActivityService activityService) {
        this.notificationRepository = notificationRepository;
        this.entityManager = entityManager;
        this.activityService = activityService;
    }

    public  void updateSeen(List<Notification> list){
     entityManager.createQuery("update Notification n set n.seen = 1 where n in ?1").setParameter(1,list).executeUpdate();

    }
    public void newAskNotify(Flight flight, Ask ask){
        log.debug("Request to save flight notify ask  : {}", ask);

        Notification notification = new Notification();
        notification.setMsg(
            "I have sent you an ask for your flight No :" + flight.getId());
        notification.setTargetScreen(TargetScreen.FlightDetail);
        notification.setTargetStack(TargetStack.Flights);
        notification.setTargetId(flight.getId());
        notification.setTargetUser(flight.getCreateBy());
        notification.setFromUser(ask.getFromUser());
        notification.setCreateDate(Instant.now());
        notification.setSeen(0);
        notificationRepository.save(notification);
        activityService.notify(flight.getCreateBy().getLogin());
    }
    public void newBidNotify(CargoRequest cargoRequest , Bid bid){
        log.debug("Request to save cargoRequest notify bid  : {}", bid);

        Notification notification = new Notification();
        notification.setMsg(
            "I have sent you a bid for your cargoRequest number " + cargoRequest.getId());
        notification.setTargetScreen(TargetScreen.CargoRequestDetail);
        notification.setTargetStack(TargetStack.Cargo);
        notification.setFromUser(bid.getFromUser());
        notification.setTargetId(cargoRequest.getId());
        notification.setTargetUser(cargoRequest.getCreateBy());
        notification.setCreateDate(Instant.now());
        notification.setSeen(0);
        notificationRepository.save(notification);
        activityService.notify(cargoRequest.getCreateBy().getLogin());

    }
    public void bidStatusNotify( CargoRequest cargoRequest ,Bid bid){
        log.debug("Request to save cargoRequest notify bid  : {}", bid);

        Notification notification = new Notification();
        notification.setMsg(
            "I have updated your bid status to " + bid.getStatus().toString());
        notification.setTargetScreen(TargetScreen.CargoRequestDetail);
        notification.setTargetStack(TargetStack.Cargo);
        notification.setTargetId(cargoRequest.getId());
        notification.setTargetUser(bid.getFromUser());
        notification.setCreateDate(Instant.now());
        notification.setFromUser(cargoRequest.getCreateBy());
        notification.setSeen(0);
        notificationRepository.save(notification);
        activityService.notify(bid.getFromUser().getLogin());
    }
}
