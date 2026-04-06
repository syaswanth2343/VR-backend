package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wishlists")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_type")
    private String itemType;

    @Column(name = "item_description", length = 1000)
    private String itemDescription;

    @Column(name = "item_image")
    private String itemImage;

    private String priority;

    public Wishlist() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }

    public String getItemDescription() { return itemDescription; }
    public void setItemDescription(String itemDescription) { this.itemDescription = itemDescription; }

    public String getItemImage() { return itemImage; }
    public void setItemImage(String itemImage) { this.itemImage = itemImage; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    @JsonProperty("user_id")
    public Long getUser_id() { return userId; }

    @JsonProperty("user_id")
    public void setUser_id(Long user_id) { this.userId = user_id; }

    @JsonProperty("item_id")
    public String getItem_id() { return itemId; }

    @JsonProperty("item_id")
    public void setItem_id(String item_id) { this.itemId = item_id; }

    @JsonProperty("item_name")
    public String getItem_name() { return itemName; }

    @JsonProperty("item_name")
    public void setItem_name(String item_name) { this.itemName = item_name; }

    @JsonProperty("item_type")
    public String getItem_type() { return itemType; }

    @JsonProperty("item_type")
    public void setItem_type(String item_type) { this.itemType = item_type; }

    @JsonProperty("item_description")
    public String getItem_description() { return itemDescription; }

    @JsonProperty("item_description")
    public void setItem_description(String item_description) { this.itemDescription = item_description; }

    @JsonProperty("item_image")
    public String getItem_image() { return itemImage; }

    @JsonProperty("item_image")
    public void setItem_image(String item_image) { this.itemImage = item_image; }
}

