package ex1;

public class Ratoli implements Runnable{
    private String nom;
    private int segonsPerMenjar = 100;

    public Ratoli(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void menja(){
        System.out.println("El ratolí " + nom + " ha començat a menjar");
        System.out.println("El ratolí " + nom + " està menjant");
        System.out.println("El ratolí " + nom + " ha acabat de menjar");
    }

    public void setTempsMenjar(int tempsMenjar){

    }

    @Override
    public void run() {
        this.menja();
    }

}
