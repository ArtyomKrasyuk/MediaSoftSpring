package com.example.MediaSoftSpring.repositories;

import com.example.MediaSoftSpring.dto.VisitorRequestDTO;
import com.example.MediaSoftSpring.entities.Visitor;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public class VisitorRepository {
    private final List<Visitor> visitors;

    public VisitorRepository(){
        visitors = new LinkedList<>();
    }

    public boolean save(Visitor visitor){
        for(Visitor elem: visitors){
            if(elem.getId().equals(visitor.getId())) return false;
        }
        visitors.add(visitor);
        return true;
    }

    public boolean remove(Visitor visitor){
        return visitors.remove(visitor);
    }

    public boolean removeById(Long id){
        return visitors.removeIf(elem -> elem.getId().equals(id));
    }

    public List<Visitor> findAll(){
        return visitors;
    }

    public Visitor findById(Long id){
        return visitors.stream().filter(elem -> elem.getId().equals(id)).findFirst().orElse(null);
    }

    @PostConstruct
    private void setData(){
        save(new Visitor(1L, "Илья", 18, "Мужской"));
        save(new Visitor(2L, null, 25, "Женский"));
        save(new Visitor(3L, "Артём", 21, "Мужской"));
    }
}
