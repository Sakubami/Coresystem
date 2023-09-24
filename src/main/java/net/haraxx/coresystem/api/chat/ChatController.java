package net.haraxx.coresystem.api.chat;

public class ChatController {

    public String cGeneric(String s) {
        String newmsg = "§7[§6Haraxx§7.§6net§7] §7"+ s;
        return newmsg;
    }

    public String cError(String s) {
        String newmsg = "§7[§6Haraxx§7.§6net§7] §c"+ s;
        return newmsg;
    }

    public String cSucces(String s) {
        String newmsg = "§7[§6Haraxx§7.§6net§7] §a"+ s;
        return newmsg;
    }
}
