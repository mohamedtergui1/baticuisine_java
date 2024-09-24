package main.java.com.app.utils;

import main.java.com.app.entities.Estimate;

import java.time.LocalDate;

public class ValidationUtils {

    public boolean isValidEstimate(Estimate estimate) {
        return isValidDate(estimate.getIssueDate()) &&
                isValidDate(estimate.getValidityDate()) &&
                areDatesInTheFuture(estimate) &&
                isValidityDateAfterIssueDate(estimate) &&
                isValidEstimatedAmount(estimate.getEstimatedAmount());
    }

    private boolean isValidDate(LocalDate date) {
        return date != null;
    }

    private boolean areDatesInTheFuture(Estimate estimate) {
        LocalDate today = LocalDate.now();
        return estimate.getIssueDate().isAfter(today) &&
                estimate.getValidityDate().isAfter(today);
    }

    private boolean isValidityDateAfterIssueDate(Estimate estimate) {
        return estimate.getValidityDate().isAfter(estimate.getIssueDate());
    }

    private boolean isValidEstimatedAmount(double estimatedAmount) {
        return estimatedAmount >= 0;
    }
}
