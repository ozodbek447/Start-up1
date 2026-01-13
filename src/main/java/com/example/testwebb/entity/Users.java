package com.example.testwebb.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Users {

    @Id
    private Long id;
    private String name;
    private String step;
    private int score;    //! tuplangam yulduzchalar
    private int answers;
    private int fight;    //! qilgan janglari soni
    private int cont=0;     //! suzlar tartibi
    private List<String> answer=new ArrayList<>();




}
