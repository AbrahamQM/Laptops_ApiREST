package com.example.ej_Spring_rest_data.Repositories;

import com.example.ej_Spring_rest_data.Entities.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop, Long> {
}
