package br.edu.ifmg.produto.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {
    private List<FieldMessage> fieldMessages = new ArrayList<FieldMessage>();

    public ValidationError() {}

    public List<FieldMessage> getFieldMessages () {
        return fieldMessages;
    }

    public void setFieldMessages (List<FieldMessage> fields) {
        this.fieldMessages = fields;
    }

    public void addFieldMessage (String field, String message) {
        this.fieldMessages.add(new FieldMessage(field, message));
    }
}
