drop database parameeCompany;

create database parameeCompany;

use parameeCompany;

CREATE TABLE users(
    userName VARCHAR(20) PRIMARY KEY,
    password VARCHAR(20),
    email TEXT
);

INSERT INTO users VALUES('oshadhi', '0812', 'oshi@gmail.com');


CREATE TABLE supplier(
    supplierId VARCHAR(5) PRIMARY KEY,
    name VARCHAR(20),
    nic VARCHAR(15),
    address VARCHAR(20),
    email VARCHAR(25),
    tel VARCHAR(15)
);

INSERT INTO supplier VALUES('S001','Danapala', '98562835', 'Panadura','danapala@gmail.com','0715427981');
INSERT INTO supplier VALUES('S002','Gunapala','89500384', 'Galle','gunapala@gmail.com','0772315678');


CREATE TABLE ingredient(
    ingredientId VARCHAR(5) PRIMARY KEY,
    name VARCHAR(100),
    qtyInStock VARCHAR(10),
    unitOfMeasure VARCHAR(25),
    unitPrice DECIMAL(10,2),
    price DECIMAL(10,2),
    supplierId VARCHAR(5),
    CONSTRAINT FOREIGN KEY(supplierId) REFERENCES supplier(supplierId) on Delete Cascade on Update Cascade
);


INSERT INTO ingredient VALUES('I001', 'Milk', '2', 'liters', 150.00, 300.00, 'S001');
INSERT INTO ingredient VALUES('I002', 'Sugar', '5', 'kilograms', 200.00, 1000.00, 'S002');



CREATE TABLE customer(
    customerId VARCHAR(5) PRIMARY KEY,
    name VARCHAR(20),
    nic VARCHAR(15),
    address VARCHAR(20),
    email VARCHAR(25),
    tel VARCHAR(15)
);


INSERT INTO customer VALUES('C001', 'Nimal', '2003765489', 'Kalutara', 'nimal@gmail.com', '0712567842');
INSERT INTO customer VALUES('C002', 'kamal', '2003783502', 'Moratuwa', 'kamal@gmail.com', '0785641904');


CREATE TABLE orders(
    orderId VARCHAR(5) PRIMARY KEY,
    orderDate DATE,
    unitPrice DECIMAL(10,2),
    customerId VARCHAR(5),
    CONSTRAINT FOREIGN KEY(customerId) REFERENCES customer(customerId) on Delete Cascade on Update Cascade
);


INSERT INTO orders VALUES('O001', '2024-03-30', 500.00, 'C001');
INSERT INTO orders VALUES('O002', '2024-03-31', 800.00, 'C002');


CREATE TABLE department(
    departmentId VARCHAR(5) PRIMARY KEY,
    name VARCHAR(20),
    description VARCHAR(100),
    numberOfEmployees VARCHAR(5)
);


INSERT INTO department VALUES('D001', 'Production', 'Responsible_for_manufacturing_operations', '5');
INSERT INTO department VALUES('D002', 'Sales', 'Handles_customer_orders_and_sales_activities', '4');



CREATE TABLE employee(
    employeeId VARCHAR(5) PRIMARY KEY,
    name VARCHAR(20),
    nic VARCHAR(15),
    address VARCHAR(20),
    email VARCHAR(25),
    tel VARCHAR(15),
    DOB DATE,
    registerDate DATE,
    position VARCHAR(20),
    salary DECIMAL(10,2),
    departmentId VARCHAR(5),
    CONSTRAINT FOREIGN KEY(departmentId) REFERENCES department(departmentId) on Delete Cascade on Update Cascade,
    path TEXT
);


INSERT INTO employee VALUES('E001', 'Sumith', '9067546', 'Mathugama', 'sumith@gmail.com', '0735472910', '1997.09.13', '2023.08.01', 'Manager', 50000.00, 'D001', '/home/oshi/Downloads');
INSERT INTO employee VALUES('E002', 'Shanika', '9067546', 'Aluthgama', 'shanika@gmail.com', '0735472910', '1998.11.15', '2023.03.01', 'HR', 30000.00, 'D002', '/home/oshi/Pictures');


CREATE TABLE product(
    productId VARCHAR(5) PRIMARY KEY,
    name VARCHAR(100),
    category VARCHAR(50),
    description VARCHAR(100),
    qtyAvailable INT(5), 
    unitPrice DECIMAL(8,2),
    departmentId VARCHAR(5),
    CONSTRAINT FOREIGN KEY(departmentId) REFERENCES department(departmentId) on Delete Cascade on Update Cascade,
    path TEXT
);


