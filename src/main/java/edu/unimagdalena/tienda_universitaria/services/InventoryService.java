package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.InventoryDtos.*;

public interface InventoryService {

    InventoryResponse update(Long productId, InventoryUpdateRequest req);
    InventoryResponse getByProductId(Long productId);
}
