package lk.ijse.parameeIceCream.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class SalaryTm {
    private String id;
    private String name;
    private String nic;
    private double basicSalary;
    private double allowences;
    private double advance;
}
