package com.project.moneymanager.models;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Table(name = "plans")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size.List ({
            @Size(min=3, message="The description must be at least {min} characters"),
            @Size(max=30, message="The description must be less than {max} characters")
    })
    private String name;
    @Min(value = 1,message = "The amount can't be a negative and zero")
    private Integer limitz;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date start_datez;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date end_datez;
    @Column(updatable = false)
    private Date createdAt;
    private Date updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "plan", fetch = FetchType.LAZY)
    private List<Expense> expenses;

    public Plan(String name, Integer limitz, Date start_datez, Date end_datez) {
        this.name = name;
        this.limitz = limitz;
        this.start_datez = start_datez;
        this.end_datez = end_datez;
    }

    public Plan() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLimitz() {
        return limitz;
    }

    public void setLimitz(Integer limitz) {
        this.limitz = limitz;
    }

    public Date getStart_datez() {
        return start_datez;
    }

    public void setStart_datez(Date start_datez) {
        this.start_datez = start_datez;
    }

    public Date getEnd_datez() {
        return end_datez;
    }

    public void setEnd_datez(Date end_datez) {
        this.end_datez = end_datez;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
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