package com.example.RuleEngine.servicetest;

import com.example.RuleEngine.model.Node;
import com.example.RuleEngine.service.impl.RuleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleServiceImplTest {

    private RuleServiceImpl ruleService;

    @BeforeEach
    public void setUp() {
        ruleService = new RuleServiceImpl();
    }

    @Test
    public void testCreateRule_ValidRule() {
        String ruleString = "age > 18";
        Node ast = ruleService.createRule(ruleString);
        assertNotNull(ast);
        assertEquals("operand", ast.getType());
    }

    @Test
    public void testCombineRules() {
        List<String> rules = Arrays.asList("age > 18", "income > 50000");
        Node combinedNode = ruleService.combineRules(rules);
        assertNotNull(combinedNode);
        assertEquals("operator", combinedNode.getType());
    }

    @Test
    public void testEvaluateRule_NumericComparison() {
        Node ast = ruleService.createRule("age > 18");
        Map<String, Object> data = new HashMap<>();
        data.put("age", 20);
        boolean result = ruleService.evaluateRule(ast, data);
        assertTrue(result);
    }

    @Test
    public void testEvaluateRule_StringComparison() {
        Node ast = ruleService.createRule("department = 'IT'");
        Map<String, Object> data = new HashMap<>();
        data.put("department", "IT");
        boolean result = ruleService.evaluateRule(ast, data);
        assertTrue(result);
    }


}
