package hello;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@Import(CachingConfig.class)
public class HelloController {
	private Cache cache;
	
	
	//@Autowired
    //ApplicationContext context;
 
    @Autowired
    CacheManager cacheManager;

    @RequestMapping(value = "/greeting/{name}", method=RequestMethod.GET)
    public String hello(@PathVariable String name) {
        return "Hello " + name;
    }
    
    @RequestMapping(value="/cache-list", method=RequestMethod.GET)
    public Collection<String> cacheList(){
    	return cacheManager.getCacheNames();
    }
    @RequestMapping(value = "/cache-put/{message}", method=RequestMethod.GET)
    public String cachePut(@PathVariable String message){
    	cache = cacheManager.getCache("greetingCache");
    	cache.put(message, message);
    	return "Added to cache: " + message;
    }
    
    @RequestMapping(value = "/cache-get/{message}", method=RequestMethod.GET)
    public String cacheGet(@PathVariable String message){
    	cache = cacheManager.getCache("greetingCache");
    	return (String) cache.get(message).get();
    }
    
    @RequestMapping(value = "/cache-size", method=RequestMethod.GET)
    public long getTotalEhCacheSize() {
        long totalSize = 0l;
        for (String cacheName : cacheManager.getCacheNames()) {
          Cache cache = cacheManager.getCache(cacheName);
          Object nativeCache = cache.getNativeCache();
          if (nativeCache instanceof net.sf.ehcache.Ehcache) {
            net.sf.ehcache.Ehcache ehCache = (net.sf.ehcache.Ehcache) nativeCache;
            totalSize += ehCache.getStatistics().getSize();
          }
        }
        return totalSize;
      }
}