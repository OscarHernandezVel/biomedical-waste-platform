package com.biomedical.waste.demo.services;

import com.biomedical.waste.demo.exception.WasteNotFoundException;
import com.biomedical.waste.demo.models.Waste;
import com.biomedical.waste.demo.models.WasteType;
import com.biomedical.waste.demo.patterns.builder.WasteBuilder;
import com.biomedical.waste.demo.patterns.decorator.BaseWasteProcessor;
import com.biomedical.waste.demo.patterns.decorator.LoggingDecorator;
import com.biomedical.waste.demo.patterns.decorator.TraceabilityDecorator;
import com.biomedical.waste.demo.patterns.decorator.ValidationDecorator;
import com.biomedical.waste.demo.patterns.decorator.WasteProcessor;
import com.biomedical.waste.demo.patterns.prototype.WastePrototype;
import com.biomedical.waste.demo.repository.WasteRepository;
import com.biomedical.waste.demo.structures.CollectionQueue;
import com.biomedical.waste.demo.structures.EntityHashMap;
import com.biomedical.waste.demo.structures.TraceabilityLinkedList;
import com.biomedical.waste.demo.structures.WasteTypeBST;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WasteService {

    private final WasteRepository wasteRepository;
    private final @Lazy AlertService alertService;
    private final CollectionQueue<Waste> collectionQueue = new CollectionQueue<>();
    private final EntityHashMap<String, List<Waste>> entityIndex = new EntityHashMap<>();
    private final TraceabilityLinkedList<String> globalTraceLog = new TraceabilityLinkedList<>();
    private final WasteTypeBST<Double> weightTree = new WasteTypeBST<>();

    /** Returns all registered waste items. */
    public List<Waste> getAll() {
        return wasteRepository.findAll();
    }

    /** Returns a waste by id or throws WasteNotFoundException. */
    public Waste getById(String id) {
        if (id == null) throw new IllegalArgumentException("Waste id cannot be null");
        return wasteRepository.findById(id).orElseThrow(() -> new WasteNotFoundException(id));
    }

    /** Creates a new waste record using WasteBuilder validation and enqueues it for collection. */
    public Waste create(Waste waste) {
        if (waste == null) {
            throw new IllegalArgumentException("Waste cannot be null");
        }
        Waste validated = new WasteBuilder()
            .withType(waste.getType())
            .withWeight(waste.getWeightKg())
            .withOriginEntity(waste.getOriginEntity())
            .withDate(waste.getGenerationDate())
            .withStatus(waste.getStatus())
            .build();
        Waste saved = wasteRepository.save(validated);
        collectionQueue.enqueue(saved);
        weightTree.insert(saved.getWeightKg());
        List<Waste> existing = entityIndex.get(saved.getOriginEntity());
        if (existing == null) existing = new java.util.ArrayList<>();
        existing.add(saved);
        entityIndex.put(saved.getOriginEntity(), existing);
        globalTraceLog.addLast("Waste " + saved.getId() + " created at " + LocalDateTime.now());
        if (saved.getType().getRiskLevel() >= 4) alertService.generateAlert(saved);
        return saved;
    }

    /** Updates an existing waste record by id. */
    public Waste update(String id, Waste waste) {
        Waste existing = getById(id);
        existing.setType(waste.getType());
        existing.setWeightKg(waste.getWeightKg());
        existing.setOriginEntity(waste.getOriginEntity());
        existing.setTreatmentMethod(waste.getTreatmentMethod());
        return wasteRepository.save(existing);
    }

    /** Deletes a waste record by id. */
    public void delete(String id) {
        if (id == null) throw new IllegalArgumentException("Waste id cannot be null");
        getById(id);
        wasteRepository.deleteById(id);
    }

    /** Returns all wastes of the given type. */
    public List<Waste> getByType(WasteType type) {
        return wasteRepository.findByType(type);
    }

    /** Returns all wastes from the given originating entity. */
    public List<Waste> getByEntity(String entity) {
        return wasteRepository.findByOriginEntity(entity);
    }

    /** Returns the total weight in kg of all registered waste. */
    public Double getTotalWeight() {
        return wasteRepository.findAll().stream().mapToDouble(Waste::getWeightKg).sum();
    }

    /** Processes a waste item through the full decorator chain: Log → Validate → Trace → Base. */
    public Waste processWaste(String id) {
        Waste waste = getById(id);
        WasteProcessor processor =
            new LoggingDecorator(
                new ValidationDecorator(
                    new TraceabilityDecorator(
                        new BaseWasteProcessor())));
        processor.process(waste);
        return wasteRepository.save(waste);
    }

    /** Clones a waste item using the Prototype pattern with a new generation date. */
    public Waste cloneWaste(String id) {
        Waste original = getById(id);
        Waste clone = new WastePrototype(original).cloneWithNewDate();
        return wasteRepository.save(clone);
    }

    /** Advances the waste status to the next step in the workflow. */
    public Waste advanceStatus(String id) {
        Waste waste = getById(id);
        waste.setStatus(waste.getStatus().next());
        return wasteRepository.save(waste);
    }

    /** Returns the global traceability log from oldest to newest entry. */
    public List<String> getTraceLog() {
        return globalTraceLog.traverseForward();
    }

    /** Returns wastes sorted by weight ascending using BST inorder traversal. */
    public List<Double> getWeightsSorted() {
        return weightTree.inorder();
    }
}
