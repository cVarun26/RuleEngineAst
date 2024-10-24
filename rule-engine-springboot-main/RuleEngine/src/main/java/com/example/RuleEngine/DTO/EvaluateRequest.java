package com.example.RuleEngine.DTO;

import com.example.RuleEngine.model.Node;

import java.util.Map;

public class EvaluateRequest {
    private Node ast;
    private Map<String, Object> data;

    // Getters and Setters
    public Node getAst() {
        return ast;
    }

    public void setAst(Node ast) {
        this.ast = ast;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
