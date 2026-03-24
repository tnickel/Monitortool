package StartFrame.config;

import data.GlobalVar;
import data.Rootpath;

public class StartMonitorConfig {
    public static void initialize(String userdir) {
        // Rootpath setzen
        new Rootpath(userdir, userdir + "\\conf");
        
        // Global Var einlesen
        new GlobalVar();
        GlobalVar.setSilentmode(1);
        
        // TODO: Weitere Initialisierung
    }
}