package me.ayushdev.runesenchant.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ExpressionResolver {

    private static final ExpressionResolver instance = new ExpressionResolver();

    public static ExpressionResolver getInstance() {
        return instance;
    }

    public int solve(String expression) throws ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        return (int) engine.eval(expression);
    }
}
