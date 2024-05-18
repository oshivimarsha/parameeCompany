package lk.ijse.parameeIceCream.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Department {
    private String id;
    private String name;
    private String description;
    private int numOfEmp;

}
