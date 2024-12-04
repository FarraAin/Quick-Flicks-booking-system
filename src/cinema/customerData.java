/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cinema;

import java.sql.Time;
import java.util.Date;

/**
 *
 * @author NFA20
 */
public class customerData {
    
    private int id;
    private String type;
    private String title;
    private int quantity;
    private double total;
    private Date date;
    private Time time;

    public customerData(int id, String type, String title, int quantity, double total, Date date, Time time) {
        this.id = id;
        this.type = type;
        this.quantity = quantity;
        this.total = total;
        this.date = date;
        this.time = time;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }
    
    public String getTitle() {
        return title;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return total;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    
    
    
}
