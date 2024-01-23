package lilypuree.metabolism.platform.services;

public interface MetabolismServerConfig {

    boolean preciseFeedback();
    
    boolean disableHeat();

    boolean convertResources();


    void reload();
}
