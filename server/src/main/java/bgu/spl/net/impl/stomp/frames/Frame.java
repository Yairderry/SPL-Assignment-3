package bgu.spl.net.impl.stomp.frames;

import java.util.Arrays;
import java.util.HashMap;

public class Frame {
    private final String command;
    protected final HashMap<String, String> headers;
    private final String body;
    private final String endOfLine = "\u0000";

    public Frame(String command, HashMap<String, String> headers, String body) {
        this.command = command;
        this.headers = headers;
        this.body = body;
    }

    public Frame(String frame) {
        String[] lines = frame.split("\n");
        this.command = lines[0];
        this.headers = new HashMap<>();

        int startOfBody = 2;
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].length() == 0) {
                startOfBody = i + 1;
                break;
            }
            String[] header = lines[i].split(":");
            this.headers.put(header[0], header[1]);
        }

        this.body = String.join("\n", Arrays.copyOfRange(lines, startOfBody, lines.length - 1));
    }

    @Override
    public String toString() {
        StringBuilder frame = new StringBuilder(command + "\n");
        for (String key : headers.keySet())
            frame.append(key).append(":").append(headers.get(key)).append("\n");
        frame.append("\n").append(body).append(endOfLine);
        return frame.toString();
    }

    public String getCommand() {
        return command;
    }

    public static String errorBody(Frame frame, String bodyMessage) {
        return "The message:" + "\n-----\n" + frame.toString().replace("\n\u0000", "") + "\n-----\n" + bodyMessage;
    }

    public static String isConnectFrame(Frame frame){
        if (!frame.headers.containsKey("version"))
            return "version header is missing";
        if (frame.headers.get("version").equals("1.2"))
            return "version header is not 1.2";
        return null;
    }

    public static String isDisconnectFrame(Frame frame){
        if (!frame.headers.containsKey("receipt"))
            return "receipt header is missing";
        if (!frame.headers.get("receipt").matches("[0-9]+"))
            return "receipt header is not a number";
        return null;
    }

    public static String isSubscribeFrame(Frame frame){
        if (!frame.headers.containsKey("destination"))
            return "destination header is missing";
        if (!frame.headers.containsKey("id"))
            return "id header is missing";
        if (!frame.headers.get("id").matches("[0-9]+"))
            return "id header is not a number";
        return null;
    }

    public static String isUnsubscribeFrame(Frame frame){
        if (!frame.headers.containsKey("id"))
            return "id header is missing";
        if (!frame.headers.get("id").matches("[0-9]+"))
            return "id header is not a number";
        return null;
    }

    public static String isSendFrame(Frame frame){
        if (!frame.headers.containsKey("destination"))
            return "destination header is missing";
        return null;
    }

}
