/*
 *     Copyright (C) 2017-Present HealPot
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.pixeltime.enchantmentsenhance.event.enchantment.gear

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffectType
import org.pixeltime.enchantmentsenhance.listener.EnchantmentListener

class Eyepatch : EnchantmentListener() {
    override fun desc(): Array<String> {
        return arrayOf("Immune to blindness", "无视失明效果")
    }

    override fun lang(): Array<String> {
        return arrayOf("天眼")
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onMove(playerMoveEvent: PlayerMoveEvent) {
        val player = playerMoveEvent.player
        if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
            try {
                val level = getLevel(player)
                if (level > 0) {
                    player.removePotionEffect(PotionEffectType.BLINDNESS)
                }
            } catch (ex: Exception) {
            }
        }
    }
}
