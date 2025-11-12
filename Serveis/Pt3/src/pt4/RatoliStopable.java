package pt4;

public class RatoliStopable extends Ratoli{
    private Boolean corrent = true;

    public RatoliStopable(String nom) {
        super(nom);
    }
    public void StopRuning (){
        corrent = false;
    }

}
