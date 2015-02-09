/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered.org <http://www.spongepowered.org>
 * Copyright (c) contributors
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
package org.spongepowered.mod.mixin.entity.living;

import com.google.common.base.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.entity.living.Sittable;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@NonnullByDefault
@Mixin(EntityTameable.class)
@Implements(@Interface(iface = Sittable.class, prefix = "sittable$"))
public abstract class MixinEntityTameable extends EntityAnimal {

    @Shadow
    public abstract void func_175544_ck();

    @Shadow
    public abstract EntityLivingBase func_180492_cm();

    @Shadow
    public abstract Entity getOwner();

    @Shadow
    public abstract void func_152115_b(String name);

    private Tamer tamer;

    public MixinEntityTameable(World worldIn) {
        super(worldIn);
    }

    public boolean sittable$isSitting() {
        return this.isSitting();
    }

    @Overwrite
    public boolean isSitting() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void sittable$setSitting(boolean sitting) {
        this.setSitting(sitting);
    }

    @Overwrite
    public void setSitting(boolean sitting) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (sitting) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 1)));
        } else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & -2)));
        }
    }

    public boolean sittable$isTamed() {
        return this.isTamed();
    }

    @Overwrite
    public boolean isTamed() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 4) != 0;
    }

    public void sittable$setTamed(boolean tamed) {
        this.setTamed(tamed);
    }

    @Overwrite
    public void setTamed(boolean tamed) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (tamed) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 4)));
        } else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & -5)));
        }

        this.func_175544_ck();
    }

    public Optional<Tamer> sittable$getOwner() {
        if (this.getOwner() != null) {
            return Optional.fromNullable((Tamer) this.getOwner());
        }
        return Optional.fromNullable(this.tamer);
    }

    public void sittable$setOwner(Tamer tamer) {
        if (tamer instanceof EntityPlayer) {
            this.func_152115_b(((EntityPlayer) tamer).getUniqueID().toString());
            this.tamer = null;
        } else {
            this.func_152115_b(null);
            this.tamer = tamer;
        }
    }
}