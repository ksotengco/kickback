package edu.cs371m.kickback.listener;

public interface FieldValidator {
    boolean isValid();
    boolean onInvalid();
    boolean onValid();
}
