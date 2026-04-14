package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.InventoryDtos.*;

import edu.unimagdalena.tienda_universitaria.repositories.InventoryRepository;
import edu.unimagdalena.tienda_universitaria.exception.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService{
    private final InventoryRepository inventoryRepo;

    @Transactional
    @Override
    public InventoryResponse update(Long productId, InventoryUpdateRequest req) {
        var inventory = inventoryRepo.findByProduct_Id(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product %d".formatted(productId)));

        if (req.availableStock() != null) {
            inventory.setAvailableStock(req.availableStock());
        }

        if (req.minimumStock() != null) {
            inventory.setMinimumStock(req.minimumStock());
        }



        inventory.setUpdatedAt(Instant.now());
        var saved = inventoryRepo.save(inventory);

        return new InventoryResponse(
                saved.getId(),
                saved.getProduct().getId(),
                saved.getAvailableStock(),
                saved.getMinimumStock(),
                saved.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public InventoryResponse getByProductId(Long productId) {
        var inventory = inventoryRepo.findByProduct_Id(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product %d".formatted(productId)));
        return new InventoryResponse(
                inventory.getId(),
                inventory.getProduct().getId(),
                inventory.getAvailableStock(),
                inventory.getMinimumStock(),
                inventory.getUpdatedAt()
        );
    }
}
