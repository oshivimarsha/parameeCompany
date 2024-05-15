package lk.ijse.parameeIceCream.model.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class OrderTm {
    private String id;
    private String productName;
    private int qty;
    private double unitPrice;
    private double price;
    private JFXButton btnRemove;




}
