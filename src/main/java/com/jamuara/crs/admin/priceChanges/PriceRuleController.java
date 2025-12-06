package com.jamuara.crs.admin.priceChanges;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/pricing")
@CrossOrigin("*")
public class PriceRuleController {

    private final PriceRuleRepository repository;

    public PriceRuleController(PriceRuleRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public PriceRule create(@RequestBody PriceRule rule) {
        return repository.save(rule);
    }

    @PutMapping("/{id}")
    public PriceRule update(@PathVariable Long id, @RequestBody PriceRule rule) {
        rule.setId(id);
        return repository.save(rule);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping
    public List<PriceRule> getAll() {
        return repository.findAll();
    }
}
