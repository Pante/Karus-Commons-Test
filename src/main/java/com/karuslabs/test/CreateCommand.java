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

import com.karuslabs.commons.animation.particles.effect.Effect;
import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.arguments.*;
import com.karuslabs.commons.world.*;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.command.arguments.Matches.*;


public class CreateCommand implements CommandExecutor {
    
    private Plugin plugin;
    private Registry registry;
    
    
    public CreateCommand(Plugin plugin, Registry registry) {
        this.plugin = plugin;
        this.registry = registry;
    }
    
    
    @Override
    public boolean execute(Context context, Arguments arguments) {
        if (!validate(context, arguments)) {
            return true;
        }
        
        BoundLocation origin = parse(context, arguments, 2, 5);
        BoundLocation target = parse(context, arguments, 5, 8);
        
        Effect effect = Effect.builder(plugin).async(true).delay(0).infinite().orientate(true).period(10).task(registry.getEffects().get(arguments.text()[0])).build();
        registry.getScheduled().put(arguments.text()[1], effect.render(origin, target));
        
        context.getSender().sendMessage(context.translate("create effect", arguments.text()[0], arguments.text()[1]));
        
        return true;
    }
    
    protected boolean validate(Context context, Arguments arguments) {
        if (!context.isPlayer()) {
            context.getSender().sendMessage(context.translate("invalid player"));
            return false;
            
        } else if (arguments.length() < 2) {
            context.getSender().sendMessage(context.translate("invalid arguments"));
            return false;
            
        } else if (!arguments.get(0).match(registry.getEffects()::containsKey)) {
            context.getSender().sendMessage(context.translate("invalid effect", arguments.text()[0]));
            return false;
            
        } else if (arguments.get(1).match(registry.getScheduled()::containsKey)) {
            context.getSender().sendMessage(context.translate("existing name", arguments.text()[1]));
            return false;
            
        } else {
            return true;
        }
    }
    
    protected BoundLocation parse(Context context, Arguments arguments, int first, int last) {
        if (arguments.length() >= last && arguments.match().between(first, last).exact(INT, INT, INT)) {
            return new StaticLocation(new Location(
                    context.getPlayer().getWorld(), 
                    arguments.get(first).as(Integer::parseInt),
                    arguments.get(first + 1).as(Integer::parseInt),
                    arguments.get(first + 2).as(Integer::parseInt)
            ), new PathVector(), true);
            
        } else {
            return LivingEntityLocation.builder(context.getPlayer()).nullable(true).relative(true).update(true).build();
        }
    }
    
}
