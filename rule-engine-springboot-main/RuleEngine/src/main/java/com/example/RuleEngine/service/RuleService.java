package com.example.RuleEngine.service;

import com.example.RuleEngine.model.Node;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RuleService {
    Node createRule(String ruleString);

    Node combineRules(List<String> rules);

    boolean evaluateRule(Node ast, Map<String, Object> data);
}
