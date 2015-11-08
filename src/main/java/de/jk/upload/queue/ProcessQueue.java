package de.jk.upload.queue;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.jk.upload.command.ExternalCommand;

@Component
public class ProcessQueue {

    private final Map<Integer, ExternalCommand> allCommands = new LinkedHashMap<>();

    @Autowired
    private ProcessRunner runner;

    public void add(ExternalCommand command) {

        allCommands.put(command.getId(), command);
        runner.runCommand(command);
    }

    public Collection<ExternalCommand> getAll() {
        List<ExternalCommand> all = new LinkedList<>(allCommands.values());
        Collections.reverse(all);
        return all;
    }

    public ExternalCommand getById(int id) {
        return allCommands.get(id);
    }

    public void setRunner(ProcessRunner runner) {
        this.runner = runner;
    }
}
