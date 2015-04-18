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
package org.spongepowered.mod.data;

import com.google.common.base.Optional;
import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.data.DataManipulatorBuilder;
import org.spongepowered.api.data.DataManipulatorRegistry;

import java.util.HashMap;

public class SpongeManipulatorRegistry implements DataManipulatorRegistry {

    private static final SpongeManipulatorRegistry instance = new SpongeManipulatorRegistry();

    private SpongeManipulatorRegistry() {
    }

    public static SpongeManipulatorRegistry getInstance() {
        return instance;
    }

    // TODO use a better implementation of this.
    private HashMap<Class, DataManipulatorBuilder> cache = new HashMap();

    @Override
    public <T extends DataManipulator<T>> void register(Class<T> manipulatorClass, DataManipulatorBuilder<T> builder) {
        cache.put(manipulatorClass, builder);
    }

    @Override
    public <T extends DataManipulator<T>> Optional<DataManipulatorBuilder<T>> getBuilder(Class<T> manipulatorClass) {
        return Optional.<DataManipulatorBuilder<T>>fromNullable((DataManipulatorBuilder<T>) cache.get(manipulatorClass));
    }
}
