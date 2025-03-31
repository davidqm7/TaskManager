package taskmanager.models;

public enum Status {
    TODO("To do"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed");

    private String displayText;

    Status(String displayText)
    {
        this.displayText = displayText;
    }
}


