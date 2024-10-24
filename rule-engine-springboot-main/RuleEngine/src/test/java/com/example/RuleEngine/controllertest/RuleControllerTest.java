package com.example.RuleEngine.controllertest;

import com.example.RuleEngine.controller.RuleController;
import com.example.RuleEngine.model.Node;
import com.example.RuleEngine.service.RuleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@ExtendWith(MockitoExtension.class)
public class RuleControllerTest {

    @InjectMocks
    private RuleController ruleController;

    @Mock
    private RuleService ruleService;

    @Test
    public void testCreateRule_Success() {
        String ruleString = "age > 18"; // Example rule string

        // Create the expected AST structure
        Node expectedAst = new Node("operator",
                new Node("operand", "age"),
                new Node("operand", "18"),
                ">"); // Example AST with operator and operands

        Mockito.when(ruleService.createRule(ruleString)).thenReturn(expectedAst);

        ResponseEntity<Node> response = ruleController.createRule(ruleString);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedAst, response.getBody());
    }


    @Test
    public void testCreateRule_BadRequest() {
        String ruleString = ""; // Invalid rule string

        Mockito.when(ruleService.createRule(ruleString)).thenThrow(new IllegalArgumentException());

        ResponseEntity<Node> response = ruleController.createRule(ruleString);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}
