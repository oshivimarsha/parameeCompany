package lk.ijse.parameeIceCream.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class IngredientsProduct {
    private String ingredientId;
    private String productId;
    private int qty;
    private double unitPrice;
}
