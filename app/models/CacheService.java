package models;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import play.Play;
import play.cache.Cache;

import java.io.IOException;

/**
 * Created by shimizu on 2014/06/09.
 */
public class CacheService {

	private static String MEMCACHE_HOST = Play.application().configuration().getString("memcached.host");
	private static String MEMCACHE_USER = Play.application().configuration().getString("memcached.user");
	private static String MEMCACHE_PASSWORD = Play.application().configuration().getString("memcached.password");

	private static MemcachedClient mc;

	private static boolean isLocal(){
		return (MEMCACHE_HOST == null || MEMCACHE_USER == null || MEMCACHE_PASSWORD != null);
	}


	private static MemcachedClient getMemcacheClient(){
		if (mc == null) {
			try {
				AuthDescriptor ad = new AuthDescriptor(new String[] { "PLAIN" },
						new PlainCallbackHandler(MEMCACHE_USER, MEMCACHE_PASSWORD));
				mc = new MemcachedClient(
						new ConnectionFactoryBuilder()
								.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
								.setAuthDescriptor(ad).build(),
						AddrUtil.getAddresses(MEMCACHE_HOST));
			} catch (IOException ioe) {
				System.err.println("Couldn't create a connection to MemCachier: \nIOException "
						+ ioe.getMessage());
			}
		}
		return mc;
	}

	public static void set(String key, Object value){
		if (isLocal()){
			Cache.set(key, value);
		} else {
			getMemcacheClient().set(key, 0, value);
		}
	}

	public static Object get(String key){
		if (isLocal()) {
			return Cache.get(key);
		} else {
			return getMemcacheClient().get(key);
		}
	}

	public static void remove(String key){
		if (isLocal()){
			Cache.remove(key);
		} else {
			getMemcacheClient().delete(key);
		}
	}
}
