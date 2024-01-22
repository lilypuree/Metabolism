package lilypuree.metabolism.config;

import lilypuree.metabolism.platform.Services;
import lilypuree.metabolism.platform.services.MetabolismClientConfig;
import lilypuree.metabolism.platform.services.MetabolismServerConfig;

public class Config {
    public static final MetabolismClientConfig CLIENT = Services.load(MetabolismClientConfig.class);
    public static final MetabolismServerConfig SERVER = Services.load(MetabolismServerConfig.class);

    public static void init() {
    }
}
