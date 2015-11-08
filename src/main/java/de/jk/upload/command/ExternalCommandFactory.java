package de.jk.upload.command;

import org.springframework.stereotype.Component;

@Component
public class ExternalCommandFactory {

    private int sequence = 0;

    public ExternalCommand create(String... commandStr) {
        ExternalCommand command = new ExternalCommand(commandStr);
        command.setId(sequence++);

        return command;
    }

}
