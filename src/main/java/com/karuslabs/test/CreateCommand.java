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

import com.karuslabs.commons.effect.Effect;
import com.karuslabs.commons.effect.SuppliedExecutor;
import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.arguments.*;
import com.karuslabs.commons.world.*;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.command.arguments.Matches.*;


public class CreateCommand implements CommandExecutor {
    
    private Plugin plugin;
    private Registry registry;
    private SuppliedExecutor executor;
    
    
    public CreateCommand(Plugin plugin, Registry registry) {
        this.plugin = plugin;
        this.registry = registry;
        this.executor = SuppliedExecutor.builder(plugin).async(true).delay(0).infinite().orientate(true).period(10).build();
    }
    
    
    @Override
    public boolean execute(CommandSource source, Context context, Arguments arguments) {
        if (!validate(source, context, arguments)) {
            return true;
        }
        
        BoundLocation origin = parse(source, context, arguments, 2, 5);
        BoundLocation target = parse(source, context, arguments, 5, 8);
        
        Effect effect = registry.getEffects().get(arguments.raw()[0]);  
        registry.getScheduled().put(arguments.raw()[1], executor.render(effect.get(), origin, target));
        
        source.sendColouredTranslation("create effect", arguments.raw()[0], arguments.raw()[1]);
        
        return true;
    }
    
    protected boolean validate(CommandSource source, Context context, Arguments arguments) {
        if (!source.isPlayer()) {
            source.sendColouredTranslation("invalid player");
            return false;
            
        } else if (arguments.length() < 2) {
            source.sendColouredTranslation("invalid arguments");
            return false;
            
        } else if (!arguments.at(0).match(registry.getEffects()::containsKey)) {
            source.sendColouredTranslation("invalid effect", arguments.raw()[0]);
            return false;
            
        } else if (arguments.at(1).match(registry.getScheduled()::containsKey)) {
            source.sendColouredTranslation("existing name", arguments.raw()[1]);
            return false;
            
        } else {
            return true;
        }
    }
    
    protected BoundLocation parse(CommandSource source, Context context, Arguments arguments, int first, int last) {
        if (arguments.match().between(first, last).exact(INT, INT, INT)) {
            return new StaticLocation(new Location(
                    source.asPlayer().getWorld(), 
                    arguments.at(first).as(Integer::parseInt),
                    arguments.at(first + 1).as(Integer::parseInt),
                    arguments.at(first + 2).as(Integer::parseInt)
            ), new Position(), true);
            
        } else {
            return LivingEntityLocation.builder(source.asPlayer()).nullable(true).relative(true).update(true).build();
        }
    }
    
}
