# Electricity Billing System – JAVA Swing Based Secure Real-Time Database Management

This project simulates electricity billing operations with real-time data handling and secure database integration. It provides a graphical user interface (GUI) for managing customer information, calculating bills, processing payments, and generating reports, all while ensuring data security and integrity.

## 🔧 Features

* **User Authentication**: Secure login system to protect sensitive data.
* **Customer Management**: Add, update, and view customer details.
* **Billing System**: Calculate electricity bills based on consumption and applicable taxes.
* **Payment Processing**: Record and manage bill payments.
* **Report Generation**: Generate detailed billing reports for customers.
* **Real-Time Data Handling**: Immediate reflection of data changes across the system.
* **Secure Database Integration**: Utilizes secure practices for database connections and operations.([github.com][1], [github.com][2])

## 🗂️ Project Structure

The repository is organized into the following directories:

* `db/`: Contains SQL scripts for database schema creation and sample data.
* `gui/`: Holds Java Swing components for the graphical user interface.
* `MysqlCode/`: Includes Java classes responsible for database connectivity and operations.
* `Main.java`: The entry point of the application.
* `README.md`: Project documentation.

## 🛠️ Technologies Used

* **Programming Language**: Java
* **GUI Framework**: Java Swing
* **Database**: MySQL
* **Database Connectivity**: JDBC (Java Database Connectivity)([med.upenn.edu][4], [github.com][2])

## 📦 Database Schema

The MySQL database consists of the following tables:([github.com][1])

1. **Login Table**: Stores user credentials and roles.

   * Fields: `UserName`, `Password`, `Name`, `MeterNumber`, `UserType`
2. **Customer Table**: Contains customer personal and contact information.



## 🚀 Getting Started

### Prerequisites

* Java Development Kit (JDK) 8 or higher
* MySQL Server
* An IDE like Eclipse or IntelliJ IDEA

### Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/rahul-rrj/Electricity-Billing-System-Secure-Real-Time-Database-Management.git
   ```



2. **Set Up the Database**

   * Open MySQL Workbench or your preferred MySQL client.
   * Execute the SQL scripts located in the `db/` directory to create the necessary tables and insert sample data.

3. **Configure Database Connection**

   * In the `MysqlCode/` directory, locate the class responsible for establishing the database connection 
   * Update the database URL, username, and password as per your MySQL configuration.

4. **Build and Run the Application**

   * Open the project in your IDE.
   * Build the project to resolve dependencies.
   * Run `Main.java` to launch the application.

## 🖥️ Usage

1. **Login**

   * Use the credentials from the `Login` table to access the system.

2. **Dashboard**

   * Navigate through the GUI to manage customers, calculate bills, process payments, and generate reports.([github.com][2])

3. **Add Customer**

   * Enter customer details to add a new customer to the system.

4. **Calculate Bill**

   * Input the number of units consumed to calculate the bill based on current rates and taxes.

5. **Pay Bill**

   * Record payment details to update the bill status.

6. **Generate Report**

   * Produce billing reports for individual customers or overall summaries.


## 🛡️ Security Measures

* **Authentication**: Ensures that only authorized users can access the system.
* **Secure Database Operations**: Utilizes prepared statements to prevent SQL injection attacks.

## 🤝 Contributing

Contributions are welcome! Please fork the repository and submit a pull request for any enhancements or bug fixes.


