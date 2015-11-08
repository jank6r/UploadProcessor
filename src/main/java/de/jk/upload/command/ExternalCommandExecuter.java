package de.jk.upload.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.jk.upload.UploadException;
import de.jk.upload.command.ExternalCommand.Status;

@Component
public class ExternalCommandExecuter {
    private static final Logger LOG = LoggerFactory.getLogger(ExternalCommandExecuter.class);

    public void execute(ExternalCommand command) {
        LOG.info("execute {}", command);

        try {

            ProcessBuilder builder = new ProcessBuilder(command.getCommand()).directory(command.getWorkingDirectory())
                    .redirectErrorStream(true);

            Process process = builder.start();
            command.setStatus(Status.RUNNING);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()), 80);

            String line = "";
            while ((line = reader.readLine()) != null) {
                command.appendOutput(line + "\n");
            }

            process.waitFor();

            command.setExitValue(process.exitValue());

        } catch (IOException | InterruptedException e) {
            throw new UploadException(
                    String.format("running process %d failed: '%s'", command.getId(), command.getCommand()), e);
        } finally {
            command.setStatus(command.getExitCode() == 0 ? Status.SUCCESS : Status.FAILURE);
        }

        LOG.info("done    {}", command);
    }

}
