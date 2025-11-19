package Clase;

public class Professor implements Runnable{
    private Examen examen;
    private int tempsTotalExamen;
    private int tempsQueTardaActivarAlarma;
    private int duracioAlarma;

    public Professor(int duracioAlarma, int tempsQueTardaActivarAlarma, int tempsTotalExamen, Examen examen) {
        this.duracioAlarma = duracioAlarma;
        this.tempsQueTardaActivarAlarma = tempsQueTardaActivarAlarma;
        this.tempsTotalExamen = tempsTotalExamen;
        this.examen = examen;
    }

    public Examen getExamen() {
        return examen;
    }

    public void setExamen(Examen examen) {
        this.examen = examen;
    }

    public int getTempsTotalExamen() {
        return tempsTotalExamen;
    }

    public void setTempsTotalExamen(int tempsTotalExamen) {
        this.tempsTotalExamen = tempsTotalExamen;
    }

    public int getDuracioAlarma() {
        return duracioAlarma;
    }

    public void setDuracioAlarma(int duracioAlarma) {
        this.duracioAlarma = duracioAlarma;
    }


    @Override
    public void run() {
        try {
            Thread.sleep(tempsQueTardaActivarAlarma * 1000L);
            examen.activarAlarma();
            Thread.sleep(duracioAlarma * 1000L);
            examen.desActivarAlarma();
            Thread.sleep((tempsTotalExamen * 1000L) - (tempsQueTardaActivarAlarma * 1000L));
            examen.finalitzarExamen();
            System.out.println("Ha acabat l' examen");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
