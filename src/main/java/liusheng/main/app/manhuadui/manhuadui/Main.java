package liusheng.main.app.manhuadui.manhuadui;


import liusheng.main.app.manhuadui.manhuadui.donwload.CartoonDonwloader;

public class Main {
    public static void main(String[] args) throws Exception{
        String url = "https://www.manhuadui.com/manhua/jiayounvyou/";
        new CartoonDonwloader("f:\\chapter").download(url);
    }
}
