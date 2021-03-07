package me.ayushdev.runesenchant.utils;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ExpressionResolver {

    private static final ExpressionResolver instance = new ExpressionResolver();

    public static ExpressionResolver getInstance() {
        return instance;
    }

//    public int solve(String expression) throws ScriptException {
//        ScriptEngineManager mgr = new ScriptEngineManager();
//        ScriptEngine engine = mgr.getEngineByName("JavaScript");
//        Object eval = engine.eval(expression);
//
//        if (eval instanceof Integer) {
//            return (int) eval;
//        }
//
//        if (eval instanceof Double) {
//            return (int) (double) eval;
//        }
//
//        if (eval instanceof Float) {
//            return (int) (float) eval;
//        }
//
//        if (eval instanceof Long) {
//            return (int) (long) eval;
//        }
//
//        return (int) (double) engine.eval(expression);
//    }

    public int solve(String expression) {
        DoubleEvaluator eval = new DoubleEvaluator();
        double ans = eval.evaluate(expression);
        return (int) ans;
    }

    public float solveForFloat(String expression) {
        DoubleEvaluator eval = new DoubleEvaluator();
        double ans = eval.evaluate(expression);
        return (float) ans;

    }

//    public float solveForFloat(String expression) throws ScriptException {
//
////        if (expression == null) {
////            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "Expression is null!");
////        } else {
////            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "" + expression);
////            ScriptEngineManager mgr = new ScriptEngineManager();
////            ScriptEngine engine = mgr.getEngineByName("JavaScript");
////            if (engine == null) {
////                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Engine is null!");
////            } else {
////                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Engine is working!");
////
////            }
////            return 0;
////        }
//
//        ScriptEngineManager mgr = new ScriptEngineManager();
//        ScriptEngine engine = mgr.getEngineByName("JavaScript");
//        Object eval = engine.eval(expression);
//
//        if (eval instanceof Integer) {
//            return (int) eval;
//        }
//
//        if (eval instanceof Double) {
//            return (float) (double) eval;
//        }
//
//        if (eval instanceof Float) {
//            return (float) eval;
//        }
//
//        if (eval instanceof Long) {
//            return (long) eval;
//        }
//
//        return (float) engine.eval(expression);
//    }

//    public double solveForDouble(String expression) throws ScriptException {
//        ScriptEngineManager mgr = new ScriptEngineManager();
//        ScriptEngine engine = mgr.getEngineByName("JavaScript");
//        return (double) engine.eval(expression);
//    }
}
