package com.example.testwebb.data;

import com.example.testwebb.entity.Users;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataBase {

    public List<Users> users=new ArrayList<>();
}
