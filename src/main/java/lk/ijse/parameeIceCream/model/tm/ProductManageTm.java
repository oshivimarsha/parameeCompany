package lk.ijse.parameeIceCream.model.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ProductManageTm {
    private String ingId;
    private String ingName;
    private double unitPrice;
    private int qty;
    private double total;
    private JFXButton remove;
}
