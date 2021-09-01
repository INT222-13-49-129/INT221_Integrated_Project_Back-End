package sit.int222.cfan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.int222.cfan.entities.Ingredients;
import sit.int222.cfan.repositories.IngredientsRepository;

import java.util.List;

@Service
public class IngredientsController {
    @Autowired
    private IngredientsRepository ingredientsRepository;

    public List<Ingredients> findAll() {
        return ingredientsRepository.findAll();
    }
}
