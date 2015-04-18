/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
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
package org.spongepowered.mod.mixin.core.block.data;

import com.google.common.base.Optional;
import org.spongepowered.api.block.tile.EndPortal;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.data.DataPriority;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.Property;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collection;

@NonnullByDefault
@Implements(@Interface(iface = EndPortal.class, prefix = "endportal$"))
@Mixin(net.minecraft.tileentity.TileEntityEndPortal.class)
public abstract class MixinTileEntityEndPortal extends MixinTileEntity {

    @Override
    public <T extends DataManipulator<T>> boolean remove(Class<T> manipulatorClass) {
        return super.remove(manipulatorClass);
    }

    @Override
    public <T extends DataManipulator<T>> boolean isCompatible(Class<T> manipulatorClass) {
        return super.isCompatible(manipulatorClass);
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(T manipulatorData) {
        return super.offer(manipulatorData);
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(T manipulatorData, DataPriority priority) {
        return super.offer(manipulatorData, priority);
    }

    @Override
    public Collection<? extends DataManipulator<?>> getManipulators() {
        return super.getManipulators();
    }

    @Override
    public <T extends Property<?, ?>> Optional<T> getProperty(Class<T> propertyClass) {
        return super.getProperty(propertyClass);
    }

    @Override
    public Collection<? extends Property<?, ?>> getProperties() {
        return super.getProperties();
    }

    @Override
    public boolean validateRawData(DataContainer container) {
        return super.validateRawData(container);
    }

    @Override
    public void setRawData(DataContainer container) throws InvalidDataException {
        super.setRawData(container);
    }
}
