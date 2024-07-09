package com.hyf.cache.impl.spel;

import org.springframework.expression.EvaluationException;

/**
 * @author baB_hyf
 * @date 2022/02/08
 */
public class VariableNotAvailableException extends EvaluationException {

    private final String name;


    public VariableNotAvailableException(String name) {
        super("Variable not available");
        this.name = name;
    }


    public final String getName() {
        return this.name;
    }

}
