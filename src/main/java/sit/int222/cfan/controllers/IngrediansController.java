package sit.int222.cfan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.int222.cfan.entities.Ingredians;
import sit.int222.cfan.repositories.IngrediansRepository;

import java.util.List;

@Service
public class IngrediansController {
    @Autowired
    private IngrediansRepository ingrediansRepository;

    public List<Ingredians> findAll() {
        return ingrediansRepository.findAll();
    }
}
