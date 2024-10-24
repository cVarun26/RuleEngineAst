package com.example.RuleEngine.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Node {
    private String type; // "operator" or "operand"
    private Node left; // reference to left child node (for operators)
    private Node right; // reference to right child node (for operators)
    private Object value; // operand value

    // Constructor for operand nodes (no left and right)
    public Node(String type, Object value) {
        this.type = type;
        this.value = value;
        this.left = null; // Optional, could be left out since Lombok will generate them
        this.right = null; // Optional, could be left out since Lombok will generate them
    }
}

