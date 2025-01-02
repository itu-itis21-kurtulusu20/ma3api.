package tr.gov.tubitak.uekae.esya.api.certificate.validation.parser;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.ArrayList;
import java.util.List;

public class BooleanExpressionParser {

    public boolean parse(String expression, BooleanEvaluator evaluator) throws ESYAException {
        try {
            String simple = simplify(expression, evaluator);
            return Boolean.parseBoolean(simple);
        } catch (Exception exception) {
            throw new ESYAException("Format yanlış girilmiş.");
        }
    }

    private String simplify(String expression, BooleanEvaluator evaluator) throws ESYAException {

        if (expression == null || expression.trim().equals(""))
            return "";

        if (hasParenthesis(expression)) {
            int startIndex = expression.indexOf("(");
            int endIndex = expression.lastIndexOf(")");
            String preExpression = expression.substring(0, startIndex);
            String middleExpression = expression.substring(startIndex + 1, endIndex);
            String postExpression = expression.substring(endIndex + 1);
            String simple = simplify(middleExpression, evaluator);
            String newExpression = preExpression + simple + postExpression;
            return simplify(newExpression, evaluator);
        }

        List<String> parts = split(expression);
        parts = convertPartsToTrueFalseFormat(evaluator, parts);
        String evaluated = evaluate(parts);
        return evaluated;
    }

    private List<String> split(String expression) {

        List<String> list = new ArrayList<>();
        while (true) {

            int andIndex = expression.indexOf(" AND ");
            int orIndex = expression.indexOf(" OR ");

            if (andIndex == -1)
                andIndex = Integer.MAX_VALUE;

            if (orIndex == -1)
                orIndex = Integer.MAX_VALUE;

            if (andIndex < orIndex) {
                String preExpression = expression.substring(0, andIndex).trim();
                expression = expression.substring(andIndex + 5);

                list.add(preExpression);
                list.add("AND");
            } else if (orIndex < andIndex) {
                String preExpression = expression.substring(0, orIndex).trim();
                expression = expression.substring(orIndex + 4);

                list.add(preExpression);
                list.add("OR");
            } else {
                list.add(expression.trim());
                break;
            }
        }
        return list;
    }

    private List<String> convertPartsToTrueFalseFormat(BooleanEvaluator evaluator, List<String> parts) {
        for (int i = 0; i < parts.size(); i++) {
            String statement = parts.get(i);
            parts.set(i, evaluate(evaluator, statement));
        }

        return parts;
    }

    private String evaluate(BooleanEvaluator evaluator, String statement) {
        //FALSE, TRUE, AND, OR Durumlarında bir şey yapma.
        if (statement.equalsIgnoreCase("false") || statement.equalsIgnoreCase("true"))
            return statement.toUpperCase();
        if (statement.equalsIgnoreCase("and") || statement.equalsIgnoreCase("or"))
            return statement.toUpperCase();
        return ((Boolean) evaluator.evaluate(statement)).toString().toUpperCase();
    }

    //Bu fonksiyona sadece AND, OR, TRUE, FALSE text'lerinin gelmesini bekliyoruz.
    private String evaluate(List<String> parts) throws ESYAException {
        while (parts.size() != 1) {
            if (parts.size() < 3) //Size'ın 2 olması durumu
                throw new ESYAException("Not expected format.");
            String statement1 = parts.remove(0);
            String operator = parts.remove(0);
            String statement2 = parts.remove(0);

            if (operator.equalsIgnoreCase("AND")) {
                Boolean result = Boolean.parseBoolean(statement1) & Boolean.parseBoolean(statement2);
                parts.add(0, result.toString().toUpperCase());
            } else if (operator.equalsIgnoreCase("OR")) {
                Boolean result = Boolean.parseBoolean(statement1) | Boolean.parseBoolean(statement2);
                parts.add(0, result.toString().toUpperCase());
            }else {
                throw new ESYAException("Not expected format.");
            }
        }

        return parts.get(0);
    }

    private boolean hasParenthesis(String expression) {
        return expression.contains("(") || expression.contains(")");
    }
}