package tr.gov.tubitak.uekae.esya.api.smartcard.object;

import java.util.List;

public class WrappedObjectsWithAttributes {

    private int totalToBeWrappedObject;
    private List<ObjectResult> objectResult;
    private byte[] wrappedObjects;

    public byte[] getWrappedObjects() {
        return wrappedObjects;
    }

    public void setWrappedObjects(byte[] wrappedObjects) {
        this.wrappedObjects = wrappedObjects;
    }

    public List<ObjectResult> getObjectResult() {
        return objectResult;
    }

    public void setObjectResult(List<ObjectResult> objectResult) {
        this.objectResult = objectResult;
    }

    public int getTotalToBeWrappedObject() {
        return totalToBeWrappedObject;
    }

    public void setTotalToBeWrappedObject(int totalToBeWrappedObject) {
        this.totalToBeWrappedObject = totalToBeWrappedObject;
    }

    public int wrappedObjectTotal(){
        int counter = 0;
        for (ObjectResult result : this.objectResult) {
            if(result.isSuccess()){
                counter++;
            }
        }
        return counter;
    }

    public boolean isAllSuccess(){
        return this.totalToBeWrappedObject == wrappedObjectTotal();
    }
}
