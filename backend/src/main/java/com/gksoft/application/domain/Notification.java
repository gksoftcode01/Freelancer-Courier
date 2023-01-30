package com.gksoft.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gksoft.application.domain.enumeration.FlightStatus;
import com.gksoft.application.domain.enumeration.TargetScreen;
import com.gksoft.application.domain.enumeration.TargetStack;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "Notification")
public class Notification {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "msg")
    private String msg;

    @Enumerated(EnumType.STRING)
    @Column(name = "targetScreen")
    private TargetScreen targetScreen;

    @Enumerated(EnumType.STRING)
    @Column(name = "targetStack")
    private TargetStack targetStack;

    @Column(name = "target_id")
    private Long targetId;

    @ManyToOne
    @JsonIgnoreProperties(value = {"authorities", "userRates", "country", "stateProvince", "city" }, allowSetters = true)
    private User targetUser;
    @ManyToOne
    @JsonIgnoreProperties(value = {"authorities", "userRates", "country", "stateProvince", "city" }, allowSetters = true)
    private User fromUser;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name="seen")
     private  int seen;

    public TargetStack getTargetStack() {
        return targetStack;
    }

    public void setTargetStack(TargetStack targetStack) {
        this.targetStack = targetStack;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    public Notification targetUser(User User) {
        this.setTargetUser(User);
        return this;
    }

    public Notification fromUser(User User) {
        this.setFromUser(User);
        return this;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public TargetScreen getTargetScreen() {
        return targetScreen;
    }

    public void setTargetScreen(TargetScreen targetScreen) {
        this.targetScreen = targetScreen;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}
