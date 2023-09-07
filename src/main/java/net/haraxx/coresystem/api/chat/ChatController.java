package net.haraxx.coresystem.api.chat;

public class ChatController {

    public static String chatGeneric(String s) {
        String newmsg = "§7[§6Haraxx§7.§6net§7] §7"+ s;
        return newmsg;
    }

    public static String chatError(String s) {
        String newmsg = "§7[§6Haraxx§7.§6net§7] §c"+ s;
        return newmsg;
    }

    public static String chatSucces(String s) {
        String newmsg = "§7[§6Haraxx§7.§6net§7] §a"+ s;
        return newmsg;
    }
}
