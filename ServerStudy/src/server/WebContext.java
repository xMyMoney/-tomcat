package server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebContext {

    private List<Entity> entityList;
    private List<Mapping> mappingList;
    private Map<String ,String> entityMap = new HashMap<>();
    private Map<String ,String> mappingMap = new HashMap<>();

    public WebContext(List<Entity> entityList, List<Mapping> mappingList) {
        this.entityList = entityList;
        this.mappingList = mappingList;
        for (Entity entity : entityList) {
            entityMap.put(entity.getName(),entity.getClz());
        }
        for (Mapping mapping : mappingList) {
            for (String pattern : mapping.getPatterns()) {
                mappingMap.put(pattern,mapping.getName());
            }
        }
    }

    /**
     * 通过URL找到对应的类名
     * @param pattern
     * @return
     */
    public String getClz(String pattern) {
        String name = mappingMap.get(pattern);
        return entityMap.get(name);
    }
}
