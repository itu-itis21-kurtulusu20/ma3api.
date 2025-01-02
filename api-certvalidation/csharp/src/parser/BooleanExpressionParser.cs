using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.common;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.parser
{
    public class BooleanExpressionParser
    {

        public bool parse(String expression, BooleanEvaluator evaluator)
        {
            try{
                String simple = simplify(expression, evaluator);
                return Boolean.Parse(simple);
            }catch (Exception exception){
                throw new ESYAException("Format yanlış girilmiş.");
            }
        }

        private String simplify(String expression, BooleanEvaluator evaluator)
        {

            if (expression == null || expression.Trim().Equals(""))
                return "";

            if (hasParenthesis(expression))
            {
                int startIndex = expression.IndexOf("(");
                int endIndex = expression.LastIndexOf(")");
                int length = endIndex - startIndex - 1;

                String preExpression = expression.Substring(0, startIndex);
                String middleExpression = expression.Substring(startIndex + 1, length);
                String postExpression = expression.Substring(endIndex + 1);
                String simple = simplify(middleExpression, evaluator);
                String newExpression = preExpression + simple + postExpression;
                return simplify(newExpression, evaluator);
            }

            List<String> parts = split(expression);
            parts = convertPartsToTrueFalseFormat(evaluator, parts);
            String evaluated = evaluate(parts);
            return evaluated;
        }

        private List<String> split(String expression)
        {

            List<String> list = new List<string>();
            while (true)
            {

                int andIndex = expression.IndexOf(" AND ");
                int orIndex = expression.IndexOf(" OR ");

                if (andIndex == -1)
                    andIndex = int.MaxValue;

                if (orIndex == -1)
                    orIndex = int.MaxValue;

                if (andIndex < orIndex)
                {
                    String preExpression = expression.Substring(0, andIndex).Trim();
                    expression = expression.Substring(andIndex + 5);


                    list.Add(preExpression);
                    list.Add("AND");
                }
                else if (orIndex < andIndex)
                {
                    String preExpression = expression.Substring(0, orIndex).Trim();
                    expression = expression.Substring(orIndex + 4);

                    list.Add(preExpression);
                    list.Add("OR");
                }
                else
                {
                    list.Add(expression.Trim());
                    break;
                }
            }
            return list;
        }

        private List<String> convertPartsToTrueFalseFormat(BooleanEvaluator evaluator, List<String> parts)
        {
            for (int i = 0; i < parts.Count; i++)
            {
                String statement = parts[i];
                parts[i] = evaluate(evaluator, statement);
            }

            return parts;
        }

        private String evaluate(BooleanEvaluator evaluator, String statement)
        {
            //FALSE, TRUE, AND, OR Durumlarında bir şey yapma.
            if (statement.Equals("false", StringComparison.OrdinalIgnoreCase) || statement.Equals("true", StringComparison.OrdinalIgnoreCase))
                return statement.ToUpper();
            if (statement.Equals("and", StringComparison.OrdinalIgnoreCase) || statement.Equals("or", StringComparison.OrdinalIgnoreCase))
                return statement.ToUpper();
            return ((Boolean)evaluator.evaluate(statement)).ToString().ToUpper();
        }

        //Bu fonksiyona sadece AND, OR, TRUE, FALSE text'lerinin gelmesini bekliyoruz.
        private String evaluate(List<String> parts)
        {
            while (parts.Count != 1)
            {
                if (parts.Count < 3) //Size'ın 2 olması durumu
                    throw new ESYAException("Not expected format.");

                String statement1 = parts[0];
                String operators = parts[1];
                String statement2 = parts[2];

                parts.RemoveRange(0,3);

                if (operators.Equals("AND", StringComparison.OrdinalIgnoreCase)){
                    Boolean result = Boolean.Parse(statement1) & Boolean.Parse(statement2);
                    parts.Insert(0, result.ToString().ToUpper());
                }else{
                    Boolean result = Boolean.Parse(statement1) | Boolean.Parse(statement2);
                    parts.Insert(0, result.ToString().ToUpper());
                }

            }
            return parts[0];
        }

        private bool hasParenthesis(String expression)
        {
            return expression.Contains("(") || expression.Contains(")");
        }

    }
}