INSERT INTO product VALUES('P001', 'VanillaIceCream', 'IceCream', 'ClassicVanillaFlavor', 100, 500.00, 'D001', '/home/oshi/Downloads');
INSERT INTO product VALUES('P002', 'StrawberryYogurt', 'Yogurt', 'FreshStrawberryFlavor', 100, 450.00, 'D001', '/home/oshi/Downloads');


CREATE TABLE machine(
    machineId VARCHAR(5) PRIMARY KEY,
    type VARCHAR(20),
    serialNumber INT(10),
    purchaseDate DATE,
    purchaseCost DECIMAL(8,2),
    maintenanceCost DECIMAL(8,2),
    departmentId VARCHAR(5),
    CONSTRAINT FOREIGN KEY(departmentId) REFERENCES department(departmentId) on Delete Cascade on Update Cascade,
    path TEXT
);


INSERT INTO machine VALUES('M001', 'IceCreamMaker', 1234567890, '2022-01-01', 5000.00, 500.00, 'D001', '/home/oshi/Downloads');
INSERT INTO machine VALUES('M002', 'YogurtDispenser', 237654321, '2022-02-01', 3000.00, 300.00, 'D002', '/home/oshi/Downloads');



CREATE TABLE orderProductDetail(
    orderId VARCHAR(5),
    productId VARCHAR(5),
    qty INT(11),
    unitPrice DECIMAL(8,2),
    CONSTRAINT FOREIGN KEY (orderId) REFERENCES orders(orderId) on Delete Cascade on Update Cascade,
    CONSTRAINT FOREIGN KEY (productId) REFERENCES product(productId) on Delete Cascade on Update Cascade
);


INSERT INTO orderProductDetail VALUES('O001', 'P001', 2, 100.00);
INSERT INTO orderProductDetail VALUES ('O002', 'P002', 3, 50.00);



CREATE TABLE ingredientProductDetail(
    ingredientId VARCHAR(5),
    CONSTRAINT FOREIGN KEY (ingredientId) REFERENCES ingredient(ingredientId) on Delete Cascade on Update Cascade,
    productId VARCHAR(5),
    CONSTRAINT FOREIGN KEY (productId) REFERENCES product(productId) on Delete Cascade on Update Cascade,
    qty INT(11),
    unitPrice DECIMAL(8,2)
);


INSERT INTO ingredientProductDetail VALUES('I001', 'P001', 3, 200.00);
INSERT INTO ingredientProductDetail VALUES('I002', 'P002', 2, 150.00);


CREATE TABLE salary(
    employeeId VARCHAR(5),
    basicSalary DECIMAL(10,2),
    allowences DECIMAL(10,2),
    advance DECIMAL(10,2),
    CONSTRAINT FOREIGN KEY (employeeId) REFERENCES employee(employeeId) on Delete Cascade on Update Cascade
);


INSERT INTO salary VALUES('E001', 20000.00, 5000.00, 5000.00);
INSERT INTO salary VALUES('E002', 25000.00, 2000.00, 5000.00);


------------------------------------------------------------------------------------------------------------------------

SELECT * FROM users;
SELECT * FROM supplier;
SELECT * FROM ingredient;
SELECT * FROM customer;
SELECT * FROM orders;
SELECT * FROM department;
SELECT * FROM employee;
SELECT * FROM product;
SELECT * FROM machine;
SELECT * FROM ingredientProductDetail;
SELECT * FROM orderProductDetail;
SELECT * FROM salary;



------------------------------------------------------------------------------------------------------------------------



SELECT 
    e.employeeId,
    e.name,
    e.path,
    e.nic,
    s.basicSalary,
    s.allowences,
    (s.basicSalary + s.allowences) AS grossSalary,
    s.advance,
    (s.basicSalary + s.allowences - s.advance) AS netPayable
FROM 
    employee e
JOIN 
    salary s 
ON 
    e.employeeId = s.employeeId;

            









SELECT 
    e.employeeId,
    e.name,
    e.path,
    e.nic,
    s.basicSalary,
    s.allowences,
    s.advance,
    (s.basicSalary + s.allowences) AS grossSalary,
    (s.basicSalary + s.allowences - s.advance) AS netPayable
FROM 
    employee e
JOIN 
    salary s ON e.employeeId = s.employeeId
WHERE
    s.employeeId = 'E002';





















































































