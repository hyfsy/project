package com.hyf.cache.impl.spel;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author baB_hyf
 * @date 2022/02/08
 */
public class CacheEvaluationContext extends MethodBasedEvaluationContext {

    private final Set<String> unavailableVariables = new HashSet<>(1);


    public CacheEvaluationContext(Object rootObject, Method method, Object[] arguments,
                           ParameterNameDiscoverer parameterNameDiscoverer) {

        super(rootObject, method, arguments, parameterNameDiscoverer);
    }


    /**
     * Add the specified variable name as unavailable for that context.
     * Any expression trying to access this variable should lead to an exception.
     * <p>This permits the validation of expressions that could potentially a
     * variable even when such variable isn't available yet. Any expression
     * trying to use that variable should therefore fail to evaluate.
     */
    public void addUnavailableVariable(String name) {
        this.unavailableVariables.add(name);
    }


    /**
     * Load the param information only when needed.
     */
    @Override
    public Object lookupVariable(String name) {
        if (this.unavailableVariables.contains(name)) {
            throw new VariableNotAvailableException(name);
        }
        return super.lookupVariable(name);
    }

}
