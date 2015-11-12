package de.jk.upload.command;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExternalCommandFactory {

    private int sequence = 0;
    private File workingDirectory;

    @Value("${uploadPath}")
    public void setWorkingDirectoryPath(String path) {
        workingDirectory = new File(path);
    }

    public ExternalCommand create(String... commandStr) {
        ExternalCommand command = new ExternalCommand(commandStr);
        command.setId(sequence++);
        command.setWorkingDirectory(workingDirectory);

        return command;
    }

}
