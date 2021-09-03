package com.project.moneymanager.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "balances")
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer val;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(updatable = false)
    private Date createdAt;
    private Date updatedAt;

    public Balance() {
    }

    public Balance(User user, Integer val) {
        this.val = val;
        this.user = user;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PostPersist
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

    @PostUpdate
    protected void postUpdate() {
        this.updatedAt = new Date();
    }
}