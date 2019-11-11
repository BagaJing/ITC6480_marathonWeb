package com.jing.blogs.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "t_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    private String orderId;
    private String customerName;
    private Date startDate;
    private Date endDate;
    private String emailAddress;
    @ManyToOne
    private User coach;
    @ManyToOne
    private Trainning selectedTrain;

    public Long getId() { return Id; }

    public void setId(Long id) { Id = id; }

    public String getOrderId() { return orderId; }

    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Trainning getSelectedTrain() { return selectedTrain; }

    public void setSelectedTrain(Trainning selectedTrain) { this.selectedTrain = selectedTrain; }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public User getCoach() {
        return coach;
    }

    public void setCoach(User coach) {
        this.coach = coach;
    }

}
