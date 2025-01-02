package tr.gov.tubitak.uekae.esya.api.smartcard.object;

import java.util.List;

public class UnwrapObjectsResults {

    private int totalToBeUnwrappedObjects;
    private List<ObjectResult> objectResult;

    public int getTotalToBeUnwrappedObjects() {
        return totalToBeUnwrappedObjects;
    }

    public void setTotalToBeUnwrappedObjects(int totalToBeUnwrappedObjects) {
        this.totalToBeUnwrappedObjects = totalToBeUnwrappedObjects;
    }

    public List<ObjectResult> getObjectResult() {
        return objectResult;
    }

    public void setObjectResult(List<ObjectResult> objectResult) {
        this.objectResult = objectResult;
    }

    public int unwrappedObjectTotal(){
        int counter = 0;
        for (ObjectResult result : objectResult) {
            if(result.isSuccess()){
                counter++;
            }
        }
        return counter;
    }

    public boolean isAllSuccess(){
        return this.totalToBeUnwrappedObjects == unwrappedObjectTotal();
    }
}
