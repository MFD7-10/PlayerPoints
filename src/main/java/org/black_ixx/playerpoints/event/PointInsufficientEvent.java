package org.black_ixx.playerpoints.event;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player's balance is insufficient for a point deduction.
 * Listeners can supplement the player's balance and call {@link #setResolved(true)}
 * to allow PlayerPoints to recheck and complete the transaction automatically.
 */
public class PointInsufficientEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private static final ThreadLocal<Boolean> REENTRANT_GUARD = ThreadLocal.withInitial(() -> Boolean.FALSE);

    private final UUID playerUuid;
    private final int requiredAmount;
    private final int currentBalance;
    private final int missingAmount;
    private boolean cancelled;
    private boolean resolved;

    public PointInsufficientEvent(UUID playerUuid, int requiredAmount, int currentBalance) {
        super(!Bukkit.isPrimaryThread());
        this.playerUuid = playerUuid;
        this.requiredAmount = requiredAmount;
        this.currentBalance = currentBalance;
        this.missingAmount = requiredAmount - currentBalance;
    }

    public UUID getPlayerUuid() {
        return this.playerUuid;
    }

    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(this.playerUuid);
    }

    public int getRequiredAmount() {
        return this.requiredAmount;
    }

    public int getCurrentBalance() {
        return this.currentBalance;
    }

    public int getMissingAmount() {
        return this.missingAmount;
    }

    public boolean isResolved() {
        return this.resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static boolean isReentrant() {
        return REENTRANT_GUARD.get();
    }

    public static void setReentrant(boolean value) {
        REENTRANT_GUARD.set(value);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
