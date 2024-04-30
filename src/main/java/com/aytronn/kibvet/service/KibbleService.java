package com.aytronn.kibvet.service;

import com.aytronn.kibvet.dao.Food;
import com.aytronn.kibvet.dao.Kibble;
import com.aytronn.kibvet.dto.CreateKibbleDto;
import com.aytronn.kibvet.exception.KibbleNotFoundException;
import com.aytronn.kibvet.mapper.FoodMapper;
import com.aytronn.kibvet.mapper.KibbleMapper;
import com.aytronn.kibvet.repository.FoodRepository;
import com.aytronn.kibvet.repository.KibbleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class KibbleService {

    private final KibbleRepository kibbleRepository;
    private final FoodRepository foodRepository;
    private final FoodMapper foodMapper;
    private final KibbleMapper kibbleMapper;

    public KibbleService(KibbleRepository kibbleRepository, FoodRepository foodRepository, FoodMapper foodMapper, KibbleMapper kibbleMapper) {
        this.kibbleRepository = kibbleRepository;
        this.foodRepository = foodRepository;
        this.foodMapper = foodMapper;
        this.kibbleMapper = kibbleMapper;
    }

    public Kibble createKibble(CreateKibbleDto kibbleDto) {
        final Food foodToSave = getFoodMapper().toFood(kibbleDto.food());

        Food food = getFoodRepository().save(foodToSave);
        final Kibble kibbleToSave = getKibbleMapper().toKibble(kibbleDto);
        kibbleToSave.setFood(food);
        return getKibbleRepository().save(kibbleToSave);
    }

    public List<Kibble> getAllKibble() {
        return getKibbleRepository().findAll();
    }

    public Kibble getKibble(UUID id) {
        final Optional<Kibble> byKibbleId = getKibbleRepository().findByKibbleId(id);
        return byKibbleId.orElseThrow(() -> new KibbleNotFoundException("Kibble not found"));
    }

    public FoodMapper getFoodMapper() {
        return this.foodMapper;
    }

    public KibbleMapper getKibbleMapper() {
        return this.kibbleMapper;
    }

    public KibbleRepository getKibbleRepository() {
        return this.kibbleRepository;
    }

    public FoodRepository getFoodRepository() {
        return this.foodRepository;
    }
}
