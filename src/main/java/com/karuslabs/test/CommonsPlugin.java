/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.test;

import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.completion.Completion;
import com.karuslabs.commons.locale.providers.Provider;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.plugin.java.JavaPlugin;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;


public class CommonsPlugin extends JavaPlugin {
    
    private Commands commands;
    private Registry registry;
    
    
    @Override
    public void onEnable() {
        // TODO implement proper configuration file
        commands = new Commands(this, Provider.DETECTED);
        commands.load("commands.yml");
        registry = new Registry();
        
        Map<String, Command> subcommands = commands.getCommand("effect").getSubcommands();
        
        Command create = subcommands.get("create");
        create.setExecutor(new CreateCommand(this, registry));
        
        Map<Integer, Completion> completions = create.getCompletions();
        completions.put(0, 
            (sender, argument) -> registry.getEffects().keySet().stream().filter(name -> name.startsWith(argument)).collect(toList())
        );
        Completion random = (sender, argument) -> singletonList(String.valueOf(ThreadLocalRandom.current().nextInt(-999, 999)));
        completions.put(2, random);
        completions.put(3, random);
        completions.put(4, random);
        completions.put(5, random);
        completions.put(6, random);
        completions.put(7, random);
        
        Command cancel = subcommands.get("cancel");
        cancel.setExecutor(new CancelCommand(registry));
        cancel.getCompletions().put(0, 
                (sender, argument) -> registry.getScheduled().keySet().stream().filter(name -> name.startsWith(argument)).collect(toList())
        );
    }
    
}
