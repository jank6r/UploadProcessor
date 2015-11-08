package de.jk.upload.command;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

public class ExternalCommand {

    public enum Status {
        FAILURE, PENDING, RUNNING, SUCCESS;
    }

    private final String[] command;
    private final Date creationDate;
    private int exitCode = -1;
    private int id;
    private final StringBuilder output = new StringBuilder();

    private Status status = Status.PENDING;
    private File workingDirectory = new File(".");

    ExternalCommand(String[] command) {

        this.command = command;
        this.creationDate = new Date();
    }

    public String[] getCommand() {
        return command;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public int getExitCode() {
        return exitCode;
    }

    public int getId() {
        return id;
    }

    public String getOutput() {
        return output.toString();
    }

    public Status getStatus() {
        return status;
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    @Override
    public String toString() {
        return "ExternalCommand [id=" + id + ", command=" + Arrays.toString(command) + ", status=" + status + "]";
    }

    protected void appendOutput(String out) {
        output.append(out);
    }

    protected void setExitValue(int exitValue) {
        this.exitCode = exitValue;
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected void setStatus(Status status) {
        this.status = status;
    }

    protected void setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

}
