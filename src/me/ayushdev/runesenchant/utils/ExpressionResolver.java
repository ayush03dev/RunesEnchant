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
        Object eval = engine.eval(expression);

        if (eval instanceof Integer) {
            return (int) eval;
        }

        if (eval instanceof Double) {
            return (int) (double) eval;
        }

        if (eval instanceof Float) {
            return (int) (float) eval;
        }

        if (eval instanceof Long) {
            return (int) (long) eval;
        }

        return (int) (double) engine.eval(expression);
    }

    public float solveForFloat(String expression) throws ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        Object eval = engine.eval(expression);

        if (eval instanceof Integer) {
            return (int) eval;
        }

        if (eval instanceof Double) {
            return (float) (double) eval;
        }

        if (eval instanceof Float) {
            return (float) eval;
        }

        if (eval instanceof Long) {
            return (long) eval;
        }

        return (float) engine.eval(expression);
    }

//    public double solveForDouble(String expression) throws ScriptException {
//        ScriptEngineManager mgr = new ScriptEngineManager();
//        ScriptEngine engine = mgr.getEngineByName("JavaScript");
//        return (double) engine.eval(expression);
//    }
}
