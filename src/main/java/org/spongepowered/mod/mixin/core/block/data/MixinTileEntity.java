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

import static org.spongepowered.api.data.DataQuery.of;

import com.google.common.base.Optional;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.util.Constants;
import org.spongepowered.api.block.tile.TileEntity;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.data.DataManipulatorBuilder;
import org.spongepowered.api.data.DataPriority;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.Property;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.mod.data.SpongeManipulatorRegistry;
import org.spongepowered.mod.interfaces.IMixinDataHolder;
import org.spongepowered.mod.service.persistence.NbtTranslator;
import org.spongepowered.mod.util.VecHelper;

import java.util.Collection;

@NonnullByDefault
@Mixin(net.minecraft.tileentity.TileEntity.class)
public abstract class MixinTileEntity implements TileEntity, IMixinDataHolder {

    @Shadow protected net.minecraft.world.World worldObj;
    @Shadow public abstract BlockPos getPos();
    @Shadow public abstract void markDirty();
    @Shadow(remap = false)
    public abstract NBTTagCompound getTileData();

    // TODO until we actually have a better solution for storing custom data....
    protected DataContainer customData = new MemoryDataContainer();

    @Override
    public Location getBlock() {
        return new Location((World) this.worldObj, VecHelper.toVector(this.getPos()).toDouble());
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = new MemoryDataContainer();
        container.set(of("world"), ((World) this.worldObj).getName());
        container.set(of("x"), this.getPos().getX());
        container.set(of("y"), this.getPos().getY());
        container.set(of("z"), this.getPos().getZ());
        container.set(of("tileType"), net.minecraft.tileentity.TileEntity.classToNameMap.get(this.getClass()));
        return container;
    }

    @Override
    public <T extends DataManipulator<T>> Optional<T> getData(Class<T> dataClass) {
        return getOrCreate(dataClass);
    }

    @Override
    public <T extends DataManipulator<T>> Optional<T> getOrCreate(Class<T> manipulatorClass) {
        Optional<DataManipulatorBuilder<T>> builderOptional = SpongeManipulatorRegistry.getInstance().getBuilder(manipulatorClass);
        if (builderOptional.isPresent()) {
            return builderOptional.get().createFrom(this);
        }
        return Optional.absent();
    }

    @Override
    public <T extends DataManipulator<T>> boolean remove(Class<T> manipulatorClass) {

        return false;
    }

    @Override
    public <T extends DataManipulator<T>> boolean isCompatible(Class<T> manipulatorClass) {
        return false;
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(T manipulatorData) {
        return offer(manipulatorData, DataPriority.DATA_MANIPULATOR);
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(T manipulatorData, DataPriority priority) {

        return null;
    }

    @Override
    public Collection<? extends DataManipulator<?>> getManipulators() {
        return null;
    }

    @Override
    public <T extends Property<?, ?>> Optional<T> getProperty(Class<T> propertyClass) {
        return null;
    }

    @Override
    public Collection<? extends Property<?, ?>> getProperties() {
        return null;
    }

    @Override
    public boolean validateRawData(DataContainer container) {
        return false;
    }

    @Override
    public void setRawData(DataContainer container) throws InvalidDataException {

    }

    /**
     * Hooks into vanilla's writeToNBT to call {@link #writeToNbt}.
     *
     * <p>
     * This makes it easier for other entity mixins to override writeToNBT
     * without having to specify the <code>@Inject</code> annotation.
     * </p>
     *
     * @param compound The compound vanilla writes to (unused because we write
     *        to SpongeData)
     * @param ci (Unused) callback info
     */
    @Inject(method = "Lnet/minecraft/tileentity/TileEntity;writeToNBT(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At("HEAD"))
    public void onWriteToNBT(NBTTagCompound compound, CallbackInfo ci) {
        this.writeToNbt(this.getSpongeData());
    }

    /**
     * Hooks into vanilla's readFromNBT to call {@link #readFromNbt}.
     *
     * <p>
     * This makes it easier for other entity mixins to override readFromNbt
     * without having to specify the <code>@Inject</code> annotation.
     * </p>
     *
     * @param compound The compound vanilla reads from (unused because we read
     *        from SpongeData)
     * @param ci (Unused) callback info
     */
    @Inject(method = "Lnet/minecraft/tileentity/TileEntity;readFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At("RETURN"))
    public void onReadFromNBT(NBTTagCompound compound, CallbackInfo ci) {
        this.readFromNbt(this.getSpongeData());
    }

    /**
     * Gets the SpongeData NBT tag, used for additional data not stored in the
     * vanilla tag.
     *
     * <p>
     * Modifying this tag will affect the data stored.
     * </p>
     *
     * @return The data tag
     */
    public final NBTTagCompound getSpongeData() {
        NBTTagCompound data = this.getTileData();
        if (!data.hasKey("SpongeData", Constants.NBT.TAG_COMPOUND)) {
            data.setTag("SpongeData", new NBTTagCompound());
        }
        return data.getCompoundTag("SpongeData");
    }

    /**
     * Read extra data (SpongeData) from the tile entity's NBT tag.
     *
     * @param compound The SpongeData compound to read from
     */
    public void readFromNbt(NBTTagCompound compound) {
        NbtTranslator.getInstance().translateContainerToData(compound.getCompoundTag("CustomData"), this.customData);
    }

    /**
     * Write extra data (SpongeData) to the tile entity's NBT tag.
     *
     * @param compound The SpongeData compound to write to
     */
    public void writeToNbt(NBTTagCompound compound) {
        NBTTagCompound customDataCompound = NbtTranslator.getInstance().translateData(this.customData);
        compound.setTag("CustomData", customDataCompound);
    }

    @Override
    public DataView getCustomDataView() {
        return this.customData;
    }
}
