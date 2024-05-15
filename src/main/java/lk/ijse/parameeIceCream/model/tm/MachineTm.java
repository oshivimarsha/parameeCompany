package lk.ijse.parameeIceCream.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class MachineTm {
    private String id;
    private String type;
    private int serialNumber;
    private Date purchaseDate;
    private double purchaseCost;
    private double maintainCost;
    private String departmentId;
}
