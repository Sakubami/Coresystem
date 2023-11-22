package net.haraxx.coresystem.api.message;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Juyas
 * @version 11.11.2023
 * @since 11.11.2023
 */
public class MessageBuilder
{

    private final List<BaseComponent> lines;
    private TextComponent currentLine;

    public static MessageBuilder create()
    {
        return new MessageBuilder();
    }

    public static MessageBuilder of( Messages messages )
    {
        return new MessageBuilder( messages );
    }

    private MessageBuilder()
    {
        this.lines = new ArrayList<>();
        this.currentLine = new TextComponent();
    }

    private MessageBuilder( Messages messages )
    {
        this.lines = messages.lines;
        this.currentLine = new TextComponent();
    }

    public MessageElementBuilder beginElement( String text )
    {
        return new MessageElementBuilder( text );
    }

    public MessageElementBuilder beginElementLegacy( String text )
    {
        return new MessageElementBuilder( text, true );
    }

    public MessageBuilder legacy( String legacyText )
    {
        return extra( TextComponent.fromLegacyText( legacyText ) );
    }

    public MessageBuilder extra( BaseComponent... components )
    {
        for ( BaseComponent bc : components )
        {
            currentLine.addExtra( bc );
        }
        return this;
    }

    public MessageBuilder raw( String text )
    {
        currentLine.addExtra( ChatColor.stripColor( text ) );
        return this;
    }

    public MessageBuilder newLine()
    {
        this.lines.add( currentLine );
        this.currentLine = new TextComponent();
        return this;
    }

    public Messages build()
    {
        Messages messages = new Messages( lines );
        messages.lines.add( currentLine );
        return messages;
    }

    public static final class Messages
    {

        private final List<BaseComponent> lines;

        private Messages( List<BaseComponent> lines )
        {
            this.lines = lines;
        }

        public void append( Messages messages )
        {
            this.lines.addAll( messages.lines );
        }

        public void send( CommandSender sender )
        {
            lines.forEach( sender.spigot()::sendMessage );
        }

        public BookMeta printBook()
        {
            BookMeta meta = (BookMeta) Bukkit.getItemFactory().getItemMeta( Material.BOOK );
            meta.spigot().setPages( asArray() );
            return meta;
        }

        public BaseComponent[] asArray()
        {
            return lines.toArray( new BaseComponent[0] );
        }

        public String toLegacyText()
        {
            return TextComponent.toLegacyText( asArray() );
        }

        public String toPlainText()
        {
            return TextComponent.toPlainText( asArray() );
        }

    }

    public final class MessageElementBuilder
    {

        private final TextComponent component;

        private MessageElementBuilder( String string )
        {
            this( string, false );
        }

        private MessageElementBuilder( String string, boolean legacy )
        {
            if ( !legacy )
                this.component = new TextComponent( ChatColor.stripColor( string ) );
            else this.component = new TextComponent( TextComponent.fromLegacyText( string ) );
        }

        public MessageBuilder endElement()
        {
            MessageBuilder.this.extra( component );
            return MessageBuilder.this;
        }

        public MessageElementBuilder bold()
        {
            component.setBold( true );
            return this;
        }

        public MessageElementBuilder italic()
        {
            component.setItalic( true );
            return this;
        }

        public MessageElementBuilder strikethrough()
        {
            component.setStrikethrough( true );
            return this;
        }

        public MessageElementBuilder underline()
        {
            component.setUnderlined( true );
            return this;
        }

        public MessageElementBuilder obfuscate()
        {
            component.setObfuscated( true );
            return this;
        }

        public MessageElementBuilder insertion( String insertion )
        {
            component.setInsertion( insertion );
            return this;
        }

        public MessageElementBuilder font( String font )
        {
            component.setFont( font );
            return this;
        }

        public MessageElementBuilder color( org.bukkit.ChatColor color )
        {
            return color( color.asBungee() );
        }

        public MessageElementBuilder color( ChatColor color )
        {
            component.setColor( color );
            return this;
        }

        public MessageElementBuilder color( Color color )
        {
            return color( ChatColor.of( color ) );
        }

        public MessageElementBuilder color( String legacyColor )
        {
            return color( ChatColor.of( legacyColor ) );
        }

        public MessageElementBuilder click( ClickEvent.Action action, String value )
        {
            component.setClickEvent( new ClickEvent( action, value ) );
            return this;
        }

        public MessageElementBuilder hoverItem( ItemStack stack )
        {
            Item item = new Item( stack.getType().getKey().toString(), stack.getAmount(),
                    stack.getItemMeta() != null ? ItemTag.ofNbt( stack.getItemMeta().getAsString() ) : null );
            return hover( HoverEvent.Action.SHOW_ITEM, item );
        }

        public MessageElementBuilder hoverEntity( org.bukkit.entity.Entity entity, @Nullable BaseComponent customName )
        {
            return hover( HoverEvent.Action.SHOW_ENTITY, new Entity( entity.getType().getKey().toString(), entity.getUniqueId().toString(), customName ) );
        }

        public MessageElementBuilder hoverText( String legacyText )
        {
            return hover( HoverEvent.Action.SHOW_TEXT, new Text( TextComponent.fromLegacyText( legacyText ) ) );
        }

        public MessageElementBuilder hoverText( BaseComponent... text )
        {
            return hover( HoverEvent.Action.SHOW_TEXT, new Text( text ) );
        }

        public MessageElementBuilder hover( HoverEvent.Action action, Content... contents )
        {
            component.setHoverEvent( new HoverEvent( action, contents ) );
            return this;
        }

    }

}
