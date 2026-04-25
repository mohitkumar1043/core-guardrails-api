package com.mohit.coreguardrails.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bots")
@Getter
@Setter
public class Bot {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    @Column(name="name",nullable = false, unique = true)
    private String name;
   
    @Column(name = "persona_description", nullable = false, columnDefinition = "TEXT")
    private String personaDescription;

}
