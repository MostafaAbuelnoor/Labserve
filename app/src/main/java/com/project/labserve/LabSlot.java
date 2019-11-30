package com.project.labserve;

import java.util.ArrayList;
import java.util.HashMap;

public class LabSlot {
    private HashMap<String, String> slot;

    LabSlot(){
        slot = new HashMap<String, String>();
    }

    LabSlot(String slot){

        this.slot = new HashMap<String, String>();
        String from = slot.split(" ")[0];
        String to = slot.split(" ")[1];
        this.slot.put("from", from);
        this.slot.put("to", to);

    }

    public void setSlot(String slot){
        String from = slot.split(" ")[0];
        String to = slot.split(" ")[1];
        this.slot.put("from", from);
        this.slot.put("to", to);
    }

    public void setFrom(String from){
        if(slot.containsKey("from")) {
            slot.remove("from");
        }
        slot.put("from", from);
    }

    public String getFrom(){
        return slot.get("from");
    }

    public String getTo(){
        return slot.get("to");
    }

    public void setTo(String to){
        if(slot.containsKey("to")) {
            slot.remove("to");
        }
        slot.put("to", to);
    }

}
