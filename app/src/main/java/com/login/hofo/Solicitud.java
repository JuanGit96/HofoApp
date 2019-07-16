package com.login.hofo;

public class Solicitud {

    private String diner_name, hour, address, city, phone, date,  ingredients, final_comment,
    utensils, additional_comments;

    private int id, amount_people, qualification, menu_id, diner_id;

    public Solicitud(String diner_name, String hour, String address, String city, String phone,
                     String date, String ingredients, String final_comment, String utensils,
                     String additional_comments, int amount_people, int qualification, int menu_id,
                     int diner_id, int id) {
        this.diner_name = diner_name;
        this.hour = hour;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.date = date;
        this.ingredients = ingredients;
        this.final_comment = final_comment;
        this.utensils = utensils;
        this.additional_comments = additional_comments;
        this.amount_people = amount_people;
        this.qualification = qualification;
        this.menu_id = menu_id;
        this.diner_id = diner_id;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiner_name() {
        return diner_name;
    }

    public void setDiner_name(String diner_name) {
        this.diner_name = diner_name;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getFinal_comment() {
        return final_comment;
    }

    public void setFinal_comment(String final_comment) {
        this.final_comment = final_comment;
    }

    public String getUtensils() {
        return utensils;
    }

    public void setUtensils(String utensils) {
        this.utensils = utensils;
    }

    public String getAdditional_comments() {
        return additional_comments;
    }

    public void setAdditional_comments(String additional_comments) {
        this.additional_comments = additional_comments;
    }

    public int getAmount_people() {
        return amount_people;
    }

    public void setAmount_people(int amount_people) {
        this.amount_people = amount_people;
    }

    public int getQualification() {
        return qualification;
    }

    public void setQualification(int qualification) {
        this.qualification = qualification;
    }

    public int getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(int menu_id) {
        this.menu_id = menu_id;
    }

    public int getDiner_id() {
        return diner_id;
    }

    public void setDiner_id(int diner_id) {
        this.diner_id = diner_id;
    }
}
