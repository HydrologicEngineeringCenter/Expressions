package usace.hec.expressions;

public interface ErrorReportable {
    EvaluationError getEvaluationError();
    default boolean hasError(){
        return !getEvaluationError().isOk();
    }
}
