package ru.tr1al.web.beans;

import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;

public class AjaxResult {

    private boolean success;
    private List<FieldError> fieldErrors;
    private Map<Object, Object> params;

    public AjaxResult(boolean success) {
        this.success = success;
    }


    public AjaxResult(boolean success, List<FieldError> fieldErrors) {
        this.success = success;
        this.fieldErrors = fieldErrors;
    }

    public AjaxResult(boolean success, Map<Object, Object> params) {
        this.success = success;
        this.params = params;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public Map<Object, Object> getParams() {
        return params;
    }

    public void setParams(Map<Object, Object> params) {
        this.params = params;
    }
}
