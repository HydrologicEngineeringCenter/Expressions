package usace.hec.expressions;

public final class EvaluationError {
    private ErrorState state = ErrorState.OK;
    private ExpressionNode node;    // first offender — diagnostics only
    private String message;

    public void report(ErrorState state, ExpressionNode node, String message) {
        this.state = state;
        this.node = node;
        this.message = message;
    }
    public boolean isOk(){ return state == ErrorState.OK; }
    public boolean warning(){return state == ErrorState.WARN;}
    public boolean isInvalid() { return state == ErrorState.INVALID; }

    public ExpressionNode getNode() {
        return node;
    }

    public void setNode(ExpressionNode node) {
        this.node = node;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}