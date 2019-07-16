package com.login.hofo;


public class Menu {

    private int id;
    private String name;
    private String description;
    private String price;
    private String photo;
    private String ingredientes;
    private String utencilios;
    private int user_id;

    private static int currentModality;

    public Menu() {
    }

    public Menu(int id, String name, String description, String price, String photo, String ingredientes, String utencilios, int user_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.photo = photo;
        this.ingredientes = ingredientes;
        this.utencilios = utencilios;
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getUtencilios() {
        return utencilios;
    }

    public void setUtencilios(String utencilios) {
        this.utencilios = utencilios;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public static int getCurrentModality() {
        return currentModality;
    }

    public static void setCurrentModality(int currentModality) {
        Menu.currentModality = currentModality;
    }
}
