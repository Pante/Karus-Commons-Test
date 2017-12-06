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

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static java.util.stream.Collectors.toList;


public class CommonsPlugin extends JavaPlugin {
    
    private Commands commands;
    private Registry registry;
    
    
    @Override
    public void onEnable() {
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
        completions.put(2, completion(player -> player.getLocation().getBlockX()));
        completions.put(3, completion(player -> player.getLocation().getBlockY()));
        completions.put(4, completion(player -> player.getLocation().getBlockZ()));
        
        completions.put(5, completion(player -> player.getLocation().getBlockX()));
        completions.put(6, completion(player -> player.getLocation().getBlockY()));
        completions.put(7, completion(player -> player.getLocation().getBlockZ()));
        
        Command cancel = subcommands.get("cancel");
        cancel.setExecutor(new CancelCommand(registry));
        cancel.getCompletions().put(0, 
            (sender, argument) -> registry.getScheduled().keySet().stream().filter(name -> name.startsWith(argument)).collect(toList())
        );
    }
    
    
    protected Completion completion(Function<Player, Integer> function) {
        return (sender, argument) -> {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                return asList(String.valueOf(function.apply(player) + ThreadLocalRandom.current().nextInt(-9, 9)));
                
            } else {
                return EMPTY_LIST;
            }
        };
    }
    
}
