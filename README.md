Here's a structured README for your Spring Boot Rule Engine application with AST:

---

# Rule Engine with Abstract Syntax Tree (AST)

## Overview

This project is a **3-tier Rule Engine** application developed using **Spring Boot (Java)** for the backend, **MySQL** for data storage, and **React** for the frontend. The application determines user eligibility based on attributes such as age, department, income, and spend. It dynamically creates, combines, and evaluates conditional rules using an **Abstract Syntax Tree (AST)**.

## Objectives

- Develop a system to represent and evaluate dynamic conditional rules using AST.
- Allow for the creation, modification, and combination of rules in real time.
- Store the rules and associated metadata in a relational database (MySQL).
- Provide a simple UI (built with React) for interacting with the rule engine.

## Features

- **Rule Creation**: Users can create rules using a simple string format (e.g., `age > 30 AND department = 'Sales'`), which are converted into an AST.
- **Rule Combination**: Users can combine multiple rules into a single AST, optimizing for efficiency and minimizing redundant conditions.
- **Rule Evaluation**: The system evaluates user eligibility against the defined rules based on user attributes like age, department, salary, and experience.
- **Data Storage**: Rules and application metadata are stored in a MySQL database.

---

## Architecture

The application is organized into three main layers:

1. **Frontend (React)**: Simple UI for rule creation and visualization.
2. **Backend (Spring Boot)**: API services to handle rule creation, combination, and evaluation.
3. **Data (MySQL)**: Storage of rules and application metadata.

### Technologies Used

- **Frontend**: React
- **Backend**: Spring Boot, Java, IntelliJ IDEA
- **Database**: MySQL

---

## Data Structure

The Abstract Syntax Tree (AST) is the core structure representing conditional rules in the application.

### AST Node Structure

```plaintext
Node {
    type: String (e.g., "operator" for AND/OR, "operand" for conditions),
    left: Reference to another Node (left child),
    right: Reference to another Node (right child),
    value: Optional value for operand nodes (e.g., number for comparisons)
}
```

This structure supports dynamic creation and modification of rules.

### Example Rules

1. **Rule 1**:
   ```
   ((age > 30 AND department = 'Sales') OR (age < 25 AND department = 'Marketing')) 
   AND (salary > 50000 OR experience > 5)
   ```

2. **Rule 2**:
   ```
   (age > 30 AND department = 'Marketing') AND (salary > 20000 OR experience > 5)
   ```

---

## Data Storage

### Database: MySQL

The application uses MySQL to store rules and metadata. Below is an example schema for the rule storage:

### Table: `rules`

| Field      | Type   | Description                                      |
|------------|--------|--------------------------------------------------|
| id         | INT    | Primary key, auto-increment                      |
| rule       | TEXT   | The rule string                                  |
| ast        | JSON   | Serialized AST representing the rule             |
| created_at | DATETIME | Timestamp of when the rule was created          |

### Sample Database Record

| id | rule | ast | created_at |
|----|------|-----|------------|
| 1  | "age > 30 AND salary > 50000" | { "type": "AND", "left": { "type": "operand", "value": "age > 30" }, "right": { "type": "operand", "value": "salary > 50000" }} | 2024-10-23 12:34:56 |

---

## API Design

The application exposes the following API endpoints:

### 1. `POST /api/create_rule`

- **Description**: Accepts a rule string and converts it to an AST.
- **Input**: Rule string
  ```json
  {
    "rule": "age > 30 AND department = 'Sales'"
  }
  ```
- **Response**: Returns the generated AST.
  ```json
  {
    "ast": {
      "type": "AND",
      "left": { "type": "operand", "value": "age > 30" },
      "right": { "type": "operand", "value": "department = 'Sales'" }
    }
  }
  ```

### 2. `POST /api/combine_rules`

- **Description**: Combines multiple rule strings into a single AST.
- **Input**: List of rule strings
  ```json
  {
    "rules": [
      "age > 30 AND department = 'Sales'",
      "salary > 50000 OR experience > 5"
    ]
  }
  ```
- **Response**: Returns the combined AST.

### 3. `POST /api/evaluate_rule`

- **Description**: Evaluates the AST against provided user data.
- **Input**: AST and user data
  ```json
  {
    "ast": { "type": "AND", "left": {...}, "right": {...} },
    "data": {
      "age": 35,
      "department": "Sales",
      "salary": 60000,
      "experience": 3
    }
  }
  ```
- **Response**: Returns a boolean (`true` if the user meets the rule, `false` otherwise).

---

## Test Cases

1. **Create Rule**:
   - Use `create_rule` to create an AST from the rule string `"age > 30 AND department = 'Sales'"`.
   - Verify that the AST accurately represents the rule.
   
2. **Combine Rules**:
   - Combine multiple rules such as `"age > 30 AND department = 'Sales'"` and `"salary > 50000 OR experience > 5"`.
   - Ensure that the combined AST correctly reflects the logic.

3. **Evaluate Rule**:
   - Test the evaluation function with different sets of user data and rules.
   - Example data: `{"age": 35, "department": "Sales", "salary": 60000, "experience": 3}`.

---

## Setup and Installation

### Prerequisites

- Java 11+
- Node.js (for frontend)
- MySQL

### Backend (Spring Boot)

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd rule-engine-backend
   ```

2. Configure the MySQL database in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/rule_engine
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   ```

3. Build and run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend (React)

1. Navigate to the frontend directory:
   ```bash
   cd rule-engine-frontend
   ```

2. Install dependencies and run the development server:
   ```bash
   npm install
   npm start
   ```

---

## Future Enhancements

- **UI Improvements**: Expand the user interface for better rule visualization.
- **Optimization**: Implement further optimizations for combining large numbers of rules.
- **Additional Data Sources**: Integrate additional data sources for rule evaluation.
  
---

## License

This project is licensed under the MIT License.

---

## Contributors

- **[Your Name]** - Developer

---

This README gives an overview of your Spring Boot Rule Engine application, explaining its structure, features, setup, and testing guidelines. Let me know if you need any adjustments!
