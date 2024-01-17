package lilypuree.metabolism.platform;

import lilypuree.metabolism.platform.services.MetabolismServerConfig;

public class FabricServerConfig implements MetabolismServerConfig {
    @Override
    public boolean preciseFeedback() {
        return false;
    }

    @Override
    public void reload() {

    }
}
