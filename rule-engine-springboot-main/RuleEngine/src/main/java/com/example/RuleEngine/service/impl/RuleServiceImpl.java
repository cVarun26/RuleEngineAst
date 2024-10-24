package com.example.RuleEngine.service.impl;

import com.example.RuleEngine.model.Node;
import com.example.RuleEngine.service.RuleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Stack;

@Service
public class RuleServiceImpl implements RuleService {
    @Override
    public Node createRule(String ruleString) {
        return parseRule(ruleString);
    }

    private Node parseRule(String ruleString) {
        // Add space around parentheses and operators for easier splitting into tokens
        ruleString = ruleString.replace("(", " ( ").replace(")", " ) ")
                .replace(" AND ", " AND ").replace(" OR ", " OR ");

        String[] tokens = ruleString.split("\\s+");

        Stack<Node> stack = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];

            if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    String operator = operators.pop();
                    Node rightNode = stack.pop();
                    Node leftNode = stack.pop();
                    stack.push(new Node("operator", leftNode, rightNode, operator));
                }
                operators.pop(); // Remove the '('
            } else if (token.equals("AND") || token.equals("OR")) {
                operators.push(token);
            } else {
                // Combine operands with their operators
                if (i + 2 < tokens.length && (tokens[i + 1].equals(">") || tokens[i + 1].equals("<") || tokens[i + 1].equals("="))) {
                    String operand = tokens[i] + " " + tokens[i + 1] + " " + tokens[i + 2]; // e.g. "age > 30"
                    operand = operand.replace("\"", ""); // Remove any extraneous quotes
                    stack.push(new Node("operand", operand));
                    i += 2; // Skip the next two tokens as they have been processed
                } else {
                    // Push standalone operand, ensuring no quotes
                    String operand = token.replace("\"", ""); // Remove quotes
                    stack.push(new Node("operand", operand));
                }
            }
        }

        while (!operators.isEmpty()) {
            String operator = operators.pop();
            Node rightNode = stack.pop();
            Node leftNode = stack.pop();
            stack.push(new Node("operator", leftNode, rightNode, operator));
        }

        return stack.pop(); // The final node is the root of the AST
    }


    @Override
    public Node combineRules(List<String> rules) {
        Node combinedNode = null;

        for (String rule : rules) {
            Node newNode = createRule(rule);
            combinedNode = combineNodes(combinedNode, newNode);
        }

        return combinedNode;
    }

    private Node combineNodes(Node combinedNode, Node newNode) {
        if (combinedNode == null) return newNode;
        if (newNode == null) return combinedNode;

        // Create a new AND node to combine both trees
        return new Node("operator", combinedNode, newNode, "AND");
    }

    @Override
    public boolean evaluateRule(Node ast, Map<String, Object> data) {
        if (ast == null) return false;

        switch (ast.getType()) {
            case "operand":
                return evaluateOperand(ast, data);
            case "operator":
                return evaluateOperator(ast, data);
            default:
                throw new IllegalArgumentException("Unknown node type");
        }
    }

    private boolean evaluateOperator(Node node, Map<String, Object> data) {
        boolean leftResult = evaluateRule(node.getLeft(), data);
        boolean rightResult = evaluateRule(node.getRight(), data);

        switch (node.getValue().toString()) {
            case "AND":
                return leftResult && rightResult;
            case "OR":
                return leftResult || rightResult;
            default:
                throw new IllegalArgumentException("Unknown operator: " + node.getValue());
        }
    }

    private boolean evaluateOperand(Node node, Map<String, Object> data) {
        // The operand is expected to be in the format "attribute operator value"
        String[] parts = node.getValue().toString().split(" ");

        if (parts.length != 3) throw new IllegalArgumentException("Invalid operand format");

        String attribute = parts[0];
        String operator = parts[1];
        String comparisonValue = parts[2].replaceAll("'", "");

        Object attributeValue = data.get(attribute);

        if (attributeValue == null) throw new IllegalArgumentException("Attribute not found in data");

        if (attributeValue instanceof String) {
            return evaluateStringComparison((String) attributeValue, operator, comparisonValue);
        } else if (attributeValue instanceof Number) {
            return evaluateNumericComparison(((Number) attributeValue).intValue(), operator, Integer.parseInt(comparisonValue));
        } else {
            throw new IllegalArgumentException("Unsupported attribute type");
        }
    }

    private boolean evaluateNumericComparison(int attributeValue, String operator, int comparisonValue) {
        switch (operator) {
            case ">":
                return attributeValue > comparisonValue;
            case "<":
                return attributeValue < comparisonValue;
            case "=":
                return attributeValue == comparisonValue;
            default:
                throw new IllegalArgumentException("Unknown operator for numeric comparison");
        }

    }

    private boolean evaluateStringComparison(String attributeValue, String operator, String comparisonValue) {
        switch (operator) {
            case "=":
                return attributeValue.equals(comparisonValue);
            default:
                throw new IllegalArgumentException("Unknown operator for string comparison");
        }
    }
}

