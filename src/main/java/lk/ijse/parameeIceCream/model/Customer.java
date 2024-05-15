package lk.ijse.parameeIceCream.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Customer {
    private String id;
    private String name;
    private String nic;
    private String address;
    private String email;
    private String tel;
}
