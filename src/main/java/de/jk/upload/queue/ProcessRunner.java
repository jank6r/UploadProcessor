package de.jk.upload.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import de.jk.upload.command.ExternalCommandExecuter;
import de.jk.upload.command.ExternalCommand;

@Component
public class ProcessRunner {

    @Autowired
    private ExternalCommandExecuter executer;

    @Async
    public void runCommand(ExternalCommand command) {
        executer.execute(command);
    }

    public void setExecuter(ExternalCommandExecuter executer) {
        this.executer = executer;
    }

}
