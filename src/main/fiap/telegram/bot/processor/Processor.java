package main.fiap.telegram.bot.processor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import main.fiap.telegram.bot.command.Command;
import main.fiap.telegram.bot.command.HelpCommand;
import main.fiap.telegram.bot.command.PlayCommand;
import main.fiap.telegram.bot.command.SearchCommand;

public class Processor {
    public class Execution {
        public Command command;
        public String args;

        public Execution(Command command, String args) {
            this.command = command;
            this.args = args;
        }
    }
    private final ArrayList<Command> commands;

    private static ArrayList<String> addCommandsFromClass(ArrayList<Command> commands, Class c) {
        ArrayList<String> errors = new ArrayList<>();
        Field[] searchCommands;
        try {
            searchCommands = c.getDeclaredFields();
        } catch (Exception e) {
            errors.add("Error get declared fields from " + c.getName());
            return errors;
        }

        for (Field field : searchCommands) {
            try {
                if (Modifier.isStatic(field.getModifiers())
                        && Command.class.isAssignableFrom(field.getType())) {
                    commands.add((Command) field.get(null));
                }
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Error get command from " + c.getName());
                sb.append(System.lineSeparator());
                sb.append("With name " + field.getName());
                sb.append(System.lineSeparator());
                sb.append(e.getMessage());

                errors.add(sb.toString());
            }
        }
        return errors;
    }

    public Processor() {
        this.commands = new ArrayList<>();
        ArrayList<String>  errors = new ArrayList<>();
        errors.addAll(Processor.addCommandsFromClass(this.commands, SearchCommand.class));
        errors.addAll(Processor.addCommandsFromClass(this.commands, PlayCommand.class));
        errors.addAll(Processor.addCommandsFromClass(this.commands, HelpCommand.class));
        for(String error : errors) {
            System.out.println(error);
        }
    }

    public Execution process(String message) {
        for(Command command : this.commands) {
            String args = command.check(message);
            if(args != null)
                return new Execution(command, args);
        }

        return null;
    }
}