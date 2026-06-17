package usace.hec.expressions;

public record DataUpdate<T>(String variableName, T newValue) {}