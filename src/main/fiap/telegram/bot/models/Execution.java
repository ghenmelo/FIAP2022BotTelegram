package main.fiap.telegram.bot.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.fiap.telegram.bot.command.Command;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Execution {
    public Command command;
    public String args;
}
