package net.haraxx.coresystem.message;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Content;
import org.bukkit.command.CommandSender;

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

    private final List<TextComponent> lines;
    private TextComponent currentLine;

    public MessageBuilder()
    {
        this.lines = new ArrayList<>();
        this.currentLine = new TextComponent();
    }

    public MessageBuilder( Messages messages )
    {
        this.lines = messages.lines;
        this.currentLine = new TextComponent();
    }

    public MessageElementBuilder beginElement( String text )
    {
        return new MessageElementBuilder( text );
    }

    public MessageBuilder legacy( String legacyText )
    {
        return add( TextComponent.fromLegacyText( legacyText ) );
    }

    public MessageBuilder add( BaseComponent... components )
    {
        for ( BaseComponent bc : components )
        {
            currentLine.addExtra( bc );
        }
        return this;
    }

    public MessageBuilder raw( String text )
    {
        currentLine.addExtra( text );
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

        private final List<TextComponent> lines;

        private Messages( List<TextComponent> lines )
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
    }

    public final class MessageElementBuilder
    {

        private final TextComponent component;

        private MessageElementBuilder( String string )
        {
            this.component = new TextComponent( string );
        }

        public MessageBuilder endElement()
        {
            MessageBuilder.this.add( component );
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

        public MessageElementBuilder click( ClickEvent.Action action, String value )
        {
            component.setClickEvent( new ClickEvent( action, value ) );
            return this;
        }

        public MessageElementBuilder hover( HoverEvent.Action action, Content... contents )
        {
            component.setHoverEvent( new HoverEvent( action, contents ) );
            return this;
        }

    }

}
