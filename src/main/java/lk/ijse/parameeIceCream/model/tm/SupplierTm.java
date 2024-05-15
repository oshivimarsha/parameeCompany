package lk.ijse.parameeIceCream.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class SupplierTm {
    private String id;
    private String name;
    private String nic;
    private String address;
    private String tel;
    private String email;

}
