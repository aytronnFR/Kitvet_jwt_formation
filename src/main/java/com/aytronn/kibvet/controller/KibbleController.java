package com.aytronn.kibvet.controller;

import com.aytronn.kibvet.dao.Kibble;
import com.aytronn.kibvet.dto.CreateKibbleDto;
import com.aytronn.kibvet.enums.KibbleType;
import com.aytronn.kibvet.exception.InvalidKibbleTypeException;
import com.aytronn.kibvet.service.KibbleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@Validated
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'EDITOR')")
public class KibbleController {

    private final KibbleService kibbleService;

    public KibbleController(KibbleService kibbleService) {
        this.kibbleService = kibbleService;
    }

    @PostMapping("/kibble")
    @PreAuthorize("hasAuthority('editor:create')")
    public Kibble createKibble(@Valid @RequestBody CreateKibbleDto kibbleDto) {
        log.info("createKibble called : {}", kibbleDto);

        try {
            KibbleType.valueOf(kibbleDto.type());
        } catch (IllegalArgumentException e) {
            throw new InvalidKibbleTypeException("The type of kibble is not valid", kibbleDto.type());
        }

        return getKibbleService().createKibble(kibbleDto);
    }

    @GetMapping("/kibble")
    public List<Kibble> getAllKibble() {
        log.info("getAllKibble called");

        return getKibbleService().getAllKibble();
    }

    @GetMapping("/kibble/{id}")
    public Kibble getKibble(@PathVariable UUID id) {
        log.info("getKibble called : {}", id);

        return getKibbleService().getKibble(id);
    }

    public KibbleService getKibbleService() {
        return this.kibbleService;
    }
}
