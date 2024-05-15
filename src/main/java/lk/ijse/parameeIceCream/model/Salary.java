package lk.ijse.parameeIceCream.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Salary {
    private String id;
    private double basicSalary;
    private double allowences;
    private double advance;

}
