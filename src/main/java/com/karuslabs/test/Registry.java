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
import com.karuslabs.commons.effect.effects.*;
import com.karuslabs.commons.effect.particles.*;
import com.karuslabs.commons.util.concurrent.Result;

import java.util.*;

import org.bukkit.*;


public class Registry {
    
    private static final Particles ENCHANTMENT = StandardParticles.builder().particle(Particle.ENCHANTMENT_TABLE).amount(1).speed(0).build();
    private static final Particles FLAME = StandardParticles.builder().particle(Particle.FLAME).amount(1).speed(0).build();
    private static final Particles PORTAL = StandardParticles.builder().particle(Particle.PORTAL).amount(1).speed(0).build();
    private static final Particles NOTE = StandardParticles.builder().particle(Particle.NOTE).amount(1).speed(0).build();
    private static final Particles WATER = StandardParticles.builder().particle(Particle.DRIP_WATER).amount(1).speed(0).build();
    
    private static final Particles WHITE = ColouredParticles.builder().particle(Particle.SPELL).amount(1).colour(Color.WHITE).build();
    
    private Map<String, Effect> effects;
    private Map<String, Result<Void>> scheduled;
            
            
    public Registry() {
        effects = new HashMap<>();
        scheduled = new HashMap<>();
        
        effects.put("animatedball", new AnimatedBall(FLAME));
        effects.put("arc", new Arc(FLAME));
        effects.put("atom", new Atom(FLAME, ENCHANTMENT));
        effects.put("cloud", new Cloud(WHITE, WATER));
        effects.put("cone", new Cone(FLAME));
        effects.put("cube", new Cube(FLAME));
        effects.put("cylinder", new Cylinder(FLAME));
        effects.put("dna", new DNA(FLAME, ENCHANTMENT, PORTAL));
        effects.put("discoball", new DiscoBall(FLAME, ENCHANTMENT));
        effects.put("donut", new Donut(FLAME));
        effects.put("dragon", new Dragon(FLAME));
        effects.put("flame", new Flame(FLAME));
        effects.put("fountain", new Fountain(WATER));
        effects.put("grid", new Grid(FLAME));
        effects.put("heart", new Heart(FLAME));
        effects.put("hill", new Hill(FLAME));
        effects.put("line", new Line(FLAME));
        effects.put("shield", new Shield(FLAME));
        effects.put("smoke", new Smoke(FLAME));
        effects.put("sphere", new Sphere(FLAME));
        effects.put("spiral", new Spiral(FLAME));
        effects.put("star", new Star(FLAME));
        effects.put("tornado", new Tornado(ENCHANTMENT, WATER));
        effects.put("trace", new Trace(ENCHANTMENT));
        effects.put("vortex", new Vortex(PORTAL));
        effects.put("warp", new Warp(PORTAL));
        effects.put("wave", new Wave(WATER, ENCHANTMENT));
    }
    
    
    public Map<String, Effect> getEffects() {
        return effects;
    }
    
    public Map<String, Result<Void>> getScheduled() {
        return scheduled;
    }
    
}
