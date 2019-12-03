
        package com.jing.blogs.domain;
        import javax.persistence.*;
        import javax.validation.constraints.NotBlank;
        import javax.validation.constraints.NotNull;
        import java.util.ArrayList;
        import java.util.List;

@Entity
@Table(name = "t_training")
public class Trainning {
    private int sideleft;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private double price;
    @NotNull
    @ManyToOne
    private User coach;
    private int durations; //count with day unit


    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String description;  //the detail description about the training
    private int ordered; //count the times the trainning has been ordered
    @OneToMany(mappedBy = "selectedTrain")
    private List<Order> orders = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDurations() { return durations; }

    public void setDurations(int durations) { this.durations = durations; }

    public User getCoach() {
        return coach;
    }

    public void setCoach(User coach) {
        this.coach = coach;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrdered() {
        return ordered;
    }

    public void setOrdered(int ordered) {
        this.ordered = ordered;
    }

    public List<Order> getOrders() { return orders; }

    public void setOrders(List<Order> orders) { this.orders = orders; }

    public int getSideleft() {
        return sideleft;
    }

    public void setSideleft(int sideleft) {
        this.sideleft = sideleft;
    }
}



