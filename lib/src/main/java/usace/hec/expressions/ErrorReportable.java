package usace.hec.expressions;

public interface ErrorReportable {
    EvaluationError getEvaluationError();
    void setErrorChannel(ErrorChannel channel);
}
