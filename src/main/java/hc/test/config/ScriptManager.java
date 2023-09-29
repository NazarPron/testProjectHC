package hc.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Optional;

@Configuration
public class ScriptManager {


    @Value("${script.js}")
    private String script;



    @Bean
    public ScriptEngine get() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("graal.js");
        engine.eval(script);
        return engine;
    }
}
