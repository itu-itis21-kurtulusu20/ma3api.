package tr.gov.tubitak.uekae.esya.cmpapi.base20.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2013 <br>
 * <b>Date</b>: 5/24/13 - 9:57 AM <p>
 * <b>Description</b>: <br>
 */
public class ProtocolState {

    Item current;
    private boolean supportsPolling;

    ProtocolState(Item head, boolean supportsPolling) {
        current = head;
        this.supportsPolling = supportsPolling;
    }

    public boolean acceptNext(IPKIMessageType messageType){
        List<Item> next = current.getNext();
        for (Item item : next) {
            if(item.getCurrent() == messageType){
                current = item;
                return true;
            }
        }
        return false;
    }

    public boolean isSupportsPolling() {
        return supportsPolling;
    }

    static class Item {
        IPKIMessageType current;
        List<Item> next = new ArrayList<Item>();

        private Item(IPKIMessageType current, List<Item> next) {
            this.current = current;
            this.next = next;
        }

        private Item(IPKIMessageType current, Item next) {
            this.current = current;
            this.next = Arrays.asList(next);
        }
        public Item(IPKIMessageType current) {
            this.current = current;
        }

        void setNext(Item... items) {
            this.next = Arrays.asList(items);
        }

        private List<Item> getNext() {
            return next;
        }

        private IPKIMessageType getCurrent() {
            return current;
        }
    }
}
