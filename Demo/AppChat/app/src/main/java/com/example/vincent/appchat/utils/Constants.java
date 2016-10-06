package com.example.vincent.appchat.utils;

import java.util.HashMap;

/**
 * Created by vincent on 19/6/2016.
 */
public class Constants {

    public static String FRAGMENT_WELCOME_TAG = "fragment:tag:welcome";
    public static String FRAGMENT_CHANNEL_TAG = "fragment:tag:channel";

    public static String RECEIVER_CHANNEL_TAG = "receiver:tag:channel";
    public static String RECEIVER_MESSAGE_TAG = "receiver:tag:message";

    public static String ACTION_DOWNLOAD_CHANNEL = "service:action:download:channel";
    public static String ACTION_DELETE_CHANNEL = "service:action:delete:channel";

    public static String[] names = new String[]{"Adam", "Ada", "Sam", "Andrew", "Alisa"};
    public static int[] ids = new int[]{23, 68, 96, 89, 22};
    public static int ACTION_SEND_MESSAGE = 0;
    public static int ACTION_LOAD_CHANNEL = 1;
//    public static String SOCKET_IP = "172.16.17.130";
    public static String SOCKET_IP = "192.168.11.7";
    public static String SOCKET_PORT = "8010";

}
