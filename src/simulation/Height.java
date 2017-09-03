package simulation;

/**
 * Created by zahar on 18/06/17.
 */
public class Height {

    private int height;

    public Height(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public boolean incHeight() {
        this.height ++;
        if(this.height >= 50) {
            this.height=50;
            return false;
        }

        return true;
    }

    public boolean decHeight() {
        this.height --;
        if(this.height <= 2) {
            this.height=2;
            return false;
        }

        return true;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    //land


}
