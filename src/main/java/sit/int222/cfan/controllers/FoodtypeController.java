package sit.int222.cfan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.int222.cfan.entities.Foodtype;
import sit.int222.cfan.repositories.FoodtypeRepository;

import java.util.List;

@Service
public class FoodtypeController {
    @Autowired
    private FoodtypeRepository foodtypeRepository;

    public List<Foodtype> findAll() {
        return foodtypeRepository.findAll();
    }
}
