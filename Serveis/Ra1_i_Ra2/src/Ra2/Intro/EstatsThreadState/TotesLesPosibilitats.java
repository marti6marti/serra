package Ra2.Intro.EstatsThreadState;

public class TotesLesPosibilitats {
    public static void main(String[] args) {


        // Recorrem tots els estats possibles d'un Thread
        for (Thread.State estat : Thread.State.values()) {
            System.out.println(estat );
            System.out.println();
        }
    }

    /**
     * Retorna una descripció detallada de cada estat d'un fil
     * @param estat L'estat del Thread
     * @return Descripció de l'estat
     */
    public static String descripcio(Thread.State estat) {
        switch (estat) {
            case NEW:
                return "El fil s'ha creat però encara no s'ha iniciat amb start().\n" +
                        "   Exemple: Thread t = new Thread(...)";

            case RUNNABLE:
                return "El fil està preparat per executar-se o s'està executant actualment.\n" +
                        "   Després de cridar start(), el fil passa a RUNNABLE.\n" +
                        "   La CPU pot estar executant-lo o esperant el seu torn.";

            case BLOCKED:
                return "El fil està esperant per obtenir un recurs o monitor.\n" +
                        "   Això passa dins d'un bloc synchronized quan un altre fil\n" +
                        "   té el lock (bloqueig) del recurs.";

            case WAITING:
                return "El fil està esperant INDEFINIDAMENT que un altre fil el desperti.\n" +
                        "   Això passa amb wait(), join() sense timeout, o\n" +
                        "   LockSupport.park(). Un altre fil ha de cridar notify() o notifyAll().";

            case TIMED_WAITING:
                return "El fil està esperant durant un temps LIMITAT.\n" +
                        "   Això passa amb sleep(ms), wait(ms), join(ms), o\n" +
                        "   LockSupport.parkNanos(). Després del temps, es desperta automàticament.";

            case TERMINATED:
                return "El fil ha acabat d'executar el mètode run() i ja no pot\n" +
                        "   tornar a iniciar-se. El fil ha finalitzat completament.";

            default:
                return "Estat desconegut.";
        }
    }
}