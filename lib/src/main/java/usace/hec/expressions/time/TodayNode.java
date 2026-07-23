package usace.hec.expressions.time;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import usace.hec.expressions.DataListener;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.LeafNode;

public class TodayNode implements ExpressionNode<LocalDate>, LeafNode<LocalDate> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public LocalDate evaluate() {
        LocalDate t = LocalDate.now();
        return t;
    }

    @Override
    public List<DataListener<?>> fetchListeners() {
        return new ArrayList<>();
    }



    @Override
    public String PreFixSyntax(){
        return ExpressionOperator.TODAY.getInfixName();
    }
    @Override
    public String ExcelSyntax(){
        return ExpressionOperator.TODAY.getInfixName();
    }

    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.TODAY;
    }
}
