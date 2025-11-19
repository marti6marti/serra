package Clase;

public class Examen {
    private int quantitatPreguntes;
    private boolean fiExamen = false;
    private boolean hiHaAlarma = false;

    public Examen(int quantitatPreguntes) {
        this.quantitatPreguntes = quantitatPreguntes;
        this.fiExamen = fiExamen;
        this.hiHaAlarma = hiHaAlarma;
    }

    public int getQuantitatPreguntes() {
        return quantitatPreguntes;
    }

    public void setQuantitatPreguntes(int quantitatPreguntes) {
        this.quantitatPreguntes = quantitatPreguntes;
    }

    public boolean isFiExamen() {
        return fiExamen;
    }

    public void setFiExamen(boolean fiExamen) {
        this.fiExamen = fiExamen;
    }

    public boolean isHiHaAlarma() {
        return hiHaAlarma;
    }

    public void setHiHaAlarma(boolean hiHaAlarma) {
        this.hiHaAlarma = hiHaAlarma;
    }

    public synchronized void activarAlarma() throws InterruptedException {
        if(!hiHaAlarma){
            hiHaAlarma=true;
            System.out.println(" ALARMA ACTIVADA! Els alumnes es bloquegen");
        }else {
            System.out.println("La alarma ja esta activada");
        }


    }
    public synchronized void esperarSiHiHaAlarma() throws InterruptedException {
        while (hiHaAlarma && !fiExamen) {
            System.out.println(Thread.currentThread().getName() + " està esperant que passi l'alarma...");
            wait();
        }
    }

    public synchronized void desActivarAlarma() {
        if(hiHaAlarma == true){
            hiHaAlarma= false;
            notifyAll();
            System.out.println(" Alarma desactivada. Els alumnes poden continuar");
        }else {
            System.out.println("La alarma no esta activada");
        }
    }

    public synchronized void finalitzarExamen(){
        fiExamen = true;
        notifyAll();
    }

}
