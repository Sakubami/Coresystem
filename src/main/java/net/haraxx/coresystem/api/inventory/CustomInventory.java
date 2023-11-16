package net.haraxx.coresystem.api.inventory;

import net.haraxx.coresystem.CoreSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.stream.IntStream;

/**
 * @author Juyas
 * @version 16.11.2023
 * @since 16.11.2023
 */
public class CustomInventory implements Listener
{

    private InventoryItem[] activeItems;
    private InventoryMask mask;
    private final Inventory inventory;
    private final String title;
    private boolean keepOpen;
    private boolean blockPlayerInventory;

    private Runnable onOpen;
    private Runnable onClose;

    private Player activePlayer;
    private InventoryView openedView;
    private boolean opened;


    public CustomInventory( Player owner, String title, InventoryMask mask )
    {
        this.mask = mask;
        this.title = title;
        this.inventory = Bukkit.createInventory( owner, mask.size(), title );
        update();
    }

    public InventoryMask getMask()
    {
        return mask;
    }

    public void setMask( InventoryMask mask )
    {
        this.mask = mask;
    }

    public Inventory getInventory()
    {
        return inventory;
    }

    public InventoryView getOpenedView()
    {
        return openedView;
    }

    public boolean isOpened()
    {
        return opened;
    }

    public boolean isKeepOpen()
    {
        return keepOpen;
    }

    public String getTitle()
    {
        return title;
    }

    public Player getActivePlayer()
    {
        return activePlayer;
    }

    public CustomInventory keepOpen( boolean keepOpen )
    {
        this.keepOpen = keepOpen;
        return this;
    }

    public CustomInventory blockPlayerInventory( boolean blockPlayerInventory )
    {
        this.blockPlayerInventory = blockPlayerInventory;
        return this;
    }

    public CustomInventory onOpen( Runnable runnable )
    {
        Runnable old = this.onOpen;
        this.onOpen = this.onOpen == null ? runnable : () -> {
            old.run();
            runnable.run();
        };
        return this;
    }

    public CustomInventory onClose( Runnable runnable )
    {
        Runnable old = this.onClose;
        this.onClose = this.onClose == null ? runnable : () -> {
            old.run();
            runnable.run();
        };
        return this;
    }

    public void update()
    {
        this.mask.apply( activeItems );
        IntStream.range( 0, activeItems.length ).forEach( this::updateItem );
    }

    public void updateItem( int slot )
    {
        this.inventory.setItem( slot, activeItems[slot].display().get() );
    }

    public boolean open( Player player )
    {
        if ( opened ) return false;
        this.activePlayer = player;
        Bukkit.getPluginManager().registerEvents( this, CoreSystem.getInstance() );
        openedView = player.openInventory( inventory );
        return true;
    }

    private void closed()
    {
        this.opened = false;
        this.activePlayer = null;
        this.openedView = null;
        HandlerList.unregisterAll( this );
    }

    public void close()
    {
        if ( opened )
            activePlayer.closeInventory();
    }

    private boolean invalidateInventory( InventoryView view )
    {
        return !view.getTitle().equals( title );
    }

    @EventHandler
    public void onClose( InventoryCloseEvent event )
    {
        if ( invalidateInventory( event.getView() ) ) return;
        if ( keepOpen )
        {
            event.getPlayer().openInventory( event.getView() );
            return;
        }
        onClose.run();
        closed();
    }

    @EventHandler
    public void onOpen( InventoryOpenEvent event )
    {
        if ( !event.getView().getTitle().equals( title ) || !activePlayer.getUniqueId().equals( event.getPlayer().getUniqueId() ) )
            return;
        if ( !this.opened )
        {
            onOpen.run();
        }
        this.opened = true;
    }

    @EventHandler
    public void onClick( InventoryClickEvent event )
    {
        if ( invalidateInventory( event.getView() ) || !this.opened ) return;
        if ( !event.getView().getTopInventory().equals( event.getClickedInventory() ) )
        {
            if ( blockPlayerInventory )
            {
                event.setCancelled( true );
                event.setResult( Event.Result.DENY );
            }
            return;
        }
        InventoryItem clicked = activeItems[event.getSlot()];
        ItemAction action = ItemAction.getForClickType( event.getClick() );
        clicked.callAction( this, action );
        if ( clicked.locked() )
        {
            event.setCancelled( true );
            event.setResult( Event.Result.DENY );
        }
    }

}
