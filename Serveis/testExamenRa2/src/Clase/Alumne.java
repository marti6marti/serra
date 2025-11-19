package Clase;

public class Alumne implements Runnable{
    private String nom;
    private int quantitatRespostes;
    private Examen examen;
    private int velocitatEscriu;

    public Alumne(String nom, Examen examen, int velocitatEscriu) {
        this.nom = nom;
        this.quantitatRespostes = 0;
        this.examen = examen;
        this.velocitatEscriu = velocitatEscriu;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getQuantitatRespostes() {
        return quantitatRespostes;
    }

    public void setQuantitatRespostes(int quantitatRespostes) {
        this.quantitatRespostes = quantitatRespostes;
    }

    public Examen getExamen() {
        return examen;
    }

    public void setExamen(Examen examen) {
        this.examen = examen;
    }

    public int getVelocitatEscriu() {
        return velocitatEscriu;
    }

    public void setVelocitatEscriu(int velocitatEscriu) {
        this.velocitatEscriu = velocitatEscriu;
    }

    @Override
    public void run() {
        if(quantitatRespostes <= examen.getQuantitatPreguntes() && !examen.isHiHaAlarma() && !examen.isFiExamen()){
            System.out.println("l'alumne " + nom + " està completan l'examen");
            for (int i = 0; i < examen.getQuantitatPreguntes(); i++) {
                try {

                    if(examen.isFiExamen() ){
                        break;
                    }
                    examen.esperarSiHiHaAlarma();
                    Thread.sleep(velocitatEscriu * 1000L);
                    quantitatRespostes = quantitatRespostes + 1;
                    System.out.println("l'alumne " + nom + " ha acabat l' ex " + (i + 1));

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (quantitatRespostes == examen.getQuantitatPreguntes()) {
            System.out.println("L'alumne " + nom + " ha acabat l'examen completament!");
        } else {
            System.out.println("L'alumne " + nom + " ha entregat amb " + quantitatRespostes + " respostes");
        }
    }
}
