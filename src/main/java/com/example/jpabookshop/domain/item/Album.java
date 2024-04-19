package com.example.jpabookshop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;

@Entity
@DiscriminatorValue("A")
@Getter
@PrimaryKeyJoinColumn(name = "ALBUM_ID")
public class Album extends Item {

    private String artist;
    private String etc;

}
