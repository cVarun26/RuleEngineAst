import React, { useState } from "react";

function App() {
  const [rule, setRule] = useState("");
  const [age, setAge] = useState("");
  const [department, setDepartment] = useState("");
  const [salary, setSalary] = useState("");
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const createResponse = await fetch(
        "http://localhost:8000/engine/create",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(rule),
        }
      );

      if (!createResponse.ok) {
        throw new Error("Error in create endpoint response");
      }

      const createdAST = await createResponse.json();
      console.log("Created AST:", createdAST);

      const parsedData = {
        age: parseInt(age, 10),
        department,
        salary: parseFloat(salary),
      };

      const evaluateResponse = await fetch(
        "http://localhost:8000/engine/evaluate",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            ast: createdAST,
            data: parsedData,
          }),
        }
      );

      if (!evaluateResponse.ok) {
        const errorDetails = await evaluateResponse.text();
        throw new Error(`Evaluate endpoint error: ${errorDetails}`);
      }

      const evaluateResult = await evaluateResponse.json();
      setResult(evaluateResult);
      setError(null);
    } catch (err) {
      console.error("Error during evaluation:", err);
      setError("An error occurred while processing your request.");
    }
  };

  return (
    <div style={{ padding: "20px", maxWidth: "600px", margin: "0 auto" }}>
      <h1>Rule Evaluator</h1>

      <form onSubmit={handleSubmit}>
        <div>
          <label>Rule String:</label>
          <input
            type="text"
            value={rule}
            onChange={(e) => setRule(e.target.value)}
            style={{ width: "100%", marginBottom: "10px", padding: "8px" }}
          />
        </div>

        <div>
          <label>Age:</label>
          <input
            type="number"
            value={age}
            onChange={(e) => setAge(e.target.value)}
            style={{ width: "100%", marginBottom: "10px", padding: "8px" }}
          />
        </div>

        <div>
          <label>Department:</label>
          <input
            type="text"
            value={department}
            onChange={(e) => setDepartment(e.target.value)}
            style={{ width: "100%", marginBottom: "10px", padding: "8px" }}
          />
        </div>

        <div>
          <label>Salary:</label>
          <input
            type="number"
            value={salary}
            onChange={(e) => setSalary(e.target.value)}
            style={{ width: "100%", marginBottom: "10px", padding: "8px" }}
          />
        </div>

        <button type="submit" style={{ padding: "10px 20px" }}>
          Evaluate
        </button>
      </form>

      {result !== null && (
        <div
          style={{
            marginTop: "20px",
            padding: "10px",
            backgroundColor: result ? "#e0f7e0" : "#f7d2d2",
            width: "600px"
          }}
        >
          <h3>Evaluation Result:</h3>
          <p>{result ? "True" : "False"}</p>
        </div>
      )}

      {error && (
        <div
          style={{
            marginTop: "20px",
            padding: "10px",
            backgroundColor: "#f7e0e0",
          }}
        >
          <h3>Error:</h3>
          <p>{error}</p>
        </div>
      )}
    </div>
  );
}

export default App;
