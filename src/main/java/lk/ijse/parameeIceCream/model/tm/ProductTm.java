package lk.ijse.parameeIceCream.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class ProductTm {
    private String id;
    private String name;
    private String category;
    private String description;
    private int qtyAvailable;
    private double unitPrice;
    private String departmentId;
}
