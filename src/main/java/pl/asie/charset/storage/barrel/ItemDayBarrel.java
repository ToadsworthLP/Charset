/*
 * Copyright (c) 2016 neptunepink
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * Copyright (c) 2015-2016 Adrian Siekierka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.asie.charset.storage.barrel;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.asie.charset.lib.ModCharsetLib;

import java.util.List;

public class ItemDayBarrel extends ItemBlock {
    public ItemDayBarrel(Block block) {
        super(block);
        setUnlocalizedName("charset.barrel");
        setCreativeTab(ModCharsetLib.CREATIVE_TAB);
    }

    @Override
    public String getItemStackDisplayName(ItemStack is) {
        TileEntityDayBarrel.Type upgrade = TileEntityDayBarrel.getUpgrade(is);
        String lookup = "tile.charset.barrel.format";
        if (upgrade != TileEntityDayBarrel.Type.NORMAL) {
            lookup = "tile.charset.barrel.format2";
        }
        String type = I18n.translateToLocal("tile.charset.barrel." + upgrade + ".name");
        return I18n.translateToLocalFormatted(lookup, type, TileEntityDayBarrel.getLog(is).getDisplayName());
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        addExtraInformation(stack, playerIn, tooltip, advanced);
    }

    @SideOnly(Side.CLIENT) // Invokes a client-only function getTooltip
    protected void addExtraInformation(ItemStack is, EntityPlayer player, List<String> list, boolean verbose) {
        TileEntityDayBarrel.Type upgrade = TileEntityDayBarrel.getUpgrade(is);
        if (upgrade == TileEntityDayBarrel.Type.SILKY) {
            list.add(I18n.translateToLocal("tile.charset.barrel.SILKY.silkhint"));
            TileEntityDayBarrel db = new TileEntityDayBarrel();
            db.loadFromStack(is);
            int count = db.getItemCount();
            if (count > 0 && db.item != null) {
                if (db.item.getItem() == this) {
                    list.add("?");
                    return;
                }
                List<String> sub = db.item.getTooltip/* Client-only */(player, false /* Propagating verbose would be natural, but let's keep the tool-tip short */);
                db.item.getItem().addInformation(db.item, player, sub, verbose);
                if (!sub.isEmpty()) {
                    Object first = sub.get(0);
                    sub.set(0, count + " " + first);
                    list.addAll(sub);
                }
            }
        }
    }
}
