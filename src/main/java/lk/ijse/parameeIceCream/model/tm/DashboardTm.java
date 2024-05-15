package lk.ijse.parameeIceCream.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class DashboardTm {
    private String productId;
    private int orderId;
    private int qty;

}
